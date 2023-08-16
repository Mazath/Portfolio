package ds.students;



import ds.interfaces.List;

/**
 * @author Jarrod WILSON
 *
 */
public class DSList implements List {

	public Node head;


	/**
	 * Empty constructor
	 * takes no parameters and creates DSlist object with head as null
	 */
	public DSList() {
		this.head = null; 

	}

	/**
	 * Constructor - takes node object as parameter and creates DSlist object
	 * @param head_ - Node object to be set as head of list object
	 */
	public DSList(Node head_) {
		this.head = head_; 



	}

	/**
	 * Copy Constructor - creates deep copy of passed in list object
	 * @param other - object to be copied
	 */
	public DSList(DSList other) { // Copy constructor (DEEP COPY).


		if(other.head == null) { // if other list object is empty, sets deep copy object to empty
			this.head = null;
			return;
		}



		Node iteratorNode = other.head; //creates copy of current holder node

		while(iteratorNode != null) {

			this.add(iteratorNode.getToken()); //calls add method to add node object to list
			iteratorNode = iteratorNode.next;


		}

	}



	/**
	 * FROM INTERFACE
	 * Remove the object at the specified index from the list, if it exists.
	 * @param index The index to remove
	 * @return The object previously at the specified index
	 *           
	 * @throws IndexOutOfBoundsException if the specified index is out of range
	 */
	public Token remove(int index) {

		Node iteratorNode = this.head; //node to iterate through list
		Token holderToken = null;
		int size = this.size();

		if (index < 0 || index > size -1 ) { //if index is out of range throw exception
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		if( index == 0 && this.head != null) { //remove head

			holderToken = iteratorNode.getToken(); //captures token for return

			if(iteratorNode.next == null) {
				this.head = null;
				return holderToken;

			}

			iteratorNode.next.prev = null;
			this.head = iteratorNode.next; //updates list head to next node
			return holderToken; //returns original head token
		}



		int nodeCounter = 0; // int to count nodes

		while (iteratorNode.next != null && nodeCounter != index) { //move down list to index node
			iteratorNode = iteratorNode.next;
			nodeCounter++;

			if (iteratorNode.next != null && nodeCounter == index) { //on match

				holderToken = iteratorNode.getToken(); // get token for return
				iteratorNode = iteratorNode.prev; //moves back one node
				iteratorNode.next = iteratorNode.next.next; //steps over node to be removed to set next
				iteratorNode = iteratorNode.next; //moves forward 2 nodes
				iteratorNode.prev = iteratorNode.prev.prev; //steps over node to be removed and sets prev
			}


			else if (iteratorNode.next == null && nodeCounter == index ) { //remove last node

				holderToken = iteratorNode.getToken(); //capture token for return
				iteratorNode.prev.next = null; //sets prev node's next to null
				iteratorNode.prev = null; //remove target node's prev reference
				return holderToken;
			}

		}

		return holderToken; //temp return
	}

	/**
	 * FROM INTERFACE
	 * Returns the first index of the specified object, or -1 if the object does not exist
	 * in the list.
	 * @param token
	 * @return The index of the specified token, or -1 if it is not contained in the list.
	 */
	public int indexOf(Token obj) {
		int counter = 0;

		if(contains(obj)) {
			Node iteratorNode = this.head; //node to iterate through list

			while(iteratorNode.getToken().equals(obj) == false) { // loop to iterate through list to target
				iteratorNode = iteratorNode.next;
				counter++;
			}

			if(iteratorNode.getToken().equals(obj) == true) {
				return counter;
			}
		}

		return -1; // returns -1 if contains does not find calling object
	}

	/**
	 * FROM INTERFACE
	 * Get the object at the specified index, if it exists.
	 * @param index The index to retrieve
	 * @return The object at the specified index, if it exists.
	 *
	 * @throws IndexOutOfBoundsException if the specified index is out of bounds
	 */
	public Token get(int index) {


		if(index > this.size() ||  index < 0) { //throws exception if index is out of range
			throw new IndexOutOfBoundsException();
		}

		Node returnNode = this.head; //return temp variable

		for(int i = 0; i < index; i++) {

			returnNode = returnNode.next; //keeps updating temp node to run through list till provided index

		}
		return returnNode.getToken(); //returns getToken result from target node
	}



	/**
	 * FROM INTERFACE
	 * Returns true if this list contains no elements.
	 * @return True if the list is empty.
	 */
	public boolean isEmpty() {
		if(head == null ) { //if head is null
			return true;
		}
		return false; //if compiler gets here, there is something in the list object.
	}

	/**
	 * FROM INTERFACE
	 * Returns the number of elements in this list.
	 * @return The number of elements in this list.
	 */
	public int size() {
		int counter = 0;

		if(this.head != null) {
			Node iteratorNode = this.head;
			counter = 1;
			while(iteratorNode.next != null) {
				iteratorNode = iteratorNode.next;
				counter++;
			}
		}
		return counter;
	}


	@Override
	/**
	 * FROM INTERFACE
	 * Returns a string containing the toString()
	 *            for each object in this list.
	 * @return The concatenated toString() for each element in this list
	 */
	public String toString() {

		Node nodeRef = this.head; //gets head node
		StringBuilder result = new StringBuilder(); //sets up result string
		while(nodeRef != null) { //while not null
			result.append(nodeRef.getToken()); //adds data to return string
			if(nodeRef.next != null) { //if there's a next value
				result.append(" "); //adds in breaker
			}
			
			nodeRef = nodeRef.next; // sets nodeRef to next value in list
		}
		return result.toString();
	}

	/**
	 * FROM INTERFACE
	 * Appends the specified element to the end of this list.
	 * @param obj The object to add.
	 * @return True if the object has been added to the list.
	 *
	 * @throws NullPointerException if the specified object is null
	 */
	public boolean add(Token obj) {

		if(obj == null) {
			throw new NullPointerException();
		}
		//create node obj to attach
		Node addNode = new Node(null, null, obj); // creates node to add to end of list


		if(this.head == null) { //if head is null, adds new node as head
			this.head = addNode;
			return true;
		}

		Node currentListNode = this.head; //creates node of list head to iterate

		while(currentListNode.next != null) { //Iterates to find last node in list
			currentListNode = currentListNode.next;
		}

		currentListNode.next = addNode; //adds node to next of last node in list

		addNode.prev = currentListNode;
		return true;


	}
	/**
	 * FROM INTERFACE
	 * Inserts the specified element at the specified position in this list.
	 *  Shifts the element currently at that position (if any) and any subsequent
	 *  elements to the right (adds one to their indices).
	 * @param index Index at which to add
	 * @param obj The object to add
	 * @return True if insertion was successful
	 *
	 * @throws NullPointerException if the given object is null
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	public boolean add(int index, Token obj) {
		//error handling
		if(index > this.size() ||  index < 0) { //throws exception if index is out of range
			throw new IndexOutOfBoundsException();
		}

		if(obj == null) { //throws exception if object to be added is null
			throw new NullPointerException();
		}

		Node iteratorNode = this.head; //node to iterate through list
		Node addNode = new Node (null, null, obj);
		int nodeCounter = 0; // int to count nodes

		if(this.head == null && index == 0) { //sets new node to head if index matches and head is null
			this.head = addNode;
			return true;
		}

		if(this.head != null && index == 0) { // add when list only has head

			addNode.next = this.head; //connects new node next to current head
			this.head.prev = addNode; //sets current head prev to new node
			this.head = addNode; //sets new node as head
			return true;
		}


		while (iteratorNode.next != null && nodeCounter != index) { //move iteratorNode down list to index node
			iteratorNode = iteratorNode.next;
			nodeCounter++;

		}


		if ( nodeCounter == index) { //on match

			addNode.next = iteratorNode; //connects new node to iteratorNode .next
			iteratorNode.prev.next = addNode; //connects iteratorNode next to new node
			addNode.prev = iteratorNode.prev; //connects new node prev to iteratorNode
			iteratorNode.prev = addNode; //sets original iteratorNode . next to new node

			return true;

		}

		else if (iteratorNode.next == null && nodeCounter == index-1 ) { //add to end of list
			add(obj);
			return true;
		}



		return false; 

	}

	/**
	 * FROM INTERFACE
	 * Returns true if the given object is contained in the list.
	 *
	 * @param obj The object whose presence is to be tested
	 * @return True if the list contains the given object
	 *
	 * @throws NullPointerException if the specified element is null
	 */
	public boolean contains(Token obj) {

		if(obj == null) { //throws exception if object to be added is null
			throw new NullPointerException();
		}

		if(this.head == null) { //throws exception if head is null
			throw new NullPointerException();

		}

		Node iteratorNode = this.head; //creates node with list head

		while(iteratorNode.next != null) { //while iterator node has a next value
			if(iteratorNode.getToken().equals(obj)) { //checks if tokens match

				return true; //on match return true
			}
			else

				iteratorNode = iteratorNode.next; //move to next node in list
		}

		if(iteratorNode.next == null && iteratorNode.getToken().equals(obj)) { //checks last node for data
			return true;
		}

		return false; //reaching here means no matching node data was found in list.
	}

	/**
	 * FROM INTERFACE
	 * Remove the first instance of the given object from the list, if it exists
	 * @param obj The object to remove
	 * @return True if the object was removed
	 *
	 * @throws NullPointerException if the specified object is null
	 */

	public boolean remove(Token obj) {

		if (obj == null) {
			throw new NullPointerException();
		}

		Node scannerNode = new Node (this.head.next, this.head.prev, this.head.getToken());
		while (scannerNode != null) {
			if(scannerNode.getToken().equals(obj)) {

				if(scannerNode.prev == null ) { //check if node is head          
					this.head = scannerNode.next; //moves node up
					this.head.prev = null; //updates prev
					return true;
				}

				else if (scannerNode.next == null) { //removes if match in last node

					scannerNode.prev.next = null; //sets node before last to tail of list
					scannerNode.prev = null;
					return true;
				}

				else {
					scannerNode.prev.next = scannerNode.next; //joins node a to node c
					scannerNode.next.prev = scannerNode.prev; //joins node c to node a
					scannerNode.next = null;
					scannerNode.prev = null;
					return true;

				}

			}

			scannerNode = scannerNode.next; //moves scanner node
		}

		return false;

	}


	/**
	 * FROM INTERFACE
	 * Returns the hashCode for this list.
	 * (This method must satisfy the constraint that if List l1.equals(List l2),
	 *            then l1.hashCode() == l2.hashCode() must also be true.
	 * @return The hashCode of this list.
	 */
	@Override
	public int hashCode() {
		return (int) System.nanoTime();

	}

	/**
	 *  FROM INTERFACE
	 * Compares this list with the specified object for equality.
	 * The equality comparison must be value-based rather than the default
	 *            (reference based).
	 *
	 * @param obj The object to compare against.
	 * @return True if the specified object is value-comparatively equal to this list
	 */
	@Override
	public boolean equals(Object other) {
		return false;
	}
}
