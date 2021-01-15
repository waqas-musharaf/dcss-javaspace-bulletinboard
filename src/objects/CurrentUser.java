package objects;

/**
 * Holds current user information during run-time.
 * 
 * @author Waqas Musharaf
 */
public class CurrentUser {

	// Variables
	public static String username;

	// Get method
	public static String getCurrentUser() {
		return CurrentUser.username;
	}

	// Set method
	public static void setCurrentUser(String user) {
		CurrentUser.username = user;
	}
}
