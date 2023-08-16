package ds.students;

import ds.interfaces.Queue;

/**
 * @author Jarrod WILSON
 *
 */
public class DSQueue extends Queue {

	private Node frontOfQueue;

	private Node backOfQueue;


	//empty constructor

	public DSQueue() {

		this.frontOfQueue = null;
		this.backOfQueue = null;
		this.list = new DSList();

	}

	//copy constructor

	public DSQueue(Queue s) {

		if(s.isEmpty()) { // if other  object is empty, sets deep copy object to empty
			this.frontOfQueue = null;
			this.backOfQueue = null;
			return;
		}

		//iteratorNode - uses helper getter to see frontOfQueue from s
		//typeCasts the node references from the calling queue object for the node constructor

		Node iteratorNode = new Node ( ((DSQueue) s).getFrontOfQueue().next, null, ((DSQueue) s).getFrontOfQueue().getToken());
		this.offer(iteratorNode.getToken()); //offers token - first node only, rest of the queue object is added using  the while loop

		while(iteratorNode.next != null) {
			iteratorNode = iteratorNode.next;
			this.offer(iteratorNode.getToken());
		}

	}

	/**
	 * Inserts the given object into the Queue if possible. 
	 * @param t
	 * @return True if the object was inserted. 
	 * 
	 * @throws NullPointerException if the given object is null.
	 */
	@Override
	public boolean offer(Token t) {

		//Token error handling
		if(t == null) {
			throw new  NullPointerException();
		}

		//creates node to add to queue with input token
		Node offerNode = new Node (null,null,t);


		//if queue exists but is empty, adds token to queue
		if(this.backOfQueue == null && this.frontOfQueue == null) {
			this.frontOfQueue = offerNode;
			this.backOfQueue = offerNode;
			return true;
		}

		//Iterate down queue, add token

		else if (this.frontOfQueue != null) {
			Node iteratorNode = this.frontOfQueue; //creates node of list head to iterate

			while(iteratorNode.next != null) { //Iterates to find last node in list
				iteratorNode = iteratorNode.next;

			}

			iteratorNode.next = offerNode; //adds node to next of last node in list

			offerNode.prev = iteratorNode;

			this.backOfQueue = offerNode;
			return true;

		}
		return false;
	}

	/**
	 * Removes and returns the head of the Queue. 
	 * @return The head of the Queue. 
	 */
	@Override
	public Token poll() {
		Token returnToken = frontOfQueue.getToken();

		if (frontOfQueue == null) {
			throw new   NullPointerException();
		}

		if (frontOfQueue.next != null) { //if the front of queue has a next node

			frontOfQueue.prev = null;
			frontOfQueue = frontOfQueue.next;

		}

		else {
			frontOfQueue = null; //set front and back to null if no other items in queue
			backOfQueue = null;
		}

		return returnToken;
	}

	/**
	 * Retrieves, but does not remove, the head of this Queue. 
	 * If the Queue is empty, returns null. 
	 * @return The head of the Queue. 
	 */
	@Override
	public Token peek() {

		if(frontOfQueue.getToken() == null) {
			return null;
		}

		return frontOfQueue.getToken();

	}

	@Override
	public String toString() {
		Node nodeRef = this.frontOfQueue; //gets top of stack node

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

	@Override
	public int size() {

		int counter = 0;

		if(this.frontOfQueue != null) {
			Node iteratorNode = this.frontOfQueue;
			counter = 1;
			while(iteratorNode.next != null) {
				iteratorNode = iteratorNode.next;
				counter++;
			}
		}
		return counter;

	}

	@Override
	public boolean isEmpty() {
		if(frontOfQueue == null && backOfQueue == null) {
			return true;
		}

		return false;
	}



	//HELPER METHODS


	public Node getFrontOfQueue() { //getter to access front of queue for iteration
		return this.frontOfQueue;
	}

	public Node getBackOfQueue() {
		return this.backOfQueue;
	}


}






