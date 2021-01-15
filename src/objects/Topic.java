package objects;

import java.util.ArrayList;
import java.util.LinkedList;
import net.jini.core.entry.*;
import objects.Message;

/**
 * The model representing a Topic object.
 * 
 * @author Waqas Musharaf
 */
public class Topic implements Entry {
	private static final long serialVersionUID = 5049408155033481264L;

	// Variables
	public String topicOwner;
	public String topicTitle;
	public LinkedList<Message> topicMessages;
	public ArrayList<String> topicNotifUsers;

	// No argument constructor
	public Topic() {
	}

	// Single argument constructor
	public Topic(String title) {
		this.topicTitle = title;
	}

	// Double argument constructor
	public Topic(String owner, String title) {
		this.topicOwner = owner;
		this.topicTitle = title;
	}

	// Quadruple argument constructor
	public Topic(String owner, String title, LinkedList<Message> messages, ArrayList<String> notifUsers) {
		this.topicOwner = owner;
		this.topicTitle = title;
		this.topicMessages = messages;
		this.topicNotifUsers = notifUsers;
	}

	// Get methods
	public String getTopicOwner() {
		return this.topicOwner;
	}

	public String getTopicTitle() {
		return this.topicTitle;
	}

	public LinkedList<Message> getTopicMessages() {
		return this.topicMessages;
	}

	public ArrayList<String> getTopicNotifUsers() {
		return this.topicNotifUsers;
	}

	// Set methods
	public void setTopicOwner(String owner) {
		this.topicOwner = owner;
	}

	public void setTopicTitle(String title) {
		this.topicTitle = title;
	}

	public void addMessage(Message message) {
		this.topicMessages.add(message);
	}

	public void addNotifUsers(String user) {
		this.topicNotifUsers.add(user);
	}
}
