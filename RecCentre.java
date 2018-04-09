/**
 * RecCentre
 */
import java.util.*;			// Scanner
import javax.swing.*;		// JOptionPane   +   JTextArea
import java.io.*;			// IOException  +  Files
public class RecCentre
{
	public static ArrayList<Booking> bookings = new ArrayList<Booking>();
	public static ArrayList<Facility> facilities = new ArrayList<Facility>();
	public static ArrayList<User> users = new ArrayList<User>();
	public static void main(String[] args) throws IOException
	{
		fillArrayLists();
		if(login())
		{
			
		}
	}

	/**
	 * Takes in an email and password from the user, and checks it against
	 * the existing user objects.
	 * @return true if valid login
	 */
	public static boolean login()
	{
		String email, password;
		int attempt;
		boolean found = false;
		String emailMsg = "Enter your email address:";
		String passMsg = "Enter your password:";
		for (attempt = 0; attempt < 3 && !found; attempt++)
		{
			email = JOptionPane.showInputDialog(null, emailMsg, "Enter userame",
					JOptionPane.PLAIN_MESSAGE);
			password = JOptionPane.showInputDialog(null, passMsg, "Enter password",
				   	   JOptionPane.PLAIN_MESSAGE);
			for (int i = 0; i < users.size() && !found; i++)
			{
				if (users.get(i).getEmail().equals(email) && users.get(i).getPassword().equals(password))
				{
					found = true;
				}
			}
			if (!found)
			{
				emailMsg = "Invalid credentials. You have " + (3 - (attempt + 1)) +
						   " attempts remaining. Please enter email again:";
				passMsg = "Please enter password again";
			}
		}
		return found;
	}

	/**
	 * Takes in a user chosen by the administrator, and generates
	 * a random password between 8 and 50 characters, specified by
	 * the administrator, and writes it to the Users object.
	 * @param User
	 */
	public static void generatePassword(User newUser)
	{
		boolean validLength = false;
		String strPassLength, diagText = "Please enter length of password:\n" +
										 "Must be between 8 and 50 characters.";
		int passLength;
		while (!validLength)
		{
			strPassLength = JOptionPane.showInputDialog(null, diagText, "Enter length", JOptionPane.PLAIN_MESSAGE);
			if (strPassLength.matches("[0-9]{8,50}"))
			{
				passLength = Integer.parseInt(strPassLength);
				validLength = true;
			}
			else
			{
				diagText = "Invalid input. Please enter a number between 8 and 50.";
			}
		}
		String[] passwordPool = {"qwertyuiopasdfghjklzxcvbnm", "QWERTYUIOPASDFGHJKLZXCVBNM", "!£$%&*()?<>#/"};
		String password = "";
		int poolChosen;
		for (int i = 0; i < passLength; i++)
		{
			poolChosen = (int)(Math.random() * passwordPoo.length);
			password += passwordPool[poolChosen].charAt((int)(Math.random() * passwordPool[poolChosen].length()));
		}
		newUser.setPassword(password);
	}
	
	/**
	  * fillArrayLists() is a method that is executed at the launch of the program
	  * It has no passed in parameters
	  * It doesn't return any values
	  * It fills our arraylists with the data from the text files
	  * Saves having to read from the text files multiple times
	  * Calls the constructor methods for Booking, User and Facility
	  * Calls checkIfPaid() to check if the booking is paid for
	  */
	public static void fillArrayLists() throws IOException			// Since we have files we need to throw those exceptions away
	{
		File input1 = new File("Bookings.txt");								// A file for bookings
		int bookingID = 0;															// Variables that correspond to the data fields of 
		int facilityID = 0;															// the Booking type objects
		int userID = 0;
		//Date bookingDate;
		int slotNumber = 0;
		boolean paymentStatus = false;
		
		File input2 = new File("Users.txt");									// A file for users
		int userId;																		// Variables that correspond to the data fields of
		String email;																	// the User type objects
		String password;
		int userType;
		
		File input3 = new File("Facilities.txt");								// A file for facilities
		int facilityId;																	// Variables that correspond to the data fields of
		String facilityName;														// the Facility type objects
		double pricePerHour;
		//Date decommissionedUntilDate;
		
		Scanner in = new Scanner(input1);									// Set up a Booking scanner
		
		String [] lineFromFile;														// Variables for storage and splitting
		String test = "";
		
		if(input1.exists() && input2.exists() && input3.exists())	// Check if the text files exist first
		{																						// No point in trying to read something that doesn't exist
			if(input1.length() != 0)													// Check if the text file has any content
			{
				while(in.hasNext())													// Go through each line of the text file
				{
					lineFromFile = (in.nextLine().split(","));				// Split the line at every comma and put in array
					bookingID = Integer.parseInt(lineFromFile[0]);	// Fill data field variables with their respective information
					facilityID = Integer.parseInt(lineFromFile[1]);
					userID = Integer.parseInt(lineFromFile[2]);
					//bookingDate =						figure this out later
					slotNumber = Integer.parseInt(lineFromFile[4]);
					test = lineFromFile[5];
					paymentStatus = checkIfPaid(test);						// Need a method for this boolean
					Booking reservation = new Booking(bookingID, facilityID, userID, /*bookingDate,*/ slotNumber, paymentStatus);		// Create a Booking object with the variables
					bookings.add(reservation);									// Add the Booking to the arraylist of Bookings
				}
			}
			in.close();																	// Close the bookings scanner
			in = new Scanner(input2);											// Set up a scanner for users
			if(input2.length() != 0)													// Check if the text file has any content
			{
				while(in.hasNext())													// Go through every line of the text file
				{
					lineFromFile = (in.nextLine().split(","));				// Split the line at each comma and place it in the array
					userId = Integer.parseInt(lineFromFile[0]);		// Fill the data field variables
					email = lineFromFile[1];
					password = lineFromFile[2];
					userType = Integer.parseInt(lineFromFile[3]);
					User newUser = new User(userId, email, password, userType);	// Create the User type object
					users.add(newUser);											// Add the object to the users arraylist
				}
			}
			in.close();																	// Close the users scanner
			in = new Scanner(input3);											// Set up a facilities scanner
			if(input3.length() != 0)													// Check if the file has any content
			{
				while(in.hasNext())													// Go through every line of the text file
				{
					lineFromFile = (in.nextLine().split(","));				// Split the line at each comma and place it in the array
					facilityId = Integer.parseInt(lineFromFile[0]);	// Fill the data field variables
					facilityName = lineFromFile[1];
					pricePerHour = Double.parseDouble(lineFromFile[2]);
					if(lineFromFile.length == 3)									// Check the length of the array
					{																			// If its 3, then it has no decommission
						Facility newFacility = new Facility(facilityId, facilityName, pricePerHour);	// Create Facility type object
					}
					else																		// If its 4, then it is decommissioned and has an extra data field
					{
						//decommissionedUntilDate = 				figure this out later
						Facility newFacility = new newFacility(facilityId, facilityName, pricePerHour, /*decommissionedUntilDate*/);		// Create Facility type object
					}
					facilities.add(newFacility);									// Add the Facility type object to the facilities arraylist
				}
			}
			in.close();																	// Close the facilities scanner
		}
		else																					// if 1 or more of the files don't exist
		{
			JOptionPane.showMessageDialog(null, "One or more files don't exist", "Error", 2);		// Get this error mate
		}
	}
	
	/**
	  * checkIfPaid() checks the bookings to see if it was paid
	  * It is called from the fillArrayLists() method to help create a boolean
	  * It is passed a String as a parameter
	  * It returns a boolean
	  */
	public static boolean checkIfPaid(String test)
	{
		boolean paid = false;
		if(test.equals("true"))
		{
			paid = true;
		}
		return paid;
	}
	
	
}