package AccessControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import assignment4.CabinetronView;

public class LoginController implements ActionListener{
	private CabinetronView view;
	
	public LoginController(CabinetronView view) {
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		switch(command) {
		case "Login":
			break;
		case "Cancel":
			view.closeLoginView();
			break;
		}
	}

}
