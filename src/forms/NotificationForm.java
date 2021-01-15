package forms;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import objects.CurrentUser;
import objects.Notification;
import utilities.SpaceUtils;

/**
 * A notification form which displays all new notifications for the current
 * user. Users can expand notifications, or return to the main menu. Any
 * notifications viewed will be removed from the JavaSpace after viewing the
 * notification form.
 * 
 * @author Waqas Musharaf
 *
 */
public class NotificationForm extends javax.swing.JFrame {
	private static final long serialVersionUID = 8971986397836768291L;

	// Variables declaration
	JavaSpace05 space;
	TransactionManager mgr;

	private javax.swing.JLabel lblCurrentUser;
	private javax.swing.JLabel lblList;
	private javax.swing.JLabel lblTitle;
	private javax.swing.JPanel pnlNotifPanel;
	private javax.swing.JScrollPane spNotifSP;
	private javax.swing.JTable tblNotifs;
	private javax.swing.JButton btnMenu;

	/**
	 * If the JavaSpace and TransactionManager are retrieved from SpaceUtils, calls
	 * the initComponents() and updateTblNotifs() methods.
	 */
	public NotificationForm() {
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
		updateTblNotifs();
	}

	/**
	 * Initialises GUI components.
	 */
	private void initComponents() {
		lblTitle = new javax.swing.JLabel();
		lblList = new javax.swing.JLabel();
		lblCurrentUser = new javax.swing.JLabel();
		pnlNotifPanel = new javax.swing.JPanel();
		spNotifSP = new javax.swing.JScrollPane();
		tblNotifs = new javax.swing.JTable() {
			private static final long serialVersionUID = 1L;

			// Sets cells of tblNotifs as uneditable.
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		btnMenu = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14));
		lblTitle.setText("JavaSpace Bulletin Board - Notifications");

		lblList.setFont(new java.awt.Font("Tahoma", 0, 14));
		lblList.setText(
				"Your Notifications: (Please note, notifications will automatically delete after being viewed.)");

		lblCurrentUser.setFont(new java.awt.Font("Tahoma", 1, 12));
		lblCurrentUser.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		// Updates lblCurrentUser to display the currently logged on user.
		lblCurrentUser.setText("Logged in as: " + CurrentUser.getCurrentUser());

		tblNotifs.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "Notification", "Date-Time Stamp" }));
		spNotifSP.setViewportView(tblNotifs);

		tblNotifs.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// If any point on tblNotifs is double-clicked, calls the
				// tblNotifsMouseDoubleClicked() method.
				if (e.getClickCount() == 2) {
					tblNotifsMouseDoubleClicked(e);
				}
			}
		});

		javax.swing.GroupLayout pnlNotifPanelLayout = new javax.swing.GroupLayout(pnlNotifPanel);
		pnlNotifPanel.setLayout(pnlNotifPanelLayout);
		pnlNotifPanelLayout.setHorizontalGroup(pnlNotifPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spNotifSP));
		pnlNotifPanelLayout
				.setVerticalGroup(pnlNotifPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlNotifPanelLayout.createSequentialGroup()
								.addComponent(spNotifSP, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
								.addContainerGap()));

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
						.addComponent(pnlNotifPanel, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup().addComponent(lblTitle).addGap(18, 18, 18).addComponent(
								lblCurrentUser, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup().addComponent(lblList)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnMenu)))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblTitle).addComponent(lblCurrentUser))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblList)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlNotifPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnMenu).addContainerGap()));

		pack();
	}

	/**
	 * Uses the JavaSpace05 space.contents() method to return a MatchSet of
	 * Notification objects that have the the current user as the notifUser.
	 * Iterates through the returned MatchSet using a transaction to add each
	 * Notification to an ArrayList of Notifications and take each Notification
	 * object returned from the space. After iterating through the entire MatchSet
	 * the transaction is commited.
	 * 
	 * @returns returnedNotifs
	 */
	public ArrayList<Notification> takeUserNotifsFromSpace() {
		ArrayList<Notification> returnedNotifs = new ArrayList<Notification>();
		// As space.contents() requires a collection of templates as an argument,
		// creates a collection of one template that matches any Notification object
		// which has the current user as the notifUser.
		LinkedList<Notification> templates = new LinkedList<Notification>();
		templates.add(new Notification(CurrentUser.getCurrentUser()));
		try {
			MatchSet matchSet = space.contents(templates, null, 1000, Long.MAX_VALUE);
			Notification currentNotif = (Notification) matchSet.next();
			// Creates a transaction.
			Transaction.Created trc = null;
			try {
				trc = TransactionFactory.create(mgr, 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Transaction txn = trc.transaction;
			while (currentNotif != null) {
				// Loops through all Notification objects in the MatchSet, adding each to an
				// ArrayList.
				returnedNotifs.add(currentNotif);
				try {
					// For each Notification object in the MatchSet, tries to take from the space
					// using the created transaction.
					space.takeIfExists(currentNotif, txn, 1000);
				} catch (Exception e) {
					// If a Notification object cannot be taken from the space, the transaction is
					// aborted.
					e.printStackTrace();
					txn.abort();
				}
				currentNotif = (Notification) matchSet.next();
			}
			// After looping through each Notification of the MatchSet, the transaction is
			// committed.
			txn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnedNotifs;
	}

	/**
	 * Creates a new and empty DefaultTableModel for tblNotifs. Uses the
	 * takeUserNotifsFromSpace() method to update the table model with
	 * Notifications.
	 */
	public void updateTblNotifs() {
		DefaultTableModel dtm = (DefaultTableModel) tblNotifs.getModel();
		// Clears the table model of existing data.
		dtm.setRowCount(0);
		// Retrieves the Notification list from takeUserNotifsFromSpace() method.
		ArrayList<Notification> notifs = takeUserNotifsFromSpace();
		// Creates a new object to store values for each table row.
		Object rowData[] = new Object[2];
		// Iterates through the Notification list. Sets the first cell of the row as the
		// notification text and the second cell of the row as the dateTimeStr.
		for (int i = 0; i < notifs.size(); i++) {
			rowData[0] = notifs.get(i).notification;
			rowData[1] = notifs.get(i).dateTimeStr;
			// Adds each row object to the table model.
			dtm.addRow(rowData);
		}
	}

	/**
	 * Upon double-click of a point on tblNotifs, retrieves notification text from
	 * selected row, inserts into an uneditable JTextField within a JScrollPane
	 * within a JOptionPane pop-up.
	 * 
	 * @param e
	 */
	private void tblNotifsMouseDoubleClicked(MouseEvent e) {
		// Sets the JTextArea to be populated by the contents of the 1st cell of the
		// selected row.
		JTextArea outputTA = new JTextArea(tblNotifs.getValueAt(tblNotifs.getSelectedRow(), 0).toString());
		// Formats JTextArea and inserts into JScrollPane.
		outputTA.setLineWrap(true);
		outputTA.setWrapStyleWord(true);
		outputTA.setEditable(false);
		JScrollPane outputSP = new JScrollPane(outputTA);
		// Sets preferred size of JScrollPane.
		outputSP.setPreferredSize(new Dimension(500, 100));
		// Creates a new message dialog, populated by the JScrollPane.
		JOptionPane.showMessageDialog(null, outputSP, "Notification Contents:", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Upon back to menu button press, displays new MainMenuForm().
	 * 
	 * @param evt
	 */
	private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {
		new MainMenuForm().setVisible(true);
		this.dispose();
	}
}