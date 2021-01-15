package forms;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import objects.CurrentTopic;
import objects.CurrentUser;
import objects.Message;
import objects.Notification;
import objects.Topic;
import utilities.SpaceUtils;

/**
 * A topic form which displays all messages for the current topic. Users can
 * send a public message, toggle notifications for the current topic, expand a
 * message or return to the main menu. The topic owner can delete the topic.
 * Non-topic-owners can send private messages.
 * 
 * @author Waqas Musharaf
 *
 */
public class TopicForm extends javax.swing.JFrame {
	private static final long serialVersionUID = -2164972111884308519L;

	// Variables declaration
	JavaSpace05 space;
	TransactionManager mgr;

	private javax.swing.JLabel lblCurrentUser;
	private javax.swing.JLabel lblTitle;
	private javax.swing.JLabel lblTopicInfo;
	private javax.swing.JPanel pnlMessagePanel;
	private javax.swing.JScrollPane spMessageSP;
	private javax.swing.JTable tblMessages;
	private javax.swing.JButton btnDelTopic;
	private javax.swing.JButton btnMenu;
	private javax.swing.JButton btnPrivateMsg;
	private javax.swing.JButton btnPublicMsg;
	private javax.swing.JButton btnTogNotifs;

	/**
	 * If the JavaSpace and TransactionManager are retrieved from SpaceUtils, calls
	 * the initComponents() and updateTblMessages() methods.
	 */
	public TopicForm() {
		space = SpaceUtils.getSpace();
		if (space == null) {
			System.err.println("Failed to find the javaspace");
			System.exit(1);
		}
		mgr = SpaceUtils.getManager();
		if (mgr == null) {
			System.err.println("Failed to find the transaction manager");
			System.exit(1);
		}
		initComponents();
		updateTblMessages();
	}

	/**
	 * Initialises GUI components.
	 */
	private void initComponents() {
		lblTitle = new javax.swing.JLabel();
		lblTopicInfo = new javax.swing.JLabel();
		lblCurrentUser = new javax.swing.JLabel();
		pnlMessagePanel = new javax.swing.JPanel();
		spMessageSP = new javax.swing.JScrollPane();
		tblMessages = new javax.swing.JTable() {
			private static final long serialVersionUID = 1L;

			// Sets cells of tblMessages as uneditable.
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		btnPublicMsg = new javax.swing.JButton();
		btnTogNotifs = new javax.swing.JButton();
		btnPrivateMsg = new javax.swing.JButton();
		btnDelTopic = new javax.swing.JButton();
		btnMenu = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14));
		lblTitle.setText("JavaSpace Bulletin Board - Topic");

		lblTopicInfo.setFont(new java.awt.Font("Tahoma", 0, 14));
		// Updates lblTopicInfo to display the current topic information.
		lblTopicInfo.setText("<html>Topic Title: <b>" + CurrentTopic.getCTTitle() + "</b> | Topic Owner: <b>"
				+ CurrentTopic.getCTOwner() + "</b></html>");

		lblCurrentUser.setFont(new java.awt.Font("Tahoma", 1, 12));
		lblCurrentUser.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		// Updates lblCurrentUser to display the currently logged on user.
		lblCurrentUser.setText("Logged in as: " + CurrentUser.getCurrentUser());

		tblMessages.setModel(
				new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Message", "Sender" }));
		spMessageSP.setViewportView(tblMessages);

		tblMessages.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// If any point on tblMessages is double-clicked, calls the
				// tblMessagesMouseDoubleClicked() method.
				if (e.getClickCount() == 2) {
					tblMessagesMouseDoubleClicked(e);
				}
			}
		});

		javax.swing.GroupLayout pnlMessagePanelLayout = new javax.swing.GroupLayout(pnlMessagePanel);
		pnlMessagePanel.setLayout(pnlMessagePanelLayout);
		pnlMessagePanelLayout
				.setHorizontalGroup(pnlMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(spMessageSP, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE));
		pnlMessagePanelLayout
				.setVerticalGroup(pnlMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(spMessageSP, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE));

		btnPublicMsg.setText("Send Public Message");
		btnPublicMsg.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPublicMsgActionPerformed(evt);
			}
		});

		btnTogNotifs.setText("Toggle Notifications");
		btnTogNotifs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnTogNotifsActionPerformed(evt);
			}
		});

		btnPrivateMsg.setText("Send Private Message");
		btnPrivateMsg.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrivateMsgActionPerformed(evt);
			}
		});

		btnDelTopic.setText("Delete Topic");
		btnDelTopic.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDelTopicActionPerformed(evt);
			}
		});

		btnMenu.setText("Back to Menu");
		btnMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnMenuActionPerformed(evt);
			}
		});

		// Configures component layout.
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(pnlMessagePanel, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup().addComponent(lblTitle).addGap(18, 18, 18).addComponent(
								lblCurrentUser, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(lblTopicInfo)
										.addGroup(layout.createSequentialGroup().addGroup(layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
												.addComponent(btnTogNotifs, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnPublicMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 159,
														Short.MAX_VALUE))
												.addGap(18, 18, 18)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(btnDelTopic, javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(btnPrivateMsg,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnMenu)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblTitle).addComponent(lblCurrentUser))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblTopicInfo)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlMessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnPublicMsg).addComponent(btnPrivateMsg))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnMenu).addComponent(btnTogNotifs).addComponent(btnDelTopic))
						.addContainerGap()));

		// If current user is current topic owner, sets btnDelTopic as visible,
		// otherwise sets btnPrivateMsg as visible.
		if (!CurrentTopic.getCTOwner().equals(CurrentUser.getCurrentUser())) {
			btnDelTopic.setVisible(false);
		} else {
			btnPrivateMsg.setVisible(false);
		}

		pack();
	}

	/**
	 * Uses the JavaSpace05 space.contents() method to return a MatchSet of Topic
	 * objects that have the same topic owner and topic title as the current topic.
	 * Iterates through the returned MatchSet to add all messages of the Topics
	 * within the MatchSet (1) to an ArrayList of messages.
	 * 
	 * @returns returnedMsgs
	 */
	public ArrayList<Message> getTopicMsgsFromSpace() {
		ArrayList<Message> returnedMsgs = new ArrayList<Message>();
		// As space.contents() requires a collection of templates as an argument,
		// creates a collection of one template which matches any Topic object (1) which
		// has the same owner and title as the current topic.
		LinkedList<Topic> templates = new LinkedList<Topic>();
		templates.add(new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle()));
		try {
			MatchSet matchSet = space.contents(templates, null, 1000, Long.MAX_VALUE);
			Topic currentTopic = (Topic) matchSet.next();
			while (currentTopic != null) {
				// Loops through all messages of the current topic, adding each message to the
				// ArrayList returnedMsgs.
				for (Message m : currentTopic.topicMessages) {
					returnedMsgs.add(m);
				}
				currentTopic = (Topic) matchSet.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnedMsgs;
	}

	/**
	 * Creates a new and empty DefaultTableModel for tblMessages. Uses the
	 * getTopicMsgsFromSpace() method to update the table model with messages.
	 */
	public void updateTblMessages() {
		DefaultTableModel dtm = (DefaultTableModel) tblMessages.getModel();
		// Clears the table model of existing data.
		dtm.setRowCount(0);
		// Retrieves message list from getTopicMsgsFromSpace() method.
		ArrayList<Message> msgs = getTopicMsgsFromSpace();
		// Creates a new object to store values for each table row.
		Object rowData[] = new Object[2];
		// Iterates through the message list. Sets the first cell of the row as the
		// message text and the second cell of the row as the message owner.
		for (int i = 0; i < msgs.size(); i++) {
			// If the message is private, formats the message contents using HTML to help
			// the user identify the message as private.
			if (msgs.get(i).privacyStatus == true) {
				rowData[0] = "<html><font color=\"#808080\">" + msgs.get(i).messageText + "</font></html>";
				rowData[1] = "<html><font color=\"#808080\">" + msgs.get(i).messageOwner
						+ " <i>[Private Message]</i></font></html>";
				// If the message is private, and the message owner or the current topic owner
				// is the current user, then adds the message to the table model, otherwise,
				// does not add the message to the table model.
				if (msgs.get(i).messageOwner.equals(CurrentUser.getCurrentUser())
						|| CurrentTopic.getCTOwner().equals(CurrentUser.getCurrentUser())) {
					dtm.addRow(rowData);
				}
				// If the message is not private, adds the message to the table model without
				// any HTML formatting or current user validation.
			} else if (msgs.get(i).privacyStatus == false) {
				rowData[0] = msgs.get(i).messageText;
				rowData[1] = msgs.get(i).messageOwner;
				dtm.addRow(rowData);
			}
		}
	}

	/**
	 * Creates a new Notification object on the JavaSpace for each notifiedUser on
	 * the current topic.
	 * 
	 * @param action: The type of notification object to create
	 * @param txn: The transaction
	 */
	public void notifyUsers(String action, Transaction txn) {
		// Creates a Topic template for the current topic.
		Topic ctTemplate = new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle());
		try {
			// Attempts to read Topic object that adheres to the Topic template from the
			// space if it exists.
			Topic ctReturned = (Topic) space.readIfExists(ctTemplate, txn, 1000);
			if (ctReturned != null) {
				String notifText = null;
				// Sets the notifText variable according to the inputted action string.
				if (action.equals("public-message")) {
					notifText = "User '" + CurrentUser.getCurrentUser() + "' sent a public message to the topic '"
							+ CurrentTopic.getCTTitle() + "'.";
				} else if (action.equals("delete-topic")) {
					notifText = "User '" + CurrentUser.getCurrentUser() + "' deleted the topic '"
							+ CurrentTopic.getCTTitle() + "'.";
				} else {
					JOptionPane.showMessageDialog(null, "Critical Error: Invalid notification action detected.",
							"Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				// For each user in the topicNotifUsers list of the current topic, attempts to
				// create a new Notification object on the space using the notifText variable,
				// the current user and the current date/time.
				for (String notifUser : ctReturned.topicNotifUsers) {
					Notification notif = new Notification(notifUser, notifText,
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
					try {
						space.write(notif, txn, Lease.FOREVER);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Critical Error: Cannot find current topic on space.", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Upon double-click of a point on tblMessages, retreives message text from
	 * selected row, inserts into an uneditable JTextField within a JScrollPane
	 * within a JOptionPane pop-up.
	 * 
	 * @param e
	 */
	private void tblMessagesMouseDoubleClicked(MouseEvent e) {
		// Sets the JTextArea to be populated by the contents of the 1st cell of the
		// selected row.
		JTextArea outputTA = new JTextArea(tblMessages.getValueAt(tblMessages.getSelectedRow(), 0).toString());
		// Formats JTextArea and inserts into JScrollPane.
		outputTA.setLineWrap(true);
		outputTA.setWrapStyleWord(true);
		outputTA.setEditable(false);
		JScrollPane outputSP = new JScrollPane(outputTA);
		// Sets preferred size of JScrollPane.
		outputSP.setPreferredSize(new Dimension(500, 200));
		// Creates a new message dialog, populated by the JScrollPane.
		JOptionPane.showMessageDialog(null, outputSP, "Message Contents:", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Upon public message button press, creates a new JOptionPane pop-up which asks
	 * user for message input. Creates a new Topic template for the current topic
	 * and uses the template to query the JavaSpace for a Topic object. Uses a
	 * transaction to take the matched Topic object, if it exists, then create a new
	 * Topic template with the message input added as a public topic message before
	 * writing the new template to the JavaSpace and commiting the transaction.
	 * 
	 * @param evt
	 */
	private void btnPublicMsgActionPerformed(java.awt.event.ActionEvent evt) {
		// Creates and formats a new JTextArea.
		JTextArea input = new JTextArea(10, 40);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		// Creates a Topic template for the current topic.
		Topic ctTemplate = new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle());
		// Creates a JOptionPane pop-up prompting the user for message input.
		switch (JOptionPane.showConfirmDialog(null, new JScrollPane(input), "Submit Public Message",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
		// If the OK button is pressed:
		case JOptionPane.OK_OPTION:
			// Performs message validation.
			if (input.getText() != null) {
				if (input.getText().length() >= 1 && input.getText().length() <= 1024) {
					// Creates a new Message template with the current user, message input and
					// public privacy.
					Message message = new Message(CurrentUser.getCurrentUser(), input.getText(), false);
					// Creates a new transaction using the TransactionManager.
					Transaction.Created trc = null;
					try {
						trc = TransactionFactory.create(mgr, 3000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Transaction txn = trc.transaction;
					// Uses the Topic template to attempt to take a matching Topic object from the
					// space.
					try {
						Topic ctReturned = (Topic) space.takeIfExists(ctTemplate, txn, 1000);
						if (ctReturned != null) {
							Topic ctUpdate = ctReturned;
							// If a Topic object is returned, creates a new Topic template with the addition
							// of the Message template and attempts to write it to the space.
							ctUpdate.addMessage(message);
							try {
								space.write(ctUpdate, txn, Lease.FOREVER);
								// Commits the transaction.
								txn.commit();
								// Calls the notifyUsers() method with an action param of 'public-message' and a
								// null transaction param.
								notifyUsers("public-message", null);
								// Calls the updatesTblMessages() method.
								updateTblMessages();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Critical Error: Cannot find current topic on space.",
									"Error", JOptionPane.ERROR_MESSAGE);
							// If the Topic object cannot be retrieved from the space, aborts the
							// transaction and closes the program.
							txn.abort();
							System.exit(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				} else {
					// If message input fails validation, displays error message JOptionPane pop-up.
					JOptionPane.showMessageDialog(null, "Message must be between 1-1024 characters long.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Upon toggle notifications button press, creates new JOptionPane pop-up
	 * informing the user of their current notifications status for the current
	 * topic. Asks user if they would like to receive notifications and either adds
	 * the current user to the current topic's notifUsers list or removes the
	 * current user from the list, depending on user input.
	 * 
	 * @param evt
	 */
	private void btnTogNotifsActionPerformed(java.awt.event.ActionEvent evt) {
		// Creates a Topic template for the current topic.
		Topic ctTemplate = new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle());
		try {
			// Uses the Topic template to attempt to read a matching Topic object from the
			// space.
			Topic ctReturned = (Topic) space.readIfExists(ctTemplate, null, 1000);
			String JOP_Message;
			// If a Topic object is returned, checks the topic's topicNotifUsers list for
			// the current user and generates a message accordingly.
			if (ctReturned != null) {
				if (ctReturned.topicNotifUsers.contains(CurrentUser.getCurrentUser())) {
					JOP_Message = "You are currently receiving notifications for this topic. Would you like to continue receiving notifcations?";
				} else {
					JOP_Message = "You are not currently receiving notifications for this topic. Would you like to receive notifcations?";
				}
				// Displays a confirmation message asking the user to input whether or not they
				// would like to receive notifications for the current topic.
				switch (JOptionPane.showConfirmDialog(null, JOP_Message, "Receive Notifications?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
				case JOptionPane.YES_OPTION:
					// If the user selects yes, and they don't already receive notifications:
					if (!ctReturned.topicNotifUsers.contains(CurrentUser.getCurrentUser())) {
						// Creates a new transaction using the TransactionManager.
						Transaction.Created trc = null;
						try {
							trc = TransactionFactory.create(mgr, 3000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Transaction txn = trc.transaction;
						// Uses the Topic template to attempt to take a matching Topic object from the
						// space.
						try {
							ctReturned = (Topic) space.takeIfExists(ctTemplate, txn, 1000);
							if (ctReturned != null) {
								Topic ctUpdate = ctReturned;
								// If a Topic object is returned, creates a new Topic template with the addition
								// of the current user in the topic's notifUsers list and attempts to write it
								// to the space.
								ctUpdate.addNotifUsers(CurrentUser.getCurrentUser());
								try {
									space.write(ctUpdate, txn, Lease.FOREVER);
									// Commits the transaction.
									txn.commit();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"Critical Error: Cannot find current topic on space.", "Error",
										JOptionPane.ERROR_MESSAGE);
								// If the Topic object cannot be retrieved from the space, aborts the
								// transaction and closes the program.
								txn.abort();
								System.exit(1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case JOptionPane.NO_OPTION:
					// If the user selects no, and they currently receive notifications:
					if (ctReturned.topicNotifUsers.contains(CurrentUser.getCurrentUser())) {
						// Creates a new transaction using the TransactionManager.
						Transaction.Created trc = null;
						try {
							trc = TransactionFactory.create(mgr, 3000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Transaction txn = trc.transaction;
						// Uses the Topic template to attempt to take a matching Topic object from the
						// space.
						try {
							ctReturned = (Topic) space.takeIfExists(ctTemplate, txn, 1000);
							if (ctReturned != null) {
								Topic ctUpdate = ctReturned;
								// If a Topic object is returned, creates a new Topic template with the removal
								// of the current user from the topic's notifUsers list and attempts to write it
								// to the space.
								ctUpdate.topicNotifUsers.remove(CurrentUser.getCurrentUser());
								try {
									space.write(ctUpdate, txn, Lease.FOREVER);
									// Commits the transaction.
									txn.commit();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"Critical Error: Cannot find current topic on space.", "Error",
										JOptionPane.ERROR_MESSAGE);
								// If the Topic object cannot be retrieved from the space, aborts the
								// transaction and closes the program.
								txn.abort();
								System.exit(1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Critical Error: Cannot find current topic on space.", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Upon private message button press, creates a new JOptionPane pop-up which
	 * asks user for message input. Creates a new Topic template for the current
	 * topic and uses the template to query the JavaSpace for a Topic object. Uses a
	 * transaction to take the matched Topic object, if it exists, then create a new
	 * Topic template with the message input added as a private topic message before
	 * writing the new template to the JavaSpace and commiting the transaction.
	 * 
	 * @param evt
	 */
	private void btnPrivateMsgActionPerformed(java.awt.event.ActionEvent evt) {
		JOptionPane.showMessageDialog(null,
				"Any private messages are displayed on the topic in grey text. Private messages can only be read by you and the topic owner.",
				"Information", JOptionPane.INFORMATION_MESSAGE);
		// Creates and formats a new JTextArea.
		JTextArea input = new JTextArea(10, 40);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		// Creates a Topic template for the current topic.
		Topic ctTemplate = new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle());
		// Creates a JOptionPane pop-up prompting the user for message input.
		switch (JOptionPane.showConfirmDialog(null, new JScrollPane(input), "Submit Private Message",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
		// If the OK button is pressed:
		case JOptionPane.OK_OPTION:
			// Performs message validation.
			if (input.getText() != null) {
				if (input.getText().length() >= 1 && input.getText().length() <= 1024) {
					// Creates a new Message template with the current user, message input and
					// private privacy.
					Message message = new Message(CurrentUser.getCurrentUser(), input.getText(), true);
					// Creates a new transaction using the TransactionManager.
					Transaction.Created trc = null;
					try {
						trc = TransactionFactory.create(mgr, 3000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Transaction txn = trc.transaction;
					// Uses the Topic template to attempt to take a matching Topic object from the
					// space.
					try {
						Topic ctReturned = (Topic) space.takeIfExists(ctTemplate, txn, 1000);
						if (ctReturned != null) {
							Topic ctUpdate = ctReturned;
							// If a Topic object is returned, creates a new Topic template with the addition
							// of the Message template and attempts to write it to the space.
							ctUpdate.addMessage(message);
							try {
								space.write(ctUpdate, txn, Lease.FOREVER);
								// Commits the transaction.
								txn.commit();
								updateTblMessages();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Critical Error: Cannot find current topic on space.",
									"Error", JOptionPane.ERROR_MESSAGE);
							// If the Topic object cannot be retrieved from the space, aborts the
							// transaction and closes the program.
							txn.abort();
							System.exit(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				} else {
					// If message input fails validation, displays error message JOptionPane pop-up.
					JOptionPane.showMessageDialog(null, "Message must be between 1-1024 characters long.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Upon delete topic button press, creates a new JOptionPane pop-up which asks
	 * user to confirm that they would like to delete the current topic. If "OK" is
	 * selected, a new Topic template for the current topic is created and is used
	 * to query the space for a Topic object. If a Topic object is returned, a
	 * transaction is used to call the notifyUsers() method and take the topic from
	 * the space before committing the transaction. If the transaction is commited,
	 * sets current topic fields to null and calls new MainMenuForm().
	 * 
	 * @param evt
	 */
	private void btnDelTopicActionPerformed(java.awt.event.ActionEvent evt) {
		switch (JOptionPane.showConfirmDialog(null,
				"Are you sure you would like to delete this topic and all of its messages?", "Delete Topic",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
		case JOptionPane.OK_OPTION:
			// Creates a Topic template for the current topic.
			Topic ctTemplate = new Topic(CurrentTopic.getCTOwner(), CurrentTopic.getCTTitle());
			// Creates a new transaction using the TransactionManager.
			Transaction.Created trc = null;
			try {
				trc = TransactionFactory.create(mgr, 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Transaction txn = trc.transaction;
			try {
				// Calls the notifyUsers() method with an action param of 'delete-topic' and a
				// transaction param of txn.
				notifyUsers("delete-topic", txn);
				// Uses the Topic template to attempt to take a matching Topic object from the
				// space.
				Topic ctReturned = (Topic) space.takeIfExists(ctTemplate, txn, 1000);
				if (ctReturned != null) {
					JOptionPane.showMessageDialog(null, "Topic successfully deleted.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
					// Commits the transaction.
					txn.commit();
					// Clears current topic fields.
					CurrentTopic.setCTOwner(null);
					CurrentTopic.setCTTitle(null);
					new MainMenuForm().setVisible(true);
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Critical Error: Cannot find current topic on space.", "Error",
							JOptionPane.ERROR_MESSAGE);
					// If the Topic object cannot be retrieved from the space, aborts the
					// transaction and closes the program.
					txn.abort();
					System.exit(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * Upon back to menu button press, sets current topic fields to null and
	 * displays new MainMenuForm().
	 * 
	 * @param evt
	 */
	private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {
		CurrentTopic.setCTOwner(null);
		CurrentTopic.setCTTitle(null);
		new MainMenuForm().setVisible(true);
		this.dispose();
	}
}
