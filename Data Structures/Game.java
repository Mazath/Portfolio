package Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to represent a PlayStation game.
 * @author Jarrod Wilson
 * @version 1.0
 */

public class Game {

	private String name;
	private Calendar released;
	private int totalTrophies;
	private Game next;

	public Game() {}

	public Game(String name, Calendar released, int totalTrophies) {

		this.name = name;
		this.released = released;
		this.totalTrophies = totalTrophies;
		this.next = null;
	}

	public String getName() {
		return this.name;
	}

	public Calendar getReleased() {
		return this.released;
	}

	public int getTotalTrophies() {
		return this.totalTrophies;
	}

	public Game getNext() {
		return this.next;
	}

	public void setNext(Game next) {
		this.next = next;
	}

	//Set trophies should be only mutator needed to account for game DLC/Expansion content
	//no other local variables should change once object is created

	public void setTotalTrophies(int totalTrophies) {
		this.totalTrophies = totalTrophies;
	}

	@Override
	public String toString() {
		return "\"" + this.name + "\", released on: " + DateFormatter() ; //
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((released == null) ? 0 : released.hashCode());
		result = prime * result + totalTrophies;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (released == null) {
			if (other.released != null)
				return false;
		} else if (!released.equals(other.released))
			return false;
		if (totalTrophies != other.totalTrophies)
			return false;
		return true;

	}



	//Helper method - Date Formatter

	private String  DateFormatter() {

		String returnString = "";

		Date releaseDate = this.released.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy"); //sets date format

		returnString = sdf.format(releaseDate);

		return returnString;

	}



}
