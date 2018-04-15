import java.util.*;
import javax.swing.*;
import java.io.*;

public class BookingsAndAvailability
{
	public static void main(String[] args)
	{
		int inputNum;
		boolean quit = false;
		while (!quit){
			if (isAdmin) 
			{
				String[] adminOptions = {"Register a new user", "Add a new facility", "View Avaliblities", "View Bookings", 
										 "Remove a facility", "Make a booking", "Record Payments", "View Accounts"};
				inputNum = JOptionPane.showOptionDialog(null, "What would you like to do?", "Options", JOptionPane.YES_NO_CANCEL_OPTION, 
														JOptionPane.QUESTION_MESSAGE, null, userOptions, "Quit");
				switch (inputNum)
				{
					case 0:
						// Register new user method
						break;
					
					case 1:
						// Add new facility method
						break;
					
					case 2:
						ViewFacilityAvailibilites();
						break;
					
					case 3:
						AdminViewBookings();
						break;
					
					case 4:
						// Remove a facility method
						break;
					
					case 5:
						Makebooking();
						break;
					
					case 6:
						// Record payments method
						break;
						
					case 7:
						// View account statements
						break;
					
					default:
						quit = true;
						break;
				}
			}
			else
			{
				String[] userOptions = {"View your Bookings", "View your statements"}
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
	public static void ViewFacilityAvailibilites()
	{
		
	}
	
	public static void Makebooking()
	{
		
	}
	
	public static void AdminViewBookings()
	{
		
	}
	
	public static void UserViewBookings()
	{
		
	}
}