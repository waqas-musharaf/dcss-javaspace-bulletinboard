package objects;

import net.jini.core.entry.*;

/**
 * The model representing a Message object.
 * 
 * @author Waqas Musharaf
 */
public class Message implements Entry {
	private static final long serialVersionUID = -4801922609721046263L;

	// Variables
	public String messageOwner;
	public String messageText;
	public boolean privacyStatus;

	// No argument constructor
	public Message() {
	}

	// Triple argument constructor
	public Message(String owner, String text, boolean status) {
		this.messageOwner = owner;
		this.messageText = text;
		this.privacyStatus = status;
	}

	// Get methods
	public String getMessageOwner() {
		return this.messageOwner;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public boolean getPrivacyStatus() {
		return this.privacyStatus;
	}

	// Set methods
	public void setMessageOwner(String owner) {
		this.messageOwner = owner;
	}

	public void setMessageText(String text) {
		this.messageText = text;
	}

	public void setPrivacyStatus(boolean status) {
		this.privacyStatus = status;
	}
}
