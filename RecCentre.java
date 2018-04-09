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
		if(login())
		{
			fillArrayLists();
		}
	}

	public static boolean login()
	{
		
	}

	public static void generatePassword()
	{
		
	}
	
	public static void fillArrayLists() throws IOException
	{
		File input1 = new File("Bookings.txt");
		File input2 = new File("Users.txt");
		File input3 = new File("Facilities.txt");
		Scanner in = new Scanner(input1);
		Booking tempBooking;
		User tempUser;
		Facility tempFacility;
		if(input1.exists() && input2.exists() && input3.exists())
		{
			if(input1.length() != 0)
			{
				while(in.hasNext())
				{
				//	tempBooking = in.nextLine();
					//bookings.add(tempBooking);
				}
			}
			in.close();
			in = new Scanner(input2);
			if(input2.length() != 0)
			{
				while(in.hasNext())
				{
					//tempUser = in.nextLine();
					//users.add(tempUser);
				}
			}
			in.close();
			in = new Scanner(input3);
			if(input3.length() != 0)
			{
				while(in.hasNext())
				{
				//	tempFacility = in.nextLine();
					//facilities.add(tempFacility);
				}
			}
			in.close();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "One or more files don't exist", "Error", 2);
		}
	}
}