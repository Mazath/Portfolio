package Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to represent a PlayStation game trophy. A trophy comes in
 * four ranks: bronze, silver, gold and platinum. The date the trophy was
 * earned and its respective game is also stored.
 * @author Jarrod Wilson
 * @version 1.0
 */
public class Trophy {

	public enum Rank {
		BRONZE, SILVER, GOLD, PLATINUM
	}

	public enum Rarity {
		COMMON, UNCOMMON, RARE, VERY_RARE, ULTRA_RARE
	}

	private String name;
	private Rank rank;
	private Rarity rarity;
	private Calendar obtained;
	private Game game;

	public Trophy() {}

	public Trophy(String name, Rank rank, Rarity rarity, Calendar obtained, Game game) {

		this.name = name;
		this.rank = rank;
		this.rarity = rarity;
		this.obtained = obtained;
		this.game = game;
	}

	public String getName() {

		return this.name;
	}

	public Rank getRank() {
		return this.rank;
	}

	public Rarity getRarity() {
		return this.rarity;
	}

	public Calendar getObtained() {
		return this.obtained;
	}

	public Game getGame() {
		return this.game;
	}

	//setter for obtained only mutator needed as the other variables shouldn't be changed once created

	public void setObtained(Calendar obtained) {
		this.obtained = obtained;
	}


	@Override
	public String toString() {
		return "\"" + this.name+ "\", rank: " + this.rank +", rarity: " + this.rarity
				+ ", obtained on: " + DateFormatter();
	}


	private String  DateFormatter() {

		String returnString = "";

		Date obtainedDate = this.obtained.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

		returnString = sdf.format(obtainedDate);

		return returnString;

	}
}
