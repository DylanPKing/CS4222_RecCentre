/*
 * Dylan King			17197813
 * Szymon Sztyrmer		17200296
 * Louise Madden		17198232
 * Brian Malone			17198178
 */

import java.time.*;

/**
 * Facility
 */
public class Facility
{
	private int facilityID;
	private String facilityName;
	private double pricePerHour;
	private LocalDate decommissionedUntilDate;
	
	Facility(int facilityID, String facilityName, double pricePerHour)
	{
		this.facilityID = facilityID;
		this.facilityName = facilityName;
		this.pricePerHour = pricePerHour;
	}
	
	Facility(int facilityID, String facilityName, double pricePerHour, LocalDate decommissionedUntilDate)
	{
		this.facilityID = facilityID;
		this.facilityName = facilityName;
		this.pricePerHour = pricePerHour;
		this.decommissionedUntilDate = decommissionedUntilDate;
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
	
	public void setDecommissionedUntilLocalDate(LocalDate decommissionedUntilDate)
	{
		this.decommissionedUntilDate = decommissionedUntilDate;
	}
	
	public LocalDate getDecommissionedUntilLocalDate()
	{
		return decommissionedUntilDate;
	}
}