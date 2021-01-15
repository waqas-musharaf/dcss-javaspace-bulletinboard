package objects;

import net.jini.core.entry.*;

/**
 * The model representing a Notification object.
 * 
 * @author Waqas Musharaf
 */
public class Notification implements Entry {
	private static final long serialVersionUID = 1908388755982172712L;

	// Variables
	public String notifiedUser;
	public String notification;
	public String dateTimeStr;

	// No argument constructor
	public Notification() {
	}

	// Single argument constructor
	public Notification(String user) {
		this.notifiedUser = user;
	}

	// Double argument constructor
	public Notification(String user, String notif) {
		this.notifiedUser = user;
		this.notification = notif;
	}

	// Triple argument constructor
	public Notification(String user, String notif, String dts) {
		this.notifiedUser = user;
		this.notification = notif;
		this.dateTimeStr = dts;
	}

	// Get methods
	public String getNotifiedUser() {
		return this.notifiedUser;
	}

	public String getNotification() {
		return this.notification;
	}

	public String getDateTimeStr() {
		return this.dateTimeStr;
	}

	// Set methods
	public void setNotifiedUser(String user) {
		this.notifiedUser = user;
	}

	public void setNotification(String notif) {
		this.notification = notif;
	}

	public void setDateTimeStr(String dts) {
		this.dateTimeStr = dts;
	}
}
