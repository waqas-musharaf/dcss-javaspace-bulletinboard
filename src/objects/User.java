package objects;

import net.jini.core.entry.*;

/**
 * The model representing a User object.
 * 
 * @author Waqas Musharaf
 */
public class User implements Entry {
	private static final long serialVersionUID = 3398539836371833490L;

	// Variables
	public String username;
	public String password;

	// No argument constructor
	public User() {
	}

	// Single argument constructor
	public User(String user) {
		this.username = user;
	}

	// Double argument constructor
	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
	}

	// Get methods
	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	// Set methods
	public void setUsername(String user) {
		this.username = user;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}
}
