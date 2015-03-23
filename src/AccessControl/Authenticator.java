package AccessControl;

public class Authenticator {
		private String[] logins = {"TomJones@Cabinetron.com", "SueSmith@Cabinetron.com", "RagnarNelson@Cabinetron.com"};
		private String[] passwords = {"TommyJ$$$", "KittyLuver75", "EX-23%^++l33t"};
		
	public Authenticator() {
		
	}
	
	public Session getUserCredentials(String login, String password) {
		if (login.equals(logins[0]) && password.equals(passwords[0])) {
			return new Session(new ProductionManager("Tom Jones", "TomJones@Cabinetron.com"));
		} else if (login.equals(logins[1]) && password.equals(passwords[1])) {
			return new Session(new InventoryManager("Sue Smith", "SueSmith@Cabinetron.com"));
		} else if (login.equals(logins[2]) && password.equals(passwords[2])) {
			return new Session(new InventoryManager("Ragnar Nelson", "RagnarNelson@Cabinetron.com"));
		} else
			return null;
	}
}
