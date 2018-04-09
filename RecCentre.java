/**
 * RecCentre
 */
import java.util.*;			
import javax.swing.*;		//JOptionPane   +   JTextArea
import java.io.*;				// IOException  +  Files
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

	public static void generatePassword()
	{
		
	}
	
	public static void fillArrayLists() throws IOException
	{
		File input1 = new File("Bookings.txt");
		int bookingID = 0;
		int facilityID = 0;
		int userID = 0;
		//Date bookingDate;
		int slotNumber = 0;
		boolean paymentStatus = false;
		
		File input2 = new File("Users.txt");
		int userId;
		String email;
		String password;
		int userType;
		
		File input3 = new File("Facilities.txt");
		int facilityId;
		String facilityName;
		double pricePerHour;
		//Date decommissionedUntilDate;
		
		Scanner in = new Scanner(input1);
		String [] lineFromFile;
		String test = "";
		if(input1.exists() && input2.exists() && input3.exists())
		{
			if(input1.length() != 0)
			{
				while(in.hasNext())
				{
					lineFromFile = (in.nextLine().split(","));
					bookingID = Integer.parseInt(lineFromFile[0]);
					facilityID = Integer.parseInt(lineFromFile[1]);
					userID = Integer.parseInt(lineFromFile[2]);
					//bookingDate =						figure this out later
					slotNumber = Integer.parseInt(lineFromFile[4]);
					test = lineFromFile[5];
					paymentStatus = checkIfPaid(test);
					Booking reservation = new Booking(bookingID, facilityID, userID, /*bookingDate,*/ slotNumber, paymentStatus);
					bookings.add(reservation);
				}
			}
			in.close();
			in = new Scanner(input2);
			if(input2.length() != 0)
			{
				while(in.hasNext())
				{
					lineFromFile = (in.nextLine().split(","));
					userId = Integer.parseInt(lineFromFile[0]);
					email = lineFromFile[1];
					password = lineFromFile[2];
					userType = Integer.parseInt(lineFromFile[3]);
					User newUser = new User(userId, email, password, userType);
					users.add(newUser);
				}
			}
			in.close();
			in = new Scanner(input3);
			if(input3.length() != 0)
			{
				while(in.hasNext())
				{
					lineFromFile = (in.nextLine().split(","));
					facilityId = Integer.parseInt(lineFromFile[0]);
					facilityName = lineFromFile[1];
					pricePerHour = Double.parseDouble(lineFromFile[2]);
					if(lineFromFile.length == 3)
					{
						Facility newFacility = new Facility(facilityId, facilityName, pricePerHour);
					}
					else
					{
						//decommissionedUntilDate = 				figure this out later
						Facility newFacility = new newFacility(facilityId, facilityName, pricePerHour, /*decommissionedUntilDate*/);
					}
					facilities.add(newFacility);
				}
			}
			in.close();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "One or more files don't exist", "Error", 2);
		}
	}
	
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