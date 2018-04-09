/**
 * Booking
 */
public class Booking
 {
	private int bookingID;
	private int facilityID;
	private int userID;
	private Date bookingDate;
	private int slotNumber;
	private boolean paymentStatus;

	booking(int bookingID, int facilityID, int userID, Date bookingDate, int slotNumber, boolean paymentStatus)
	{
		this.bookingID = bookingID;
		this.facilityID = facilityID;
		this.userID = userID;
		this.bookingDate = bookingDate;
		this.slotNumber = slotNumber;
		this.paymentStatus = paymentStatus;
	}
	
	public void setBookingID(int bookingID)
	{
		this.bookingID = bookingID;
	}
	
	public int getBookingID()
	{
		return bookingID;
	}
	
	public void setFacilityID(int facilityID)
	{
		this.facilityID = facilityID;
	}
	
	public int getFacilityID()
	{
		return facilityID;
	}
	
	public void setUserID(int userID)
	{
		this.userID = userID;
	}
	
	public int getUserID()
	{
		return userID;
	}
	
	public void setBookingDate(Date bookingDate)
	{
		this.bookingDate = bookingDate;
	}
	
	public Date getBookingDate()
	{
		return bookingDate;
	}
	
	public void setSlotNumber(int slotNumber)
	{
		this.slotNumber = slotNumber;
	}
	
	public int getSlotNumber()
	{
		return slotNumber;
	}
	
	public void setPaymentStatus(boolean paymentStatus)
	{
		this.paymentStatus = paymentStatus;
	}
	
	public boolean getPaymentStatus()
	{
		return paymentStatus;
	}
}