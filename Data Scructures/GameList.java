package Database;


/**
 * Class to represent a single linked-list of Database.Game objects.
 * @author Jarrod Wilson
 * @version 1.0
 */
public class GameList {

	public Game head;



	public GameList(Game head) {
		this.head = head;
	}

	public Game getGame(String game) {

		if(game == null) {
			throw new IllegalArgumentException();
		}

		Game iteratorGame = this.head;

		while(iteratorGame != null) {

			if(iteratorGame.getName().equals(game)) {
				return iteratorGame;
			}

			iteratorGame = iteratorGame.getNext();
		}

		return null;
	}


	//add game to game list
	public void addGame(Game game) {

		//error handling
		if(game == null) {
			throw new IllegalArgumentException();
		}

		//if list is empty
		if(this.head == null) {
			this.head = game;
		}

		//iterate to last position in list and add providing game doesn't already exist

		Game iteratorGame = this.head;
		Game insertGame = game;

		while(iteratorGame.getNext() != null) {

			//throw exception if matching game object exists in list
			if(iteratorGame.getName().equals(insertGame.getName())){
				throw new IllegalArgumentException();
			}
			else
				iteratorGame = iteratorGame.getNext();
		}

		//if it gets here if iterator reaches end of the list

		iteratorGame.setNext(game);
		iteratorGame = iteratorGame.getNext();
		iteratorGame.setNext(null);

	}


	//remove game identified as matching string and remove from list, changing references

	public void removeGame(String game) {

		//Error handling
		if(game == null) {
			throw new IllegalArgumentException();
		}

		//edge case for head is only object in list
		if(this.head.getNext() == null) {
			if(this.head.getName().equals(game)) {
				this.head = null;
			}
			else
				throw new IllegalArgumentException();
		} 

		Game iteratorGame = this.head; //object to iterate through game list
		Game referenceHolder = this.head; // object to hold prev node details to amend next following removal 

		//when match is found
		if(iteratorGame.getName().toString().equals(game)) {

			referenceHolder.setNext(iteratorGame.getNext());

			//if iterator game is head of list, update head to next item in list
			if(iteratorGame == this.head) {
				this.head = iteratorGame.getNext();
			}
		}

		//move iterator after checking
		while(!iteratorGame.getName().toString().equals(game) &&
				iteratorGame.getNext() != null) {

			referenceHolder = iteratorGame; //update holder reference and move iterator to next game
			iteratorGame=iteratorGame.getNext();

			if(iteratorGame.getName().toString().equals(game) &&
					iteratorGame.getNext() != null) {
				referenceHolder.setNext(iteratorGame.getNext());
			}

			else if(iteratorGame.getName().toString().equals(game) &&
					iteratorGame.getNext() == null) {
				referenceHolder.setNext(null);
			}

			else {
				throw new IllegalArgumentException();
			}			
		}
	}


	//Remove game from list without addressing impact on list 

	public void removeGame(Game game) {

		//Error handling
		if(game == null) {
			throw new IllegalArgumentException();
		}

		//edge case for head is only object in list
		if(this.head.getNext() == null) {
			if(this.head == game) {
				this.head = null;
			}
			else
				throw new IllegalArgumentException();
		} 

		Game iteratorGame = this.head; //object to iterate through game list
		Game referenceHolder = this.head; // object to hold prev node details to amend next following removal 

		//when match is found
		if(iteratorGame == game) {

			referenceHolder.setNext(iteratorGame.getNext());

			//if iterator game is head of list, update head to next item in list
			if(iteratorGame == this.head) {
				this.head = iteratorGame.getNext();
			}
		}

		//move iterator after checking
		while(iteratorGame !=game &&
				iteratorGame.getNext() != null) {

			referenceHolder = iteratorGame; //update holder reference and move iterator to next game
			iteratorGame=iteratorGame.getNext();

			if(iteratorGame == game && //on match mid list
					iteratorGame.getNext() != null) {
				referenceHolder.setNext(iteratorGame.getNext());
			}

			else if(iteratorGame == game && //on match at last node
					iteratorGame.getNext() == null) {
				referenceHolder.setNext(null);
			}

			else {
				throw new IllegalArgumentException(); //thrown if game not in list
			}			
		}
	}

	@Override
	public String toString() {

		//output if gameList is empty
		if(this.head == null) {
			return "Empty game list";
		}

		Game iteratorGame = this.head;
		StringBuilder returnString = new StringBuilder(); //sets up result string

		while(iteratorGame != null) {
			returnString.append(iteratorGame.toString());

			if(iteratorGame.getNext() != null){ //adds line between game outputs
				returnString.append("\n");           
			}
			iteratorGame = iteratorGame.getNext();
		}


		return returnString.toString();
	}

}
