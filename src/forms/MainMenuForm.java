package forms;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import objects.CurrentTopic;
import objects.CurrentUser;
import objects.Message;
import objects.Notification;
import objects.Topic;
import utilities.SpaceUtils;

/**
 * A main menu form which displays all current Topic objects on the JavaSpace.
 * The user can create a new topic, check their notifications, expand an
 * existing topic or log out.
 * 
 * @author Waqas Musharaf
 *
 */
public class MainMenuForm extends javax.swing.JFrame {
	private static final long serialVersionUID = -6075456560841556549L;

	// Variables declaration
	JavaSpace05 space;

	private javax.swing.JLabel lblTitle;
	private javax.swing.JLabel lblList;
	private javax.swing.JLabel lblCurrentUser;
	private javax.swing.JPanel pnlTopicPanel;
	private javax.swing.JScrollPane spTopicSP;
	private javax.swing.JTable tblTopics;
	private javax.swing.JButton btnNewTopic;
	private javax.swing.JButton btnNotifs;
	private javax.swing.JButton btnLogOut;

	/**
	 * If JavaSpace is retrieved from SpaceUtils, calls the initComponents() and
	 * updateTblTopics() methods as well as executing checkUserNotifsRunnable every
	 * 500ms.
	 */
	public MainMenuForm() {
		space = SpaceUtils.getSpace();
		if (space == null) {
			System.err.println("Failed to find the javaspace");
			System.exit(1);
		}
		initComponents();
		updateTblTopics();
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(checkUserNotifsRunnable, 0, 500, TimeUnit.MILLISECONDS);
	}

	/**
	 * Initialises GUI components.
	 */
	public void initComponents() {
		lblTitle = new javax.swing.JLabel();
		lblList = new javax.swing.JLabel();
		lblCurrentUser = new javax.swing.JLabel();
		pnlTopicPanel = new javax.swing.JPanel();
		spTopicSP = new javax.swing.JScrollPane();
		tblTopics = new javax.swing.JTable() {
			private static final long serialVersionUID = 1L;

			// Sets cells of tblTopics as uneditable.
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		btnNewTopic = new javax.swing.JButton();
		btnNotifs = new javax.swing.JButton();
		btnLogOut = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14));
		lblTitle.setText("JavaSpace Bulletin Board - Main Menu");

		lblList.setFont(new java.awt.Font("Tahoma", 0, 14));
		lblList.setText("List of Current Topics: (double-click topic to expand)");

		lblCurrentUser.setFont(new java.awt.Font("Tahoma", 1, 12));
		lblCurrentUser.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		// Updates lblCurrentUser to display the currently logged on user.
		lblCurrentUser.setText("Logged in as: " + CurrentUser.getCurrentUser());

		tblTopics.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "Title", "Owner", "Number of Public Messages" }));
		spTopicSP.setViewportView(tblTopics);

		tblTopics.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// If any point on tblTopics is double-clicked, calls the
				// tblTopicsMouseDoubleClicked() method.
				if (e.getClickCount() == 2) {
					tblTopicsMouseDoubleClicked(e);
				}
			}
		});

		javax.swing.GroupLayout pnlTopicPanelLayout = new javax.swing.GroupLayout(pnlTopicPanel);
		pnlTopicPanel.setLayout(pnlTopicPanelLayout);
		pnlTopicPanelLayout.setHorizontalGroup(pnlTopicPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spTopicSP));
		pnlTopicPanelLayout
				.setVerticalGroup(pnlTopicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(spTopicSP, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE));

		btnNewTopic.setText("Create New Topic");
		btnNewTopic.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNewTopicActionPerformed(evt);
			}
		});

		btnNotifs.setText("Notifications (" + "0" + ")");
		btnNotifs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNotifsActionPerformed(evt);
			}
		});

		btnLogOut.setText("Log Out");
		btnLogOut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnLogOutActionPerformed(evt);
			}
		});

		// Configures component layout.
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addComponent(pnlTopicPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup().addComponent(btnNotifs)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnLogOut))
						.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(lblList).addComponent(btnNewTopic))
								.addGap(0, 0, Short.MAX_VALUE))
						.addGroup(javax.swing.GroupLayout.Alignment.LEADING,
								layout.createSequentialGroup().addComponent(lblTitle).addGap(18, 18, 18).addComponent(
										lblCurrentUser, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblTitle).addComponent(lblCurrentUser))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblList)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlTopicPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewTopic).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnLogOut).addComponent(btnNotifs))
						.addContainerGap()));

		pack();
	}

	/**
	 * Uses the JavaSpace05 space.contents() method to return a MatchSet of Topic
	 * objects on the space and iterates through the MatchSet to add each Topic
	 * object to an ArrayList of Topics.
	 * 
	 * @returns returnedTopics
	 */
	public ArrayList<Topic> getTopicsFromSpace() {
		ArrayList<Topic> returnedTopics = new ArrayList<Topic>();
		// As space.contents() requires a collection of templates as an argument,
		// creates a collection of one template which matches any Topic object.
		LinkedList<Topic> templates = new LinkedList<Topic>();
		templates.add(new Topic());
		try {
			MatchSet matchSet = space.contents(templates, null, 1000, Long.MAX_VALUE);
			Topic currentTopic = (Topic) matchSet.next();
			// Iterates through each Topic object within the returned MatchSet, adding each
			// Topic object to the ArrayList returnedTopics.
			while (currentTopic != null) {
				returnedTopics.add(currentTopic);
				currentTopic = (Topic) matchSet.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnedTopics;
	}

	/**
	 * Creates a new and empty DefaultTableModel for tblTopics. Uses the
	 * getTopicsFromSpace() method to update the table model with topics.
	 */
	public void updateTblTopics() {
		DefaultTableModel dtm = (DefaultTableModel) tblTopics.getModel();
		// Clears the table model of existing data.
		dtm.setRowCount(0);
		// Retrieves topic list from getTopicsFromSpace() method.
		ArrayList<Topic> topics = getTopicsFromSpace();
		// Creates a new object to store values for each table row.
		Object rowData[] = new Object[3];
		// Iterates backwards through the topic list to display the most recent topic at
		// the top of the table. Sets the first cell of the row as the topic title, the
		// second cell of the row as the topic owner and the third cell of the row as
		// the number of /public/ messages on the topic.
		for (int i = topics.size() - 1; i >= 0; i--) {
			rowData[0] = topics.get(i).topicTitle;
			rowData[1] = topics.get(i).topicOwner;
			if (topics.get(i).topicMessages != null) {
				int pubMsgs = 0;
				for (Message m : topics.get(i).topicMessages) {
					if (m.privacyStatus == false) {
						pubMsgs++;
					}
				}
				rowData[2] = pubMsgs;
			}
			// Adds each row object to the table model.
			dtm.addRow(rowData);
		}
	}

	/**
	 * Runnable which uses the JavaSpace05 space.contents() method to return a
	 * MatchSet of Notification objects which have the field notifiedUser as the
	 * current user. Counts the number of Notification objects within the returned
	 * MatchSet in order to update btnNotifs with the number of notifications for
	 * the current user.
	 */
	Runnable checkUserNotifsRunnable = new Runnable() {
		public void run() {
			// As space.contents() requires a collection of templates as an argument,
			// creates a collection of one template which matches any Notification object
			// with field notifiedUser as the current user.
			LinkedList<Notification> templates = new LinkedList<Notification>();
			templates.add(new Notification(CurrentUser.getCurrentUser()));
			int userNotifsCount = 0;
			try {
				MatchSet matchSet = space.contents(templates, null, 1000, Long.MAX_VALUE);
				Notification currentNotif = (Notification) matchSet.next();
				// Increments userNotifsCount by one for each Notification object within the
				// returned MatchSet.
				while (currentNotif != null) {
					userNotifsCount++;
					currentNotif = (Notification) matchSet.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Updates the text of btnNotifs to display the number of Notification objects
			// on the space that have field notifiedUser as the current user.
			btnNotifs.setText("Notifications (" + userNotifsCount + ")");
		}
	};

	/**
	 * Upon double-click of a point on tblTopics, retrieves values in selected row,
	 * updates the current topic accordingly and then displays new TopicForm().
	 * 
	 * @param e
	 */
	private void tblTopicsMouseDoubleClicked(MouseEvent e) {
		// Sets current topic owner as the value in the second cell of the selected row.
		CurrentTopic.setCTOwner(tblTopics.getValueAt(tblTopics.getSelectedRow(), 1).toString());
		// Sets current topic title as the value in the first cell of the selected row.
		CurrentTopic.setCTTitle(tblTopics.getValueAt(tblTopics.getSelectedRow(), 0).toString());
		new TopicForm().setVisible(true);
		this.dispose();
	}

	/**
	 * Upon notifications button press, displays new NotificationForm().
	 * 
	 * @param evt
	 */
	private void btnNotifsActionPerformed(java.awt.event.ActionEvent evt) {
		new NotificationForm().setVisible(true);
		this.dispose();
	}

	/**
	 * Upon create new topic button press, attempts to create a new topic using user
	 * input. Uses provided input to create a new Topic template in order to query
	 * the JavaSpace for a Topic object with the same title. If no matching Topic
	 * object exists, writes new Topic object to the JavaSpace using provided input
	 * as topic title.
	 * 
	 * @param evt
	 */
	private void btnNewTopicActionPerformed(java.awt.event.ActionEvent evt) {
		String input = JOptionPane.showInputDialog(null, "Please enter a topic title:", "Create New Topic",
				JOptionPane.PLAIN_MESSAGE);
		// Performs validation to ensure input is non-null.
		if (input != null) {
			// Performs valiation to ensure input is between 2-64 chars.
			if (input.length() >= 2 && input.length() <= 64) {
				Topic topicCheck = new Topic(input);
				Topic topicInput = new Topic(CurrentUser.getCurrentUser(), input, new LinkedList<Message>(),
						new ArrayList<String>());
				// Attempts to read Topic object with provided title from the space if it
				// exists.
				try {
					Topic topicReturned = (Topic) space.readIfExists(topicCheck, null, 1000);
					// If a null object is returned, writes new Topic object to the space using
					// title input provided.
					if (topicReturned == null) {
						try {
							space.write(topicInput, null, Lease.FOREVER);
							updateTblTopics();
							JOptionPane.showMessageDialog(null, "Topic: '" + input
									+ "' sucessfully created. Please note that public messages are in black and private messages are in grey.",
									"Topic Created!", JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// If a non-null object is returned, displays an error message.
					} else {
						JOptionPane.showMessageDialog(null,
								"Cannot create topic: '" + input
										+ "' as it already exists, please try another topic title.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Topic title must be between 2-64 characters long.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Upon log out button press, sets current user to null and displays new
	 * LogInForm().
	 * 
	 * @param evt
	 */
	private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {
		CurrentUser.setCurrentUser(null);
		new LogInForm().setVisible(true);
		this.dispose();
	}
}