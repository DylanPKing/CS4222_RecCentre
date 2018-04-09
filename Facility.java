/**
 * Facility
 */
public class Facility
{
	private int facilityID;
	private String facilityName;
	private double pricePerHour;
	//private Date decommissionedUntilDate;
	
	Facility(int facilityID, String facilityName, double pricePerHour)
	{
		this.facilityID = facilityID;
		this.facilityName = facilityName;
		this.pricePerHour = pricePerHour;
	}
	
	Facility(int facilityID, String facilityName, double pricePerHour, /*Date decommissionedUntilDate*/)
	{
		this.facilityID = facilityID;
		this.facilityName = facilityName;
		this.pricePerHour = pricePerHour;
		/*this.decommissionedUntilDate = decommissionedUntilDate;*/
	}
	
	public void setFacilityID(int facilityID)
	{
		this.facilityID = facilityID;
	}
	
	public int getFacilityID()
	{
		return facilityID;
	}
	
	public void setFacilityName(String facilityName)
	{
		this.facilityName = facilityName;
	}
	
	public String getFacilityName()
	{
		return facilityName;
	}
	
	public void setPricePerHour(double pricePerHour)
	{
		this.pricePerHour = pricePerHour;
	}
	
	public double getPricePerHour()
	{
		return pricePerHour;
	}
	/*
	public void setDecommissionedUntilDate(Date decommissionedUntilDate)
	{
		this.decommissionedUntilDate = decommissionedUntilDate;
	}
	
	public Date getDecommissionedUntilDate()
	{
		return decommissionedUntilDate;
	}
	*/
}