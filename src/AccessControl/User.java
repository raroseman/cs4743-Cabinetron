package AccessControl;

public abstract class User {
	private String fullName;
	private String emailAddress;
	
	public User(String fullName, String emailAddress) {
		this.fullName = fullName;
		this.emailAddress = emailAddress;
	}
}