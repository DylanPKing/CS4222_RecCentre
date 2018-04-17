import java.util.*;			// Scanner
import javax.swing.*;		// JOptionPane   +   JTextArea
import java.io.*;			// IOException  +  Files
import java.time.LocalDate;


//Should the Admin options have just a view/edit a facility option and then have in that have them chose a facility 
//and then decide what to do specifically? 
//Then there is no repeating of code to checkwhat facility the user wants to edit/view in every method


public class BookingsAndAvailability
{
	public static ArrayList<Booking> bookings = new ArrayList<Booking>();
	public static ArrayList<Facility> facilities = new ArrayList<Facility>();
	public static ArrayList<User> users = new ArrayList<User>();
	public static boolean isAdmin;
	//public int loggedInID;

	public static void main(String[] args) throws IOException
	{
		fillArrayLists();

		//loggedInID = 2;

		int inputNum;
		boolean isAdmin = false;
		boolean quit = false;
		while (!quit){
			if (isAdmin) 
			{
				String[] adminOptions = {"Register a new user", "Add a new facility", "Edit or View a facility", 
										 "Record Payments", "View Accounts"};
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
						EditAndViewFacilities();
						break;
					
					case 3:
						// Record payments method
						break;
						
					case 4:
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
						UserViewBookings();
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

	public static void EditAndViewFacilities()
	{
		String[] facilitiesToEdit;
		for (int i = 0; i < facilities.size() - 1; i++)
		{
			facilitiesToEdit[i] = facilities.getFacilityName();

		}
	}

	public static void ViewFacilityAvailibilites()
	{
		/* TODO:
		 * - Ask which facility to check the avalibility for (List? or regular input and then check if it exists or is decommissioned)
		 * - Take in 2 inputs a start date and an end date to check(inclusive)
		 * - Bubble sort the bookings ArrayList by date first?
		 * - Go through by the dates(index 3) and check the slot(index 4) use Nested for loops
		 * - If statements that if it isn't in the booking Array add it to a string and JTextArea?
		 */ 
	}
	
	public static void Makebooking()
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
	}
	
	public static void AdminViewBookings()
	{
		/* TODO:
		 * - JTextArea of all of the Bookings ArrayList?
		 */
	}
	
	public static void UserViewBookings()
	{
		/* TODO:
		 * - Get the user ID (again global variable?)
		 * - For Loop, read through the bookings ArrayList and check the userID for each one (index 2)
		 * - If it equals the userID logged in add it to a string and use JTextArea?
		 */
	}


//SZYMON'S CODE FOR TESTING

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
					//paymentStatus = checkIfPaid(test);						// Need a method for this boolean
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
}