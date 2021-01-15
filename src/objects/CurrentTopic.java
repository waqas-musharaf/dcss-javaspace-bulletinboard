package objects;

/**
 * Holds current topic information during run-time.
 * 
 * @author Waqas Musharaf
 */
public class CurrentTopic {

	// Variables
	public static String owner;
	public static String title;

	// Get Methods
	public static String getCTOwner() {
		return CurrentTopic.owner;
	}

	public static String getCTTitle() {
		return CurrentTopic.title;
	}

	// Set Methods
	public static void setCTOwner(String owner) {
		CurrentTopic.owner = owner;
	}

	public static void setCTTitle(String title) {
		CurrentTopic.title = title;
	}
}
