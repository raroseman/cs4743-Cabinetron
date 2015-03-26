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
			authenticator = new Authenticator();
			session = authenticator.getUserCredentials(loginView.getUsername(), loginView.getPassword());
			view.enableMenu();
			view.closeLoginView();
			view.setTitle(session.getUserName());
			System.out.println(session.getUserName());
			break;
		case "Cancel":
			view.closeLoginView();
			break;
		}
	}

}
