package forms;

import javax.swing.JOptionPane;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace05;
import objects.CurrentUser;
import objects.User;
import utilities.SpaceUtils;

/**
 * A log in form which handles the registration of users (enters new User
 * objects into the JavaSpace) and handles logging in existing users (reads
 * existing User objects from the JavaSpace).
 * 
 * @author Waqas Musharaf
 *
 */
public class LogInForm extends javax.swing.JFrame {
	private static final long serialVersionUID = 1789324238945846356L;

	// Variables declaration
	JavaSpace05 space;

	private javax.swing.JLabel lblTitle;
	private javax.swing.JTextField tfUser;
	private javax.swing.JLabel lblWelcome;
	private javax.swing.JLabel lblUser;
	private javax.swing.JLabel lblPass;
	private javax.swing.JPasswordField pfPass;
	private javax.swing.JButton btnLogIn;
	private javax.swing.JButton btnRegister;

	private String username;
	private String password;

	/**
	 * Main method which attempts to retrieve the JavaSpace from SpaceUtils and
	 * displays a new LogInForm() if successful.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JavaSpace05 space = SpaceUtils.getSpace();
		if (space == null) {
			System.err.println("Failed to find the javaspace");
			System.exit(1);
		} else {
			System.out.println("Found the javaspace");
			new LogInForm().setVisible(true);
		}
	}

	/**
	 * If JavaSpace is retrieved from SpaceUtils, calls initComponents() method.
	 */
	public LogInForm() {
		space = SpaceUtils.getSpace();
		if (space == null) {
			System.err.println("Failed to find the javaspace");
			System.exit(1);
		}
		initComponents();
	}

	/**
	 * Initialises GUI components.
	 */
	private void initComponents() {
		// Variables declaration
		lblTitle = new javax.swing.JLabel();
		tfUser = new javax.swing.JTextField();
		lblWelcome = new javax.swing.JLabel();
		lblUser = new javax.swing.JLabel();
		lblPass = new javax.swing.JLabel();
		pfPass = new javax.swing.JPasswordField();
		btnLogIn = new javax.swing.JButton();
		btnRegister = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24));
		lblTitle.setText("JavaSpace Bulletin Board");

		lblWelcome.setFont(new java.awt.Font("Tahoma", 1, 14));
		lblWelcome.setText("Welcome! Please enter your log-in credentials");

		lblUser.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblUser.setText("Username:");

		lblPass.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblPass.setText("Password:");

		btnLogIn.setText("Log In");
		btnLogIn.addActionListener(new java.awt.event.ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				username = tfUser.getText();
				// If pfPass field is non-blank, uses the sha512Hex() method from DigestUtils
				// (sourced from org.apache.commons.codec.digest) to hash the text contents of
				// pfPass using SHA512.
				if (pfPass.getPassword().length != 0) {
					password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pfPass.getText());
				}
				btnLogInActionPerformed(evt);
			}
		});

		btnRegister.setText("Don't have an account? Register");
		btnRegister.addActionListener(new java.awt.event.ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				username = tfUser.getText();
				// As above.
				if (pfPass.getPassword().length != 0) {
					password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pfPass.getText());
				}
				btnRegisterActionPerformed(evt);
			}
		});

		// Configures component layout.
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(lblUser).addComponent(lblPass))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(layout.createSequentialGroup().addComponent(btnLogIn)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(btnRegister).addGap(0, 0, Short.MAX_VALUE))
										.addComponent(tfUser).addComponent(pfPass)))
						.addComponent(lblTitle).addComponent(lblWelcome))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblTitle)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblWelcome)
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblUser).addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblPass).addComponent(pfPass, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(btnLogIn).addComponent(btnRegister))
						.addContainerGap(27, Short.MAX_VALUE)));

		pack();
	}

	/**
	 * Clears tfUser and pfPass fields on the GUI and clears username and password
	 * variables.
	 */
	public void clearFields() {
		tfUser.setText("");
		pfPass.setText("");
		username = password = "";
	}

	/**
	 * Upon log in button press, attempts to log in user. Uses provided credentials
	 * to create a User template in order to query the JavaSpace for a matching User
	 * object. If matching User object exists, sets current user accordingly and
	 * displays new MainMenuForm().
	 *
	 * @param evt
	 */
	private void btnLogInActionPerformed(java.awt.event.ActionEvent evt) {
		// Performs validation to ensure username and password fields are non-null and
		// non-empty.
		if (username != null && !username.isEmpty()) {
			if (password != null && !password.isEmpty()) {
				User userLogin = new User(username, password);
				// Attempts to read User object with provided credentials from the space if it
				// exists.
				try {
					User userReturned = (User) space.readIfExists(userLogin, null, 1000);
					// If a null object is returned, displays an error message and clears input
					// fields.
					if (userReturned == null) {
						JOptionPane.showMessageDialog(null,
								"Username/password combination incorrect, please try again.", "Error",
								JOptionPane.ERROR_MESSAGE);
						clearFields();
						// If a non-null object is returned, sets current user as username
						// provided and displays new MainMenuForm().
					} else {
						CurrentUser.setCurrentUser(username);
						new MainMenuForm().setVisible(true);
						this.dispose();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please enter a password to log in.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				clearFields();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please enter a username to log in.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			clearFields();
		}
	}

	/**
	 * Upon register button press, attempts to register user. Uses provided username
	 * to create a User template in order to query the JavaSpace for a User object
	 * with the same username. If no matching User object exists, writes new User
	 * object to the JavaSpace using provided credentials.
	 * 
	 * @param evt
	 */
	private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
		// Performs validation to ensure username and password fields are non-null and
		// non-empty.
		if (username != null && !username.isEmpty()) {
			if (password != null && !password.isEmpty()) {
				// Performs validation to ensure username entered is between 4-16 chars.
				if (username.length() >= 4 && username.length() <= 16) {
					User userCheck = new User(username);
					User userRegister = new User(username, password);
					// Attempts to read User object with provided username from the space if it
					// exists.
					try {
						User userReturned = (User) space.readIfExists(userCheck, null, 1000);
						// If a null object is returned, writes new User object to the space using
						// credentials provided.
						if (userReturned == null) {
							try {
								space.write(userRegister, null, Lease.FOREVER);
								JOptionPane.showMessageDialog(null, "User: '" + username + "' successfully registered.",
										"Information", JOptionPane.INFORMATION_MESSAGE);
								clearFields();
							} catch (Exception e) {
								e.printStackTrace();
							}
							// If a non-null object is returned, displays an error message and clears input
							// fields.
						} else {
							JOptionPane.showMessageDialog(null,
									"Cannot register user: '" + username
											+ "' as it already exists, please try another username.",
									"Error", JOptionPane.ERROR_MESSAGE);
							clearFields();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Username must be between 4-16 characters long.", "Error",
							JOptionPane.ERROR_MESSAGE);
					clearFields();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please enter a password to register.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				clearFields();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please enter a username to register.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			clearFields();
		}
	}
}
