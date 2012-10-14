package chat.model;

public class User {
	
	@Override
	public String toString() {
		return "User [displayName=" + displayName + ", emailAddress="
				+ emailAddress + "]";
	}

	private String displayName;
	private String emailAddress;
	
	public User(String displayName, String emailAddress) {
		super();
		this.displayName = displayName;
		this.emailAddress = emailAddress;
	}

	public User() {
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
