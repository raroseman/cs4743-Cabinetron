package AccessControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import assignment4.CabinetronView;

public class LoginController implements ActionListener{
	private CabinetronView view;
	private LoginView loginView;
	private Authenticator authenticator;
	private Session session;
	
	public LoginController(CabinetronView view, LoginView loginView) {
		this.view = view;
		this.loginView = loginView;
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		switch(command) {
		case "Login":
			try {
				authenticator = new Authenticator();
				session = authenticator.getUserCredentials(loginView.getUsername(), loginView.getPassword());
				if (session == null) {
					loginView.setErrorMessage("Invalid username or password");
					break;
				}
				view.enableMenu();	
				view.setTitle(session.getUserName());
				view.disableLogin();
				view.enableLogout();
				view.closeLoginView();
			} catch (NullPointerException n) {
				
			}
			
			break;
		case "Cancel":
			view.closeLoginView();
			break;
		}
	}
	
	public LoginController getController() {
		return this;
	}
	
	public boolean[] getPermisions() {
		return session.getAccessPrivileges();
	}
}
