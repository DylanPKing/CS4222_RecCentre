/*
 * Dylan King			17197813
 * Szymon Sztyrmer		17200296
 * Louise Madden		17198232
 * Brian Malone			17198178
 */

/**
 * User
 */
public class User
{

	private int userID;
	private String email;
	private String password;
	private int userType;

	User(int userID, String email, String password, int userType)
	{
		this.userID = userID;
		this.email = email;
		this.password = password;
		this.userType = userType;
	}
	/**
	 * @return the userID
	 */
	public int getUserID()
	{
		return userID;
	}
	
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(int userID)
	{
		this.userID = userID;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the userType
	 */
	public int getUserType()
	{
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(int userType)
	{
		this.userType = userType;
	}
}