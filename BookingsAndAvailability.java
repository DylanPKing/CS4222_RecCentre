import java.util.*;			// Scanner
import javax.swing.*;		// JOptionPane   +   JTextArea
import java.io.*;			// IOException  +  Files
import java.time.*;

/**
 * RecCentre
 */
public class RecCentre
{
	public static ArrayList<Booking> bookings = new ArrayList<Booking>();
	public static ArrayList<Facility> facilities = new ArrayList<Facility>();
	public static ArrayList<User> users = new ArrayList<User>();
	public static boolean isAdmin;
	public static int loggedInUser;
	public static void main(String[] args) throws IOException
	{
		fillArrayLists();
		if(login())
		{
			int inputNum;
			boolean quit = false;
			while (!quit)
			{
				if (isAdmin) 
				{
					String[] adminOptions = {"Register a new user", "Add a new facility", "Edit or View a facility", 
											 "Remove a facility", "Record Payments", "View Accounts"};
					inputNum = JOptionPane.showOptionDialog(null, "What would you like to do?", "Options", JOptionPane.YES_NO_CANCEL_OPTION,
															JOptionPane.QUESTION_MESSAGE, null, adminOptions, "Quit");
					switch (inputNum)
					{
						case 0:
							// Register new user method
							break;
						
						case 1:
							// Add new facility method
							break;
						
						case 2: 
							editAndViewFacilities();
							break;
						
						case 3:
							//Remove facility method
							break;

						case 4:
							// Record payments method
							break;
							
						case 5:
							// View account statements
							break;
						
						default:
							quit = true;
							break;
					}
				}
				else
				{
					String[] userOptions = {"View your Bookings", "View your statements"};
					inputNum = JOptionPane.showOptionDialog(null, "What would you like to do?", "Options", JOptionPane.YES_NO_CANCEL_OPTION, 
															JOptionPane.QUESTION_MESSAGE, null, userOptions, "Quit");
					switch (inputNum)
					{
						case 0:
							// userViewBookings();
							break;
						
						case 1:
							// View statement
							break;
							
						default:
							quit = true;
							break;
					}
				}
			}
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
			if (email != null)
			{
				password = JOptionPane.showInputDialog(null, passMsg, "Enter password",
						JOptionPane.PLAIN_MESSAGE);
				for (int i = 0; i < users.size() && !found; i++)
				{
					if (users.get(i).getEmail().equals(email) && users.get(i).getPassword().equals(password))
					{
						found = true;
						isAdmin = users.get(i).getUserType() == 1;
					}
				}
				if (!found)
				{
					emailMsg = "Invalid credentials. You have " + (3 - (attempt + 1)) +
							" attempts remaining. Please enter email again:";
					passMsg = "Please enter password again";
				}
			}
		}
		return found;
	}

	/**
	 * Takes in a user chosen by the administrator, and generates
	 * a random password between 8 and 50 characters, specified by
	 * the administrator, and writes it to the Users object.
	 * @param newUser
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
				String[] passwordPool = {"qwertyuiopasdfghjklzxcvbnm", "QWERTYUIOPASDFGHJKLZXCVBNM", "!£$%&*()?<>#/"};
				String password = "";
				int poolChosen;
				for (int i = 0; i < passLength; i++)
				{
					poolChosen = (int)(Math.random() * passwordPool.length);
					password += passwordPool[poolChosen].charAt((int)(Math.random() * passwordPool[poolChosen].length()));
				}
				newUser.setPassword(password);
			}
			else
			{
				diagText = "Invalid input. Please enter a number between 8 and 50.";
			}
		}
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
		LocalDate bookingDate;
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
		LocalDate decommissionedUntilDate;
		String decomDateString;

		Scanner in = new Scanner(input1);									// Set up a Booking scanner
		
		String [] lineFromFile;	
		String [] dateElements;													// Variables for storage and splitting
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
					dateElements = lineFromFile[3].split("/");		// Takes the date from file into its own array
					bookingDate = LocalDate.of(Integer.parseInt(dateElements[2]),
								  Integer.parseInt(dateElements[1]), Integer.parseInt(dateElements[0]));
								  // COnverts the date array into an actual date
					slotNumber = Integer.parseInt(lineFromFile[4]);
					test = lineFromFile[5];
					paymentStatus = checkIfPaid(test);						// Need a method for this boolean
					Booking reservation = new Booking(bookingID, facilityID, userID, bookingDate, slotNumber, paymentStatus);		// Create a Booking object with the variables
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
						facilities.add(newFacility);
					}
					else																		// If its 4, then it is decommissioned and has an extra data field
					{
						decomDateString = JOptionPane.showInputDialog(null, "Please enter a date in the format dd/mm/yyyy");
						if (decomDateString.matches("[0-9]{2}/[0-9]{1,2}/[0-9]{4}"))
						{
							dateElements = decomDateString.split("/");
							decommissionedUntilDate = LocalDate.of(Integer.parseInt(dateElements[2]), Integer.parseInt(dateElements[1]), Integer.parseInt(dateElements[0]));	// Creats thedate object
							Facility newFacility = new Facility(facilityId, facilityName, pricePerHour, decommissionedUntilDate);		// Create Facility type object
							facilities.add(newFacility);									// Add the Facility type object to the facilities arraylist
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Error: invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
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
	
	public static void editAndViewFacilities()
	{
		String[] facilitiesNames = new String[facilities.size()];
		String facilityToEdit;
		boolean idFound = false;
		int idToEdit = 0;
		try
		{
			for (int i = 0; i < facilities.size(); i++)
			{
				facilitiesNames[i] = facilities.get(i).getFacilityName();
			}
			facilityToEdit = (String)(JOptionPane.showInputDialog(null, "What facility would you like to edit/view?",
							"Choose a facility", 1, null, facilitiesNames, facilitiesNames[0]));
			for (int i = 0; i < facilities.size() && !idFound; i++)
			{
				if (facilities.get(i).getFacilityName().equals(facilityToEdit))
				{
					idToEdit = facilities.get(i).getFacilityID();
					idFound = true;
				}
			}
			String[] options = {"View facility availability", "Make a booking", "View Bookings"};
			int inputNum = JOptionPane.showOptionDialog(null,"What would you like to do?", "Options", JOptionPane.YES_NO_CANCEL_OPTION,
														JOptionPane.QUESTION_MESSAGE, null, options, "Quit");
			switch(inputNum)
			{
				case 0:
					viewFacilityAvailibilites(idToEdit);
					break;
				
				case 1:
					makeBooking(idToEdit);
					break;

				case 2:
					viewBookings(idToEdit);
					break;

				default:
					break;
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			//TODO: handle exception. Change to If statement
			JOptionPane.showMessageDialog(null, "Error: No facilities registered. Please create a new facility before attempmting to view or edit.", "Error", JOptionPane.ERROR_MESSAGE);
		}
			
	}

	public static void viewFacilityAvailibilites(int idToEdit)
	{
		/* TODO:
		 * - Ask which facility to check the avalibility for (List? or regular input and then check if it exists or is decommissioned)
		 * - Take in 2 inputs a start date and an end date to check(inclusive)
		 * - Bubble sort the bookings ArrayList by date first?
		 * - Go through by the dates(index 3) and check the slot(index 4) use Nested for loops
		 * - If statements that if it isn't in the booking Array add it to a string and JTextArea?
		 */ 
		 String startDate, endDate;
		 startDate = JOptionPane.showInputDialog(null, "Check availabilities from what date? /n(dd/mm/yyyy)");
		 endDate = JOptionPane.showInputDialog(null, "Until what date? /n(dd/mm/yyyy)");
		 String[] startArray = startDate.split("/");
		 String[] endArray = endDate.split("/");
		 LocalDate localStartDate = LocalDate.of(Integer.parseInt(startArray[2]), Integer.parseInt(startArray[1]), Integer.parseInt(startArray[0]));
		 LocalDate localEndDate = LocalDate.of(Integer.parseInt(endArray[2]), Integer.parseInt(endArray[1]), Integer.parseInt(endArray[0]));
		 LocalDate testDate;
		 for (testDate = localStartDate; testDate.isBefore(localEndDate) || testDate.equals(localEndDate); testDate.plusDays(1))
		 {
			String output;
			for (int i = 0; i < bookings.size(); i++)
			{
				if (bookings.get(i).getFacilityID() == idToEdit)
				{
					if (bookings.get(i).getBookingDate().equals(testDate))
					{
						for (int j = 1; j < 10; j++)
						{
							if (bookings.get(i).getSlotNumber() != j)
							{
								output = bookings.get(i) + "/n";
							}
						}
					}
				}
			}
		 }
	}
	
	public static void makeBooking(int idToEdit)
	{
		/* TODO:
		 * - Get the booking ID from the last entry in the booking Array (Sort by Booking ID first)
		 * - Get the facility to make the booking for and check it exists and isnt decommissioned
		 * - Get user ID (another global variable?)
		 * - Get the date from the user
		 * - Get the time from the user (List options available? or list all options and then check if it is available?)
		 * - Ask the user if payment has been made(if it has add Y to ArrayList otherwise N)
		 * - Display a message to say that the booking has been made
		 */
		int bookingID, facilityID, userID, slotNumber;
		LocalDate bookingDate;
		boolean paid;
		boolean decommissioned;
		String date;
		String[] dateArray;
		boolean validDate = false;
		while (!validDate)
		{
			date = JOptionPane.showInputDialog(null, "What date would you like to make the booking for? (dd-mm-yyyy)");
			dateArray = date.split("-");
			bookingDate = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]));
			boolean found = false;
			int i;
			for (i = 0; i < facilities.size() && !found; i++)
			{
				found = (facilities.get(i).getFacilityID() == idToEdit)
			}
			i--;
			LocalDate decomissionUntil = facilities.get(i).getDecommissionUntilLocalDate();
			if (bookingDate.isAfter(LocalDate.now()) && decommissionUntil.isBefore(bookingDate))//check that it isnt decommissioned at the time
			{
				validDate = true;
				if (bookings.isEmpty)
				{
					bookingID = 0;
				}
				else
				{
					bookingID = (bookings.size()) + 1;
				}
				facilityID = idToEdit;
				userID = Integer.parseInt(JOptionPane.showInputDialog(null, "What user is making the booking? (Please enter ID number"));
				slotNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "What time would you like to make the booking for? /n
																		  Please enter in 24 hour format between 09 and 17"));
				slotNumber = slotNumber - 8;
				if (JOptionPane.showConfirmDialog(null, "Has a payment been made?",
				"Payment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				{
					paid = true;
				}
				else
				{
					paid = false;
				}
				Booking newBooking = new Booking(bookingID, facilityID, userID, bookingDate, slotNumber, paid);
				FileWriter writeBooking = new FileWriter("bookings.txt", true);
				PrintWriter out = new PrintWriter(writeBooking);
				out.println(newBooking);
			}
		}
	}
	
	public static void viewBookings(int idToView)
	{
		ArrayList<Booking> bookingsToView = new ArrayList<Booking>();
		int facilityID;
		LocalDate bookingDate, dateToView;
		String[] dateElements;
		String dateString = JOptionPane.showInputDialog(null, "Please enter the date you would like to view in the format dd/mm/yyyy", "Enter date");
		String output = "";
		if (dateString.matches("[0-9]{2}/[0-9]{{1,2}/[0-9]{4}"))
		{
			dateElements = dateString.split("/");
			dateToView = LocalDate.of(Integer.parseInt(dateElements[2]), Integer.parseInt(dateElements[1]),
									  Integer.parseInt(dateElements[0]));

			if (isAdmin)
			{
				for (int i = 0; i < bookings.size(); i++)
				{
					facilityID = bookings.get(i).getFacilityID();
					bookingDate = bookings.get(i).getBookingDate();
					if (facilityID == idToView && bookingDate.isEqual(dateToView))
					{
						bookingsToView.add(bookings.get(i));
					}
				}
			}
			else
			{
				for (int i = 0; i < bookings.size(); i++)
				{
					facilityID = bookings.get(i).getFacilityID();
					bookingDate = bookings.get(i).getBookingDate();
					bookingUser = bookings.get(i).getUserID();
					if (facilityID == idToView && bookingDate.isEqual(dateToView) &&
						bookingUser == currentUser.getUserID())
					{
						bookingsToView.add(bookings.get(i));
					}
				}
			}
			if (bookingsToView.size() != 0)
			{
				String paid;
				output += "These bookings are scheduled in this facility today:\n";
				for (int i = 0; i < bookingsToView.size(); i++)
				{
					paid = "has not been paid";
					if (bookingsToView.get(i).getPaymentStatus())
					{
						paid = "has been paid";
					}
					output += "User" + bookingsToView.get(i).getUserID() + " has a booking at " +
							  (bookingsToView.get(i).getSlotNumber() + 8) + " and " + paid + "\n";
				}
			}
			else
			{
				output += "No booking scheduled on the day entered.";
			}
		}
		else
		{
			output += "Error: Invalid date format.";
		}
		JOptionPane.showMessageDialog(null, output, "Result", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
		  *	viewAccountStatement() can be called from the main
		  * It reads the arraylists and checks the payment status of a user.
		  * It can be accessed by both the admin and the user
		  * If accessed by the admin, it displays all the facilities and the outstanding balances
		  * If accessed by a user, it displays  all the outstanding balances for that user
		  */
		public static void viewAccountStatement()
		{
			String statement = "";																						// My vars
			String pay = "";
			String facilityCheck = "";
			if(isAdmin)																										// If admin is logged on
			{
				for(int i = 0; i < bookings.size(); i++)															// Run through bookings
				{
					if(bookings.get(i).getPaymentStatus())														// If paid
					{
						for(int j = 0; j < facilities.size(); j++)													// Run through facilities
						{
							if(facilities.get(j).getFacilityID() == bookings.get(i).getFacilityID())											// Find the facility for that booking
							{
								pay = "has been paid";								// Attach its price to pay
								facilityCheck = facilities.get(j).getFacilityName();
							}
						}
					}
					else																											// If not paid
					{
						for(int j = 0; j < facilities.size(); j++)													// Run through facilities
						{
							if(facilities.get(j).getFacilityID() == bookings.get(i).getFacilityID())										// Find the facility for that booking
							{
								pay = "has an outstanding balance of: \u20AC" + facilities.get(j).getPricePerHour();									// Attach its price to pay
								facilityCheck = facilities.get(j).getFacilityName();
							}
						}
					}
					statement += facilityCheck + "\t" + pay + "\tby user:" + bookings.get(i).getUserID() + "\n";	// Ammend the whole thing onto statement
				}
			}
			else																													// Is not admin
			{
				for(int i = 0; i < bookings.size(); i++)															// Run through bookings
				{
					if(bookings.get(i).getPaymentStatus())														// If paid
					{
						for(int j = 0; j < facilities.size(); j++)													// Run through facilities
						{
							if(facilities.get(j).getFacilityID() == bookings.get(i).getFacilityID())											// Find the facility for that booking
							{
								pay = "has been paid";								// Attach its price to pay
								facilityCheck = facilities.get(j).getFacilityName();
							}
						}
					}
					else																											// If not paid
					{
						for(int j = 0; j < facilities.size(); j++)													// Run through facilities
						{
							if(facilities.get(j).getFacilityID() == bookings.get(i).getFacilityID())											// Find the facility for that booking
							{
								pay = "has an outstanding balance of: \u20AC" + facilities.get(j).getPricePerHour();									// Attach its price to pay
								facilityCheck = facilities.get(j).getFacilityName();
							}
						}
					}
					if(bookings.get(i).getUserID() == (loggedInUser))																						// If used a user with matching id
					{
						statement += facilityCheck + "\t" + pay + "\tby user:" + bookings.get(i).getUserID() + "\n";			// Amend the relevant booking to statement
					}
				}
			}
			JOptionPane.showMessageDialog(null, statement);											// Show the bookings
		}

		public static String generatePassword()
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
				String[] passwordPool = {"qwertyuiopasdfghjklzxcvbnm", "QWERTYUIOPASDFGHJKLZXCVBNM", "!£$%&*()?<>#/"};
				String password = "";
				int poolChosen;
				for (int i = 0; i < passLength; i++)
				{
					poolChosen = (int)(Math.random() * passwordPool.length);
					password += passwordPool[poolChosen].charAt((int)(Math.random() * passwordPool[poolChosen].length()));
				}
				return password;
			}
			else
			{
				diagText = "Invalid input. Please enter a number between 8 and 50.";
			}
		}
	}
	

		public static void reisterUser()
		{
			int userID = users.size();
			boolean validEmail = false;
			while (!validEmail)
			{
				String email = JOptionPane.showInputDialog(null,"Please enter the amail address");
				if (email.contains.contains("@") && (email.indexOf("@") != 0) && (email.indexOf("@") != email.length()-1))
				{
					validEmail = true;
				}
			}
			String password = generatePassword();
			User newUser = new User(userID, validEmail, password, 2);
		}
}