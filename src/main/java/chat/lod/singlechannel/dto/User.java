package chat.lod.singlechannel.dto;



public class User {
	
	private String displayName;
	private String emailAddress;
	//private HttpServletResponse connection;
	private int lastMessageId;

	public User(String displayName, String emailAddress) { //, HttpServletResponse connection) {
		super();
		this.displayName = displayName;
		this.emailAddress = emailAddress;
		// this.connection = connection;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

//	public void setEmailAddress(String emailAddress) {
//		this.emailAddress = emailAddress;
//	}
//
//	public void setDisplayName(String displayName) {
//		this.displayName = displayName;
//	}
	
	public int getLastMessageId() {
		return lastMessageId;
	}

	public void setLastMessageId(int lastMessageId) {
		this.lastMessageId = lastMessageId;
	}

//	public HttpServletResponse getConnection() {
//		return connection;
//	}
//
//	public void setConnection(HttpServletResponse connection) {
//		this.connection = connection;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [ displayName=" + displayName + ", emailAddress="
				+ emailAddress + "]";
	}

}
