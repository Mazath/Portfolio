package ds.students;

import java.util.NoSuchElementException;

import ds.students.Token.Type;

/**
 * @author Jarrod WILSON
 *
 */
public class Calculator {

	public DSQueue infixToPrefix(DSQueue infix) {

		// error handling for influx (null, etc)
		if(infix.isEmpty()) {
			System.out.println("invalid infix"); //fix to throw exception or something
		}

		//local variables
		DSQueue outputQueue = new DSQueue(); //return 
		DSStack outputStack = new DSStack(); //hold while processing

		Token o1 = null; //Precedence token

		int queueSize = infix.size(); //While loop counters
		int queueCounter = 0;

		Node iteratorNode =  infix.getBackOfQueue();


		while(queueCounter < queueSize) { //while loop to detect input tokens exist
			Token readToken = iteratorNode.getToken();

			//checks for parenthesis

			if(readToken.type == Type.PAREN ) {

				if(readToken.toString().equals(")")){
					outputStack.push(readToken);
				}

				else if(readToken.toString().equals("(")){

					Token stackToken = new Token(outputStack.peek());


					while(!stackToken.toString().equals(")")){
						outputQueue.offer(outputStack.pop()); //empty stack between brackets
						stackToken = outputStack.peek();
					}

					outputStack.pop();
				}
			}

			if(readToken.type == Type.OPERAND) {

				outputQueue.offer(readToken); //add integer to output stack
			}


			else if(readToken.type == Type.OPERATOR ) { 

				o1 = readToken; 

				//if the output stack has a operator with a greater than or equal precedence to the read token
				//pop top of stack onto the output queue

				if(!outputStack.isEmpty() && outputStack.peek().getPrecedence() > o1.getPrecedence()) {

					while (!outputStack.isEmpty() && outputStack.peek().getPrecedence() > o1.getPrecedence()) { 
						outputQueue.offer(outputStack.pop());

					}
				}

				outputStack.push(readToken); //push operand to outputQueue

			}

			queueCounter++;
			iteratorNode = iteratorNode.prev;

		} //while loop closing bracket      

		while(!outputStack.isEmpty()) {
			outputQueue.offer(outputStack.pop()); // pop operators from stack onto output queue
		}

		//reverse queue for return output

		while(outputQueue.isEmpty() == false)
		{
			outputStack.push(outputQueue.poll());
		}

		while(outputStack.isEmpty() == false) {
			outputQueue.offer(outputStack.pop());
		}

		return outputQueue;
	}

	public double evaluatePrefix(DSQueue exp)
	{

		// error handling for calling queue
		if(exp.isEmpty()) {

			throw new NoSuchElementException();
		}

		//local variables

		int inputQueueSize = exp.size(); //input queue size for iteration

		DSStack inputStack = new DSStack();

		DSQueue processingQueue = new DSQueue(); //holds input in correct order for processing

		//reverse queue for processing

		while(exp.isEmpty() == false)
		{
			inputStack.push(exp.poll());
		}

		while(inputStack.isEmpty() == false) {
			processingQueue.offer(inputStack.pop());
		}


		Token currentToken = new Token(processingQueue.poll()); 

		DSStack holdingStack = new DSStack(); //stack to hold operands while processing

		int counter = 0; //counter to iterate through nodes

		while (counter < inputQueueSize) {


			if (currentToken.type == Type.OPERAND) { //if token is a operand push to holding stack

				holdingStack.push(currentToken);

			} 

			else if(currentToken.type == Type.OPERATOR) { 
				//get operands from stack
				Token tokenX = new Token(holdingStack.pop());  //top of stack
				Token tokenY = new Token(holdingStack.pop());  //second stack value

				double outcomeReturn = 0.0; //variable to return result to stack

				switch(currentToken.toString()) { //reads the toString of the operator and performs calculation 

				case "+":
					outcomeReturn = tokenX.getOperand() + tokenY.getOperand();
					break;

				case "-":
					outcomeReturn = tokenX.getOperand() - tokenY.getOperand();
					break;

				case "*":
					outcomeReturn = tokenX.getOperand() * tokenY.getOperand();
					break;

				case "/":
					outcomeReturn = tokenX.getOperand() / tokenY.getOperand();
					break;
				}

				Token resultToken = new Token(outcomeReturn);
				holdingStack.push(resultToken); //returns result to stack

			}

			//updates currentToken
			if(!processingQueue.isEmpty()) {
				currentToken = processingQueue.poll();
			}

			counter++;

		} //while loop closing bracket

		return holdingStack.pop().getOperand();
	}


}


