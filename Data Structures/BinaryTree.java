package Database;


/**
 * Uses a binary search tree to store a database of
 * PlayStation users. Nodes are ordered by user unique key (see the
 * User class for more detail).
 * @author Jarrod Wilson
 * @version 1.0
 */
public class BinaryTree {

	public User root;

	//global variable for mostPlatinums() recursion
	private User maxPlatinums = null;

	/**
	 * Method to add user to tree sorted by key
	 * @param friend The friend to be added
	 * @return true if  successfully added, false for all error cases
	 * @throws IllegalArgumentException if friend is null
	 */
	public boolean beFriend(User friend) throws IllegalArgumentException {

		//error handling
		if(friend == null) {
			throw new IllegalArgumentException();
		}


		//checks if friend already in tree
		User inTree = findUser(this.root, friend);

		if(inTree != null) {

			if(friend.getUsername().equals(this.root.getUsername())) {

				return false;
			}

			else {
				if (this.root.getUsername().equals(inTree.getUsername())) {

				}
				else {
					return false;
				}
			}

		}

		//if tree is empty
		if(this.root == null) {
			this.root = friend;
			return true;
		}

		else {
			//recursive method to add node
			insertNode(this.root, friend);
			return true;
		}

	}

	/**
	 * SMethod removes friend from BST.
	 * @param friend the friend to remove
	 * @return true if successfully removed, false for all error cases
	 * @throws IllegalArgumentException if friend is null
	 */
	public boolean deFriend(User friend) throws IllegalArgumentException {


		//error handling
		if(friend == null) {
			throw new IllegalArgumentException();
		}
		
		User inTree = findUser(this.root, friend);

		if(inTree != null) {

			if(friend.getUsername().equals(this.root.getUsername())) {
			}
			else {
				if (this.root.getUsername().equals(inTree.getUsername())) {

					return false;
				}
			}
		}

		User tempFriend = new User(friend);
		//calls helper method
		deleteNode(this.root, friend);

		//checks if user is still in tree for return

		User checkDelete = findUser(this.root, friend);


		if(friend.getUsername().equals(root.getUsername())) {//checking if root is deleted

			if(checkDelete.getUsername().equals(root.getUsername())) {

				return true;
			} else
				return false;
		}
		else {


			if(checkDelete.getUsername().equals(root.getUsername())){

				return true;
			} else {

				if(tempFriend.getUsername().equals(friend.getUsername())) {
					return false;
				}
				return true;
			}
		}
	}

	/**
	 * This method returns the number of higher ranked users that the provided reference
	 * user, or zero if there are none.
	 * @param reference The starting point in the search
	 * @return Number of higher ranked users or -1 if user not found
	 * @throws IllegalArgumentException if reference is null
	 */
	public int countBetterPlayers(User reference) throws IllegalArgumentException {

		//error handling
		if(reference == null) {
			throw new IllegalArgumentException();
		}

		//recursive function to find user in tree
		User inTree = findUser(this.root, reference.getUsername());


		//checks reference is in tree and account for findUser returning root user
		//returns -1 if not found
		if(this.root != reference && inTree == this.root) {
			return -1;
		}

		//recursive function to count users with level > than goal
		int counter = getCountHigher(this.root, reference.getLevel());

		return counter;
	}

	/**
     * This method finds all friends who have a lower level than you, or zero if
	 * there are none.
	 * @param reference The starting point in the search
	 * @return Number of lower ranked users
	 * @throws IllegalArgumentException if reference is null
	 */
	public int countWorsePlayers(User reference) throws IllegalArgumentException {

		//error handling
		if(reference == null) {
			throw new IllegalArgumentException();
		}

		//recursive function to find user in tree
		User inTree = findUser(this.root, reference.getUsername());


		//checks reference is in tree and account for findUser returning root user
		//returns -1 if not found
		if(this.root != reference && inTree == this.root) {
			return -1;
		}

		//if root doesn't match pass in and temp user = root, return false

		//recursive function to count users with level < than goal

		int counter = getCountLower(this.root, reference.getLevel());

		return counter;
	}

	/**
     * This method should returns the user with the highest number of platinum trophies.
	 * @return the user with the most platinum trophies, or null if there are none
	 */
	public User mostPlatinums() {

		//copies root into max for comparison
		this.maxPlatinums = new User(this.root);

		//uses helper function to search tree for correct user
		maxPlatFinder(this.root);

		//returns updated max user
		return maxPlatinums;
	}

	/**
	 * You or one of your friends bought a new game! This method should add it to their
	 * GameList.
	 * @param username The user who has bought the game
	 * @param game The game to be added
	 */
	public void addGame(String username, Game game) throws IllegalArgumentException {

		//error handling
		if(username == null || game == null) {
			throw new IllegalArgumentException();
		}

		// finds matching user in tree
		User currentUser = findUser(this.root, username);

		//if user not in tree, throws exception
		if(currentUser == null) {
			throw new IllegalArgumentException();
		}

		if(currentUser.getUsername().equals(username)) {
			currentUser.addGametoList(game);
		}                            

	}

	/**
	 * You or one of your friends achieved a new trophy! This method should add it to
	 * their trophies.
	 * @param username The user who has earned a new trophy
	 * @param trophy The trophy to be added 
	 */
	public void addTrophy(String username, Trophy trophy) throws IllegalArgumentException {

		//Initial error handling
		if(username == null || trophy == null) {
			throw new IllegalArgumentException();
		}

		//recursive method finds matching user name in tree
		User currentUser = findUser(this.root, username);

		//if user name not in list, throws exception
		if(currentUser == null) {
			throw new IllegalArgumentException();
		}

		//on match, adds trophy to trophy array list
		if(currentUser.getUsername().equals(username)) {
			currentUser.addTrophyToList(trophy);
		}

	}

	/**
	 * You or one of your friends has gained one level! this method
	 * updates the level and reorganises the tree.
	 * @param username The user whose level has increased
	 */
	public void levelUp(String username) throws IllegalArgumentException {

		//error handling
		if(username == null) {
			throw new IllegalArgumentException();
		}

		//finds user to level up
		User levelUpUser = findUser(this.root, username);

		//throws exception if not in tree
		if(levelUpUser.getUsername().equals(null)) { 
			throw new IllegalArgumentException();
		}

		//removes original tree node
		deFriend(levelUpUser);

		//increase level
		levelUpUser.addLevel();

		//update key
		levelUpUser.setKey(levelUpUser.calculateKey());

		//return node to tree with updated key value
		beFriend(levelUpUser);
	}


	/**
	 * toString -  print out the details of each user, traversing the tree in order.
	 * @return A string version of the tree
	 */
	public String toString() {

		//returns output of helper method
		return   toStringInorder(this.root);
	}

	//helper Methods


	//Helper Method - find user node via string value
	private User findUser(User root, String userName) {

		if (root == null) {
			return (User) null;
		}

		// returns user on match with string
		if (root.getUsername().equals(userName)) {

			return root;
		}

		User foundUserLeft = findUser(root.getLeft(), userName);
		User foundUserRight =findUser(root.getRight(), userName);

		if(foundUserLeft != null ) {

			//updates node for final return if match
			if(foundUserLeft.getUsername().equals(userName)) {

				root = foundUserLeft;

			}
		}

		if(foundUserRight != null ) {
			//updates node for final return if match
			if(foundUserRight.getUsername().equals(userName)) {
				root = foundUserRight;

			}
		}
		return root;

	}

	//Helper Method - find user node via user key value

	private User findUser(User root, User key) {
		if (root == null) {
			return (User) null;
		}
		if (root.getKey() == key.getKey()) {
			return root;
		}

		User foundUserLeft = findUser(root.getLeft(), key);
		User foundUserRight =findUser(root.getRight(), key);

		if(foundUserLeft != null ) {

			//updates node for final return if match
			if(foundUserLeft.getKey() == key.getKey()) {

				root = foundUserLeft;
			}
		}
		if(foundUserRight != null ) {
			//updates node for final return if match
			if(foundUserRight.getKey() == key.getKey()) {
				root = foundUserRight;

			}
		}

		return root;
	}


	//recursive toString Method to build toString output

	private String toStringInorder(User root) {

		String s = "";
		if (root == null) {
			return "";
		}


		s += toStringInorder(root.getLeft());
		s += "\n" +root.toString()+ "\n"  ;
		s += toStringInorder(root.getRight());

		return s.trim() ; 
	}



	//recursive search for users of a higher level than root
	int getCountHigher(User root, int goal)  {


		// Base Case
		if(root == null)
			return 0;

		// If current user level is greater than goal level, then 
		// include it in count and recur for left and right children of it
		if(root.getLevel() > goal) {

			return 1 + this.getCountHigher(root.getLeft(), goal)+
					this.getCountHigher(root.getRight(), goal);
		}
		else if(root.getLevel() < goal) {
			return this.getCountHigher(root.getRight(), goal);
		}
		else {
			//if root.level equals goal, moves past without impacting count
			return this.getCountHigher(root.getLeft(),goal)+
					this.getCountHigher(root.getRight(), goal); 
		}
	}


	//recursive search for users of a lower level than root
	int getCountLower(User root, int goal)  {

		// Base Case
		if(root == null)
			return 0;

		// If current user level is less than goal level, then 
		// include it in count and recur for left and right children of it
		if(root.getLevel() < goal) {

			return 1 + this.getCountLower(root.getLeft(), goal)+
					this.getCountLower(root.getRight(), goal);
		}
		else if(root.getLevel() > goal) {
			return
					this.getCountLower(root.getLeft(), goal);
		}
		else {
			//if root.level equals goal, moves past without impacting count
			return this.getCountLower(root.getLeft(),goal)+
					this.getCountLower(root.getRight(), goal); 
		}
	}

	//recursive helper method to add nodes to tree
	private void insertNode(User root, User newFriend) {


		//if the root key is less than new friend key
		//recursively tries to add till left child is empty
		if (newFriend.getKey() < root.getKey()) {
			if (root.getLeft() == null) {
				root.setLeft(newFriend);
			}

			else
				insertNode(root.getLeft(), newFriend);
		}

		else {
			//if the root key is greater than than new friend key
			//recursively tries to add till right child is empty
			if (root.getRight() == null) {
				root.setRight(newFriend);
			}

			else
				insertNode(root.getRight(), newFriend);
		}
	}


	//recursive method that finds the user with the most platinum
	//includes edge case if two users has the same value and then checks
	//number of gold trophies

	private void maxPlatFinder(User root) {

		//exception for stopping exceptions
		if(root == null) {
			throw new NullPointerException();
		}


		if(root.getLeft() != null) {

			maxPlatFinder(root.getLeft());
		}


		//checks node against max value, if greater updates max to root
		if(root.platinumCounter() > maxPlatinums.platinumCounter()) {
			maxPlatinums = root;

		}

		//edge case for matching plat values
		else if(root.platinumCounter() == maxPlatinums.platinumCounter()) {
			if (root.goldCounter() > maxPlatinums.goldCounter()) {
				maxPlatinums = root;
			}
		}

		if(root.getRight() != null) {
			maxPlatFinder(root.getRight());
		}

	}



	//helper method to delete node from tree.
	private User deleteNode(User root, User key) {

		User tempUser = null;
		//base case to return recursion
		if(root == null) {
			return null;
		}

		//traversal - Find node
		if(root.getKey() > key.getKey()) {
			tempUser = deleteNode(root.getLeft(), key);
			if(tempUser == null) {

				root.setLeft(null);
			}
		}

		else if (root.getKey() < key.getKey()) {
			tempUser = deleteNode(root.getRight(), key);
			if(tempUser == null) {

				root.setRight(null);
			}
		}

		else {

			//Case 1 - removes node when no child nodes exist

			if (root.getLeft() == null && root.getRight() == null) {

				root = null;
				return root;
			}


			//Case 2 - node to be deleted has only one child
			else if(root.getLeft() == null || root.getRight() == null) {

				User temp = null;

				temp = root.getLeft() == null ? root.getRight() : root.getLeft();

				if(temp == null) {
					return null;
				} else {
					root = temp;
					return root;
				}

			}
			//case 3 - node to delete has both left and right child

			else {

				User successor = getSuccessor(root.getLeft());

				User tempRight = root.getRight();
				User templeft = root.getLeft();


				root.userOverWrite(successor);
				root.setLeft(templeft);
				root.setRight(tempRight);

			}
		}
		return root;
	}


	//helper method to find biggest key user
	private User getSuccessor(User root) {
		if(root == null ) {
			return null;
		}

		User temp = root.getRight();
		User prevTemp = root;

		while (temp.getRight() != null) {

			prevTemp = temp;
			temp = temp.getRight();

		}

		prevTemp.setRight(null);


		return temp;
	}

