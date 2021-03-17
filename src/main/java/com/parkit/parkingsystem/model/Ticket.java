package com.parkit.parkingsystem.model;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price = 0;
    private Instant inTime;
    private Instant outTime;
    private boolean recurringUser = false;
    

    public Ticket(ParkingSpot parkingSpot, String vehicleRegNumber, double price, Instant inTime, Instant outTime,
			boolean recurringUser) {
		super();
		this.parkingSpot = parkingSpot;
		this.vehicleRegNumber = vehicleRegNumber;
		this.price = price;
		this.inTime = inTime;
		this.outTime = outTime;
		this.recurringUser = recurringUser;
	}

	public Ticket( Instant inTime, Instant outTime) {
		super();
		this.inTime = inTime;
		this.outTime = outTime;
	}

	public Ticket(ParkingSpot parkingSpot, String vehicleRegNumber, Instant inTime) {
		super();
		this.parkingSpot = parkingSpot;
		this.vehicleRegNumber = vehicleRegNumber;
		this.inTime = inTime;
	}

	public Ticket() {
		
	}

	public boolean isRecurringUser() {
		return recurringUser;
	}

	public void setRecurringUser(boolean recurringUser) {
		this.recurringUser = recurringUser;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }
}
