package ds.students;

import java.util.EmptyStackException;
import ds.interfaces.Stack;

/**
 * 
 * @author Jarrod WILSON
 * 
 * @param  The type of object stored in this Stack. 
 */
public class DSStack extends Stack {

	//private node to hold top of stack
	private Node topOfStack; 


	/**
	 * empty constructor
	 */
	public DSStack() {
		topOfStack = null;
	}
	
	/**
	 * copy constructor (Deep Copy)
	 * @param other
	 */
	public DSStack(DSStack other) {

		if(other.topOfStack == null) { // if other list object is empty, sets deep copy object to empty
			this.topOfStack = null;
			return;
		}

		Node iteratorNode = other.topOfStack; //creates copy of current head of stack node
		while(iteratorNode != null) {

			this.add(iteratorNode.getToken()); //calls add method to add node object to stack
			iteratorNode = iteratorNode.next;
		}
	}

	/**
	 * Adds the given object to the top of the Stack. 
	 * @return The given object. 
	 */
	public Token push(Token obj) {

		//error handling

		if(obj == null) {
			throw new  NullPointerException();
		}

		//create insert node
		Node insertNode = new Node(null,null, obj);

		//if top of stack is null

		if(topOfStack == null) {
			topOfStack = insertNode;
			return topOfStack.getToken();
		}

		//iterate through nodes
		else if (topOfStack != null) {

			insertNode.next = topOfStack; // points new node at top of stack
			topOfStack.prev = insertNode; // points top of stack .prev to insert Node
			topOfStack = insertNode; //updates top of stack to insert node
			return topOfStack.getToken();

		}
		return null;
	}

	/**
	 * Returns, without removing, the object at the top of the Stack. 
	 * @return the object at the top of the Stack. 
	 * 
	 * @throws EmptyStackException if the stack is empty. 
	 */
	public Token peek() {

		if(topOfStack == null) {
			throw new EmptyStackException();
		}

		Token returnToken = topOfStack.getToken(); //if top of stack exists, returns node token

		return returnToken;
	}

	/**
	 * Returns and removes the object at the top of the Stack. 
	 * @return the object at the top of the Stack. 
	 * 
	 * @throws EmptyStackException if the stack is empty.
	 */
	public Token pop() {

		//check stack isn't empty before pop
		if(topOfStack == null) {
			throw new EmptyStackException();
		}

		//creates token to return from top of stack
		Token returnToken = topOfStack.getToken();

		//sets top of stack to null if only one item in stack

		if(topOfStack.next == null) {
			topOfStack = null;
		}

		else {
			topOfStack = topOfStack.next;
			topOfStack.prev = null;
		}

		return returnToken;
	}

	public boolean isEmpty() { //if top Stack node is null, returns true 
		if(topOfStack == null ){
			return true;
		}
		return false;
	}

	public int size() {
		int counter = 0; //return int

		if(this.topOfStack == null) { //if topOfStack is null returns 0
			return counter;

		}

		if(this.topOfStack != null) { //iterates through stack form topOfStack, increasing counter
			Node iteratorNode = this.topOfStack;
			counter = 1;
			while(iteratorNode.next != null) {
				iteratorNode = iteratorNode.next;
				counter++;
			}
		}

		return counter; //return final count value


	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {

		return true;
	}

	@Override
	public String toString() {
		Node nodeRef = this.topOfStack; //gets top of stack node
		StringBuilder result = new StringBuilder(); //sets up result string
		while(nodeRef != null) { //while not null
			result.append(nodeRef.getToken()); //adds data to return string
			if(nodeRef.next != null) { //if there's a next value
				result.append(" "); //adds in breaker
			}
			nodeRef = nodeRef.next; // sets nodeRef to next value in stack

		}
		return result.toString();

	}



	///HELPER METHODS - added to facilitate copy constructor

	private boolean add(Token obj) {

		if(obj == null) {
			throw new NullPointerException();
		}
		//create node obj to attach
		Node addNode = new Node(null, null, obj); // creates node to add to end of list


		if(this.topOfStack == null) { //if head is null, adds new node as head
			this.topOfStack = addNode;
			return true;
		}

		Node currentListNode = this.topOfStack; //creates node of list head to iterate

		while(currentListNode.next != null) { //Iterates to find last node in list
			currentListNode = currentListNode.next;

		}

		currentListNode.next = addNode; //adds node to next of last node in list

		addNode.prev = currentListNode;
		return true;

	}
	
	//gets top of stack node
	public Node getTopOfStack() {
		return this.topOfStack;
	}
}
