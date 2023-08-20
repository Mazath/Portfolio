#script creates and trains a simple bigram language model using a transformer architecture
#this is a simplified version of the model used in the paper "Attention is all you need" (Vaswani et al., 2017)
#the model is trained on the tinyshakespeare dataset, which is a small subset of the works of Shakespeare
#the model is trained to predict the next character in a sequence of characters


import torch
import torch.nn as nn
from torch.nn import functional as F

#hyperparameters
batch_size = 64 #Number of independent sequences processed in parallel
block_size = 256 #maximum context length for predictions
max_iters = 5000
eval_interval = 500
learning_rate = 3e-4
device = 'cuda' if torch.cuda.is_available() else 'cpu' #run on GPU if avaliable
eval_iters = 200
n_embed = 384 #embedding dimension
n_head = 6 #number of heads
n_layer = 6 #number of layers
dropout = 0.2 #dropout rate
# ------------
# With these hyperparameters, it is recommended to run on a GPU.
# If you run on a CPU, you may want to reduce the number of layers and embedding dimension.
# ------------

#seed set for testing
torch.manual_seed(1337)

# !wget https://raw.githubusercontent.com/karpathy/char-rnn/master/data/tinyshakespeare/input.txt #source of input.txt file if not avaliable
with open('input.txt', 'r', encoding='utf-8') as f:
    text = f.read()

#here are all the unique characters that occur in this text
chars = sorted(list(set(text)))
vocab_size = len(chars)
#create a mapping from characters to integers
stoi = { ch:i for i,ch in enumerate(chars) }
itos = { i:ch for i,ch in enumerate(chars) }
encode = lambda s: [stoi[c] for c in s] #encoder: take a string, output a list of integers
decode = lambda l: ''.join([itos[i] for i in l]) #decoder: take a list of integers, output a string

#Train and test splits
data = torch.tensor(encode(text), dtype=torch.long)
n = int(0.9*len(data)) #90% of data will be used to train 
train_data = data[:n]
val_data = data[n:]

#data loading
def get_batch(split):
    #generate a small batch of data of inputs x and targets y
    data = train_data if split == 'train' else val_data
    ix = torch.randint(len(data) - block_size, (batch_size,))
    x = torch.stack([data[i:i+block_size] for i in ix])
    y = torch.stack([data[i+1:i+block_size+1] for i in ix])
    x, y = x.to(device), y.to(device)
    return x, y


@torch.no_grad() #no grad added for efficiency as we are backpropagating  
def estimate_loss():
    out = {}
    model.eval()
    for split in ['train', 'val']:
        losses = torch.zeros(eval_iters)
        for k in range(eval_iters):
            X, Y = get_batch(split)
            logits, loss = model(X, Y)
            losses[k] = loss.item()
        out[split] = losses.mean()
    model.train()
    return out

#one headed self attention

class Head(nn.Module):

    """one headed self-attention"""

    def __init__(self, head_size):
        super().__init__()
        self.key = nn.Linear(n_embed, head_size, bias=False)
        self.query = nn.Linear(n_embed, head_size, bias=False)
        self.value = nn.Linear(n_embed, head_size, bias=False)
        self.register_buffer('tril', torch.tril(torch.ones(block_size, block_size)))

        self.dropout = nn.Dropout(dropout)

    def forward(self, x):
        B,T,C = x.shape
        #compute queries, keys 
        k = self.key(x) #(B,T,C)
        q = self.query(x) #(B,T,C)
        #compute dot product weights
        wei = q @ k.transpose(-2,-1) * C**-0.5 #(B,T,C) @ (B,C, T) -> (B,T,T)
        wei = wei.masked_fill(self.tril[:T,:T] == 0, float('-inf')) #(B,T,T)
        wei = F.softmax(wei, dim=-1) #(B,T,T)
        wei = self.dropout(wei) #dropout
        #apply attention weights to values
        v = self.value(x) #(B,T,C)
        out = wei @ v #(B,T,T) @ (B,T,C) -> (B,T,C)
        return out


#multi headed self attention
class MultiHead(nn.Module):

    """multi headed self-attention in paralel"""

    def __init__(self, n_heads, head_size):
        super().__init__()
        self.heads = nn.ModuleList([Head(head_size) for _ in range(n_heads)])
        self.proj = nn.Linear(n_embed, n_embed)
        self.dropout = nn.Dropout(dropout)

    def forward(self, x):
        out = torch.cat([h(x) for h in self.heads], dim=-1) #(B,T,C*n_heads)
        out = self.proj(out) #(B,T,C)
        return out

#feed forward

class FeedForward(nn.Module):
    """simple linear layer followed by a nonlinearity"""
    def __init__(self, n_embed):
        super().__init__()
        self.net = nn.Sequential(
            nn.Linear(n_embed, 4 *n_embed),
            nn.ReLU(),
            nn.Linear(4 *n_embed, n_embed), #project back to original dimension
            nn.Dropout(dropout),
        )

    def forward(self, x):
        return self.net(x)

#transformer block
class Block(nn.Module):
    
        """transformer block: communication followed by computation"""
        def __init__(self, n_embed, n_head):
            super().__init__()
            head_size = n_embed // n_head
            self.sa = MultiHead(n_head, head_size)#self attention multihead
            self.ffwd = FeedForward(n_embed)#feed forward layer
            self.ln1 = nn.LayerNorm(n_embed)#layer normalization
            self.ln2 = nn.LayerNorm(n_embed)#layer normalization
    
        def forward(self, x): #adding to x creates residual connections
            x = x + self.sa(self.ln1(x))
            x = x + self.ffwd(self.ln2(x))
            return x
        
#simple Bigram model
class BigramLanguageModel(nn.Module):

    def __init__(self):
        super().__init__()
        #each token directly reads off the logits for the next token from a lookup table
        self.token_embedding_table = nn.Embedding(vocab_size, n_embed)
        self.position_embedding_table = nn.Embedding(block_size, n_embed) #position embedding
        #self.sahead = Head(n_embed) #self attention head
        #self.saheads = MultiHead(4, n_embed//4) #4 heads of 8-dimensional self attention
        self.blocks = nn.Sequential(*[Block(n_embed, n_head=n_head) for _ in range(n_layer)])
        self.ln_f = nn.LayerNorm(n_embed) #final layer normalization
        #self.ffwd = FeedForward(n_embed) #feed forward layer
        self.lmhead = nn.Linear(n_embed, vocab_size) #language model head

    def forward(self, idx, targets=None):
        B, T = idx.shape 
        
        #idx and targets are both (B,T) tensor of integers
        token_emb = self.token_embedding_table(idx) #(B,T,C)
        #add position embeddings
        pos_emb = self.position_embedding_table(torch.arange(T, device=device)) #(T,C)
        x = token_emb + pos_emb #(B,T,C)
        #apply self attention
        #x = self.saheads(x) #(B,T,C)
        #apply feed forward
        #x = self.ffwd(x) #(B,T,C)
        x = self.blocks(x) #(B,T,C)

        logits = self.lmhead(x) #(B,T, vocab_size)

        if targets is None:
            loss = None
        else:
            B, T, C = logits.shape
            logits = logits.view(B*T, C)
            targets = targets.view(B*T)
            loss = F.cross_entropy(logits, targets)

        return logits, loss

    def generate(self, idx, max_new_tokens):
        #idx is (B, T) array of indices in the current context
        for _ in range(max_new_tokens):
            # crop idx to the last block_size tokens
            idx_cond = idx[:, -block_size:] #(B, T)
            #get the predictions
            logits, loss = self(idx_cond)
            #focus only on the last time step
            logits = logits[:, -1, :] #becomes (B, C)
            #apply softmax to get probabilities
            probs = F.softmax(logits, dim=-1) #(B, C)
            #sample from the distribution
            idx_next = torch.multinomial(probs, num_samples=1) #(B, 1)
            #append sampled index to the running sequence
            idx = torch.cat((idx, idx_next), dim=1) #(B, T+1)
        return idx

model = BigramLanguageModel()
m = model.to(device)

#create PyTorch optimizer
optimizer = torch.optim.AdamW(model.parameters(), lr=learning_rate)

for iter in range(max_iters):

    #every once in a while evaluate the loss on train and val sets
    if iter % eval_interval == 0:
        losses = estimate_loss()
        print(f"step {iter}: train loss {losses['train']:.4f}, val loss {losses['val']:.4f}")

    # ample a batch of data
    xb, yb = get_batch('train')

    #evaluate the loss
    logits, loss = model(xb, yb)
    optimizer.zero_grad(set_to_none=True)
    loss.backward()
    optimizer.step()

#generate from the model
context = torch.zeros((1, 1), dtype=torch.long, device=device)
print(decode(m.generate(context, max_new_tokens=500)[0].tolist()))
