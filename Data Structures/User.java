package Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Database.Trophy.Rank;

/**
 * Class to represent a PlayStation user.
 * @author Jarrod Wilson
 * @version 1.0
 */
public class User {

	private String username;
	private int level;
	private double key;
	private Calendar dob;
	private ArrayList<Trophy> trophies;
	private GameList games;
	private User left;
	private User right;
	private int height = 1; //used for AVL
	private int balance = 0; //used for AVL

	public User(String username, Calendar dob, int level) {
		this.username = username;
		this.dob = dob;
		this.level = level;
	}

	//copy constructor
	public User(User user) {
		this.username = user.getUsername();
		this.dob = user.getDob();
		this.level = user.getLevel();
		this.key =user.getKey();
		this.trophies = user.getTrophies();
		this.games = user.getGames();
		this.left = user.getLeft();
		this.right = user.getRight();
	}

	/**
	 * DO NOT MODIFY THIS METHOD
	 * This method uses the username and level to create a unique key.
	 * As we don't want the username's hash to increase the level, it's first converted
	 * to a floating point, then added to the level.
	 * @return the unique key
	 */
	public double calculateKey() {
		int hash = Math.abs(username.hashCode());
		// Calculate number of zeros we need
		int length = (int)(Math.log10(hash) + 1);
		// Make a divisor 10^x
		double divisor = Math.pow(10, length);
		// Return level.hash
		return level + hash / divisor;
	}





	public String getUsername() {
		return this.username;
	}

	public int getLevel() {
		return this.level;
	}

	public double getKey() {
		this.key = calculateKey();
		return this.key;
	}

	public Calendar getDob() {
		return this.dob;
	}

	public ArrayList<Trophy> getTrophies() {
		return this.trophies;
	}

	public GameList getGames() {
		return this.games;
	}

	public User getLeft() {
		return this.left;
	}

	public User getRight() {
		return this.right;
	}

	public void setTrophies(ArrayList<Trophy> trophies) {

		this.trophies = trophies;
	}


	public void setGames(GameList games) {
		this.games = games;
	}

	public void setLeft(User left) {
		this.left = left;

	}

	public void setRight(User right) {
		this.right = right;

	}


	@Override
	public String toString() {


		StringBuilder returnString = new StringBuilder();

		//using the string builder to collect relevant details and add in line breaks
		returnString.append("User: " + this.username + "\n");
		returnString.append("\nTrophies: \n" + formattedTrophyToString() + "\n");
		returnString.append("\nGames: \n" + this.getGames().toString() + "\n");
		returnString.append("\nBirth Date: "+ DateFormatter());

		return returnString.toString();

	}



	private String  DateFormatter() {

		String returnString = "";
		Date dateOfBirth = this.dob.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy"); //sets date format
		returnString = sdf.format(dateOfBirth);
		return returnString;

	}

	//helper method for Trophy toString - iterates through array list and matches formatting to expected output

	private String formattedTrophyToString() {

		StringBuilder returnString = new StringBuilder(); //sets up result string
		int counter = 0;
		Trophy iteratorTrophy = this.trophies.get(0);
		while(counter != this.trophies.size()) {
			//add trophy details to string
			returnString.append(iteratorTrophy.toString());

			//increase counter
			counter++;

			//adds line between trophy outputs
			if(counter < this.trophies.size()) {
				returnString.append("\n");                         
			}

			//iterates through array list to update trophy to add to output
			if( counter != this.trophies.size()) {
				iteratorTrophy = this.trophies.get(counter);
			}
		}

		return returnString.toString();
	}

	//helper method to add game

	public void addGametoList(Game game) {
		this.games.addGame(game);
	}

	//helper method to add trophy

	public void addTrophyToList(Trophy trophy) {
		this.trophies.add(trophy);
	}


	//helper method to count platinum trophies
	public int platinumCounter() {
		int counter = 0;

		for(int i = 0; i < this.trophies.size(); i++) {
			Trophy pointerTrophy = this.trophies.get(i);
			if(pointerTrophy.getRank() == Rank.PLATINUM) {
				counter ++;
			}
		}
		return  counter;

	}

	//Gold counter to break ties when looking for max platinum trophies
	public int goldCounter() {
		int counter = 0;

		for(int i = 0; i < this.trophies.size(); i++) {
			Trophy pointerTrophy = this.trophies.get(i);
			if(pointerTrophy.getRank() == Rank.GOLD) {
				counter ++;
			}
		}
		return  counter;

	}


	//Helper method to increase level
	public void addLevel() {
		this.level++;
	}

	//helper method to set key
	public void setKey(double key) {
		this.key = key;
	}

	//Helper method for deletion
	public void userOverWrite (User update) {
		this.key = update.key;
		this.dob = update.dob;
		this.games = update.games;
		this.left = update.left;
		this.right = update.right;
		this.level = update.level;
		this.trophies = update.trophies;
		this.username = update.username;

	}

	//getters and setters for AVL

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBalance() {
		return this.balance;
	}

	public void setbalance(int balance) {
		this.balance = balance;
	}

}

