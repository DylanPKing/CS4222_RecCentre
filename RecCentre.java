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
			boolean quit = false;
			while (!quit)
			{
				mainInterface();
			}
		}
	}
	
	public static void mainInterface() throws IOException
	{
		int inputNum;
		boolean quit = false;
		if (isAdmin) 
				{
					String[] adminOptions = {"Register a new user", "Add a new facility", "Edit or View a facility", 
											 "Remove a facility", "Record Payments", "View Accounts", "Quit"};
					inputNum = JOptionPane.showOptionDialog(null, "What would you like to do?", "Options", JOptionPane.YES_NO_CANCEL_OPTION,
															JOptionPane.QUESTION_MESSAGE, null, adminOptions, "Quit");
					switch (inputNum)
					{
						case 0:
							registerUser();
							break;
						
						case 1:
							createFacility();
							break;
						
						case 2: 
							editAndViewFacilities();
							break;
						
						case 3:
							deleteFacility();
							break;

						case 4:
							// Record payments method     needs confirmation
							break;
							
						case 5:
							viewAccountStatement();
							break;
						
						case 6:
							System.exit(0);
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
							viewBookings();
							break;
						
						case 1:
							viewAccountStatement();
							break;
							
						default:
							quit = true;
							break;
					}
				}
	}
	
	/**
	 * Takes in an email and password from the user, and checks it against
	 * the existing user objects.
	 * @return true if valid login
	 */
	public static boolean login() throws IOException
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
						loggedInUser = users.get(i).getUserID();
					}
				}
				if (!found)
				{
					emailMsg = "Invalid credentials. You have " + (3 - (attempt + 1)) +
							" attempts remaining. Please enter email again:";
					passMsg = "Please enter password again";
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Closing Application");
				System.exit(0);
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
	public static String generatePassword() throws IOException
	{
		String password = "";
		String pattern = "[0-9]{1,2}";
		boolean validLength = false;
		String strPassLength, diagText = "Please enter length of password:\n" +
										 "Must be between 8 and 50 characters.";
		int passLength;
		while (!validLength)
		{
			strPassLength = JOptionPane.showInputDialog(null, diagText, "Enter length", JOptionPane.PLAIN_MESSAGE);
			if (strPassLength.matches(pattern))
			{
				passLength = Integer.parseInt(strPassLength);
				if(passLength > 7 && passLength < 51)
				{
					validLength = true;
					String[] passwordPool = {"qwertyuiopasdfghjklzxcvbnm", "QWERTYUIOPASDFGHJKLZXCVBNM", "!£$%&*()?<>#/"};
					int poolChosen;
					for (int i = 0; i < passLength; i++)
					{
						poolChosen = (int)(Math.random() * passwordPool.length);
						password += passwordPool[poolChosen].charAt((int)(Math.random() * passwordPool[poolChosen].length()));
					}
				}
				else
				{
					diagText = "Invalid input. Please enter a number between 8 and 50";
				}
			}
			else
			{
				diagText = "Invalid input. Please enter a number between 8 and 50.";
			}
		}
		return password;
	}

	public static void registerUser() throws IOException
		{
			String email = "";
			int userID = users.size();
			boolean validEmail = false;
			while (!validEmail)
			{
				email = JOptionPane.showInputDialog(null,"Please enter the email address");
				if (email.contains("@") && (email.indexOf("@") != 0) && (email.indexOf("@") != email.length()-1))
				{
					validEmail = true;
				}
			}
			String password = generatePassword();
			User newUser = new User(userID, email, password, 2);
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
	
	public static void editAndViewFacilities() throws IOException
	{
		String[] facilitiesNames = new String[facilities.size()];
		String facilityToEdit;
		boolean idFound = false;
		boolean quit = false;
		int idToEdit = 0;
		try
		{
			while(!quit)
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
				String[] options = {"View facility availability", "Make a booking", "View Bookings", "Decommission a facility", "Recommission a facility"};
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
						viewBookings();
						break;

					case 3:
						suspendFacility();
						
					case 4:
						removeSuspension();
						
					default:
						quit = true;
						break;
					}
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				//TODO: handle exception. Change to If statement
				JOptionPane.showMessageDialog(null, "Error: No facilities registered. Please create a new facility before attempmting to view or edit.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	

	public static void viewFacilityAvailibilites(int idToEdit) throws IOException
	{
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
		JOptionPane.showMessageDialog(null, output);
	}
	
	public static void makeBooking(int idToEdit) throws IOException
	{
		int bookingID, facilityID, userID, slotNumber;
		LocalDate bookingDate = null;
		boolean paid;
		boolean decommissioned;
		String date;
		String temp1 = "";
		String temp2 = "";
		String[] dateArray;
		String pattern2 = "[0-9]{1,}";
		String pattern = "[0-9]{2}-[0-9]{2}-[0-9]{4}";
		boolean validDate = false;
		while (!validDate)
		{
			date = JOptionPane.showInputDialog(null, "What date would you like to make the booking for? (dd-mm-yyyy)");
			if(date == null)
			{
				mainInterface();
			}
			if(date.matches(pattern))
			{
				dateArray = date.split("-");
				try
				{
					bookingDate = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]));
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, "The date supplied is not a real date");
					makeBooking(idToEdit);
				}
				boolean found = false;
				int i;
				for (i = 0; i < facilities.size() && !found; i++)
				{
					found = (facilities.get(i).getFacilityID() == idToEdit);
				}
				i--;
				LocalDate decommissionUntil = facilities.get(i).getDecommissionedUntilLocalDate();
				if(decommissionUntil == null)
				{
					if (bookingDate.isAfter(LocalDate.now()))//check that it isnt decommissioned at the time
					{
						validDate = true;
						if (bookings.isEmpty())
						{
							bookingID = 0;
						}
						else
						{
							bookingID = (bookings.size()) + 1;
						}
						facilityID = idToEdit;
						temp1 = JOptionPane.showInputDialog(null, "What user is making the booking? (Please enter ID number");
						if(temp1 == null)
						{
							mainInterface();
						}
						if(!(temp1.matches(pattern2)))
						{
							mainInterface();
						}
						userID = Integer.parseInt(temp1);
						temp2 = JOptionPane.showInputDialog(null, "What time would you like to make the booking for? \n Please enter in 24 hour format between 09 and 17");
						if(temp2 == null)
						{
							mainInterface();
						}
						if(!(temp2.matches(pattern2)))
						{
							mainInterface();
						}
						slotNumber = Integer.parseInt(temp2);
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
						bookings.add(newBooking);
						FileWriter writeBooking = new FileWriter("bookings.txt", true);
						PrintWriter out = new PrintWriter(writeBooking);
						out.println(newBooking);
						JOptionPane.showMessageDialog(null, "Your booking has been successfully");
					}
				}
				else
				{
					if (bookingDate.isAfter(LocalDate.now()) && decommissionUntil.isBefore(bookingDate))//check that it isnt decommissioned at the time
					{
						validDate = true;
						if (bookings.isEmpty())
						{
							bookingID = 0;
						}
						else
						{
							bookingID = (bookings.size()) + 1;
						}
						facilityID = idToEdit;
						userID = Integer.parseInt(JOptionPane.showInputDialog(null, "What user is making the booking? (Please enter ID number"));
						slotNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "What time would you like to make the booking for? /n Please enter in 24 hour format between 09 and 17"));
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
						JOptionPane.showMessageDialog(null, "Your booking has been successfully");
						writeBooking.close();
						out.close();
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Invalid format supplied");
			}
		}
	}
	
	public static void viewBookings() throws IOException
	{
		ArrayList<Booking> bookingsToView = new ArrayList<Booking>();
		int facilityID;
		int bookingUser = 0;
		LocalDate bookingDate, dateToView;
		String[] dateElements;
		String dateString = JOptionPane.showInputDialog(null, "Please enter the date you would like to view in the format dd-mm-yyyy", "Enter date");
		String output = "";
		if (dateString.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}"))
		{
			dateElements = dateString.split("-");
			dateToView = LocalDate.of(Integer.parseInt(dateElements[2]), Integer.parseInt(dateElements[1]),
									  Integer.parseInt(dateElements[0]));

			if (isAdmin)
			{
				for (int i = 0; i < bookings.size(); i++)
				{
					facilityID = bookings.get(i).getFacilityID();
					bookingDate = bookings.get(i).getBookingDate();
					if (bookingDate.isEqual(dateToView))
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
					if (bookingDate.isEqual(dateToView) &&
						bookingUser == loggedInUser)
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
		public static void viewAccountStatement() throws IOException
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
		
		public static void createFacility() throws IOException
	{
		String input, input2, filename, error1, error2, pattern, facilityName;
		int facilityID = 0;
		String price = "";
		double pricePerHour = 0.0;
		error1 = "Goodbye";
		error2 = "Please enter a valid facility name";
		pattern = "[a-zA-z' ]{1,}";
		String numPattern = "[0-9]{1,2}.[0-9]{2}";
		input2 = " ";
		facilityName = " ";
		filename = "Facilities.txt";
		FileWriter fr = new FileWriter(filename, true);
		PrintWriter pr = new PrintWriter(fr);
		facilityName = makeName();
		pricePerHour = makePrice();
		
				facilityID = generateID();
				facilities.add(new Facility(facilityID, facilityName, pricePerHour));
				pr.print(facilityID +","+ facilityName +","+ pricePerHour);
				pr.println();
				fr.close();
				pr.close();
	
			String [] choices = {"Yes", "No"};
			String selection = (String) JOptionPane.showInputDialog(
			null, "Would you like to create another facility?", "Create", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				if(selection != null)
				{
					if(selection.matches(choices[0]))
					{
						createFacility();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Returning to menu");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Returning to menu");
					mainInterface();
				}
	}
			
	
	
	public static String makeName() throws IOException
	{
		String facilityName = JOptionPane.showInputDialog(null, "Enter facility name");
		if(facilityName == null)
		{
			JOptionPane.showMessageDialog(null, "Creation aborted\nReturning to main menu");
			mainInterface();
		}
		if(facilityName.equals(""))
		{
			JOptionPane.showMessageDialog(null, "No name provided\nPlease supply a name for the facility");
			makeName();
		}
		for(int i = 0; i < facilities.size(); i++)
		{
			if(facilityName.equals(facilities.get(i).getFacilityName()))
			{
				JOptionPane.showMessageDialog(null, "You already have a facility with this name", "Error", 2);
				makeName();
			}
		}
		return facilityName;
	}
	
	public static double makePrice() throws IOException
	{
		String price = JOptionPane.showInputDialog(null, "Enter the price per hour\nUse the range(0.01 - 99.99)");
		double actualPrice = 0.0;
		String pattern = "[0-9]{1,2}.[0-9]{2}";
		if(price == null)
		{
			JOptionPane.showMessageDialog(null, "Creation aborted\nReturning to the main menu");
			mainInterface();
		}
		if(price.equals(""))
		{
			JOptionPane.showMessageDialog(null, "No Price put in\nPut in a price");
			makePrice();
		}
		if(!(price.matches(pattern)))
		{
			JOptionPane.showMessageDialog(null, "Invalid price input");
			makePrice();
		}
		actualPrice = Double.parseDouble(price);
		return actualPrice;
	}
	
	public static int generateID()
	{
		int uniqueId;
		uniqueId = (int)(Math.random() * 500 + 1);
		for(int i = 0; i < facilities.size(); i++)
		{
			if(facilities.get(i).getFacilityID() == uniqueId)
			{
				generateID();
			}
		}
		return uniqueId;
			
	}
	
	public static void suspendFacility() throws IOException
	{
		/*string array choices, JOptionPane.QUESTION_MESSAGE, getFacilityName plug in to choices, 
		*/
		
		String filename = "Facilities.txt";
		String file = "";
		FileWriter fr = new FileWriter(filename, false);
		PrintWriter pr = new PrintWriter(fr);
		
		String suspendedFacilityName = "";
		int suspendedFacilityID = 0;
		double suspendedPricePerHour = 0.0;
		String pattern = "[0-9]{4}/[0-9]{2}/[0-9]{2}";
		LocalDate temp;
		
		boolean found = false;
		String dateInput = "";
		LocalDate decommissionDate = null;
	
		String error1 = "Goodbye";
		String error2 = "Please provide a valid date in the format yyyy/mm/dd";
		
		String [] dateSplit; //Not specifing array size due to the nature of .split method
		
		if(facilities.size() > 0)
		{
			
			String [] choices = new String[facilities.size()];
			for(int i = 0; i < facilities.size(); i++)
			{
				choices [i] = facilities.get(i).getFacilityName();
			}
			String selection = (String) JOptionPane.showInputDialog(
				null, "Would you like to suspend a facility?", "Suspend", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					if(selection != null)
					{
						for(int j = 0; j < choices.length && !found; j++)
						{
							if(selection.matches(choices[j]))
							{
								suspendedFacilityName = facilities.get(j).getFacilityName();
								suspendedFacilityID = facilities.get(j).getFacilityID();
								suspendedPricePerHour = facilities.get(j).getPricePerHour();
								//take this object and bring them back with some motherfucking dates
								dateInput = JOptionPane.showInputDialog(null, "Please supply the date you wish to decommission this facility until in the format yyyy/mm/dd");
								//validate format
								found = true;
								facilities.remove(j);
							}
						}
							if(dateInput != null)
									{
										if(!(dateInput.equals("")))
										{
											if(dateInput.matches(pattern))//For now assume valid date, will need to revise due to 9111/70/43 etc
											{
												dateSplit = dateInput.split("/");
												try
												{
													decommissionDate = LocalDate.of(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
												}
												catch (Exception e)
												{
													JOptionPane.showMessageDialog(null, "Invalid date entered"); 
													createFacility();
												}
												facilities.add(new Facility(suspendedFacilityID, suspendedFacilityName, suspendedPricePerHour, decommissionDate));
									
												pr.print("");
												pr.close();
												fr.close();
												fr = new FileWriter(filename, true);
												pr = new PrintWriter(fr);
												
												for(int i = 0; i < facilities.size(); i++)
												{
													temp = facilities.get(i).getDecommissionedUntilLocalDate();
													if(temp == null)
													{
														file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour();
														pr.print(file);
														pr.println();
													}
													else
													{
														file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour() + "," + facilities.get(i).getDecommissionedUntilLocalDate();
														pr.print(file);
														pr.println();
													}
												}
												fr.close();
												pr.close();
												mainInterface();
											}
											else
											{
												JOptionPane.showMessageDialog(null, error2);
												mainInterface();
											}
										}
										else
										{
											JOptionPane.showMessageDialog(null, error2);
											mainInterface();
										}
									}
									else
									{
										JOptionPane.showMessageDialog(null, error2);
										mainInterface();
									}
					}
					mainInterface();
		}
	}
	
	public static void removeSuspension() throws IOException
	{
		String options[];
		String [] valids = {"Yes", "No"};
		String input;
		String file = "";
		int count = 0;
		int token = 0;
		String recommissionedName = "";
		int recommissionedID = 0;
		double recommissionedPricePerHour = 0.0;
		LocalDate temp;
		
		String filename = "Facilities.txt";
		FileWriter fr = new FileWriter(filename, false);
		PrintWriter pr = new PrintWriter(fr);
		
		for(int i = 0; i < facilities.size(); i++)
		{
			temp = facilities.get(i).getDecommissionedUntilLocalDate();
			if(temp != null)
			{
				count++;
			}
		}
		if(count != 0)
		{
			options = new String[count];
			for(int i = 0; i < facilities.size(); i++)
			{
				temp = facilities.get(i).getDecommissionedUntilLocalDate();
				if(temp != null)
				{
					options[token] = facilities.get(i).getFacilityName();
					token++;
				}
			}
			input = (String) JOptionPane.showInputDialog(null,"choose a facility:","Facility",JOptionPane.QUESTION_MESSAGE, null, options,options[0]); 
			if(input == null || input.equals(""))
			{
				mainInterface();
			}
			for(int i = 0; i < facilities.size(); i++)
			{
				if(input.matches(facilities.get(i).getFacilityName()))
				{
					recommissionedID = facilities.get(i).getFacilityID();
					recommissionedName = facilities.get(i).getFacilityName();
					recommissionedPricePerHour = facilities.get(i).getPricePerHour();
					String validation = (String) JOptionPane.showInputDialog(null, "Are you sure you want to recommission this facility?", "Recommission Menu", JOptionPane.QUESTION_MESSAGE,null, valids, valids[0]);
					if(validation.equals(valids[0]))
					{
						facilities.remove(i);
					}
					else
					{
						mainInterface();
					}
				}
			}
			facilities.add(new Facility(recommissionedID, recommissionedName, recommissionedPricePerHour));
			
			pr.print("");
			fr.close();
			pr.close();
			fr = new FileWriter(filename, true);
			pr = new PrintWriter(fr);
			
			for(int i = 0; i < facilities.size(); i++)
			{
				temp = facilities.get(i).getDecommissionedUntilLocalDate();
				if(temp == null)
				{
					file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour();
					pr.print(file);
					pr.println();
				}
				else
				{
					file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour() + "," + facilities.get(i).getDecommissionedUntilLocalDate();
					pr.print(file);
					pr.println();
				}
			}
			fr.close();
			pr.close();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "There are no facilities to recommission");
			mainInterface();
		}
	}
	
	public static void deleteFacility() throws IOException
	{
		//permanently delete facilities
		
		String error1 = "Goodbye";
		String filename = "Facilities.txt";
		String file = "";
		LocalDate temp;
		FileWriter fr = new FileWriter(filename, false);
		PrintWriter pr = new PrintWriter(fr);
		
		String [] valids = {"Yes", "No"};
		String [] choices = new String[facilities.size()];
		
		if(facilities.size() > 0)
		{
			for(int i = 0; i < facilities.size(); i++)
			{
				choices [i] = facilities.get(i).getFacilityName();
			}
			String selection = (String) JOptionPane.showInputDialog(
				null, "Would you like to remove a facility?", "Remove", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					if(selection != null)
					{
						for(int j = 0; j < choices.length; j++)
						{
							if(selection.equals(choices[j]))
							{
								String validation = (String) JOptionPane.showInputDialog(null, "Are you sure you want to remove this facility?", "Delete Menu", JOptionPane.QUESTION_MESSAGE,null, valids, valids[0]);
								if(validation.equals(valids[0]))
								{
									facilities.remove(j);
								}
								else
								{
									mainInterface();
								}
							}
						}
						pr.print("");
						fr.close();
						pr.close();
						fr = new FileWriter(filename, true);
						pr = new PrintWriter(fr);
						
						for(int i = 0; i < facilities.size(); i++)
						{
							temp = facilities.get(i).getDecommissionedUntilLocalDate();
							if(temp == null)
							{
								file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour();
								pr.print(file);
								pr.println();
							}
							else
							{
								file = facilities.get(i).getFacilityID() + "," + facilities.get(i).getFacilityName() + "," + facilities.get(i).getPricePerHour() + "," + facilities.get(i).getDecommissionedUntilLocalDate();
								pr.print(file);
								pr.println();
							}
						}
						fr.close();
						pr.close();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Returning to main menu");
						mainInterface();
					}
		
		}
		else
		JOptionPane.showMessageDialog(null, "There are no facilities to delete");
		mainInterface();
	}
}