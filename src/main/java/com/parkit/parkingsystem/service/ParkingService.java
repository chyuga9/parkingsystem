package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Date;

public class ParkingService {

	private static final Logger logger = LogManager.getLogger("ParkingService");

	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	private InputReaderUtil inputReaderUtil;
	private ParkingSpotDAO parkingSpotDAO;
	private TicketDAO ticketDAO;
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	public void processIncomingVehicle() throws Exception {
			ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
			if (parkingSpot != null && parkingSpot.getId() > 0) {
				String vehicleRegNumber = getVehicleRegNumber();
				parkingSpot.setAvailable(false);
				parkingSpotDAO.updateParking(parkingSpot);// allot this parking space and mark it's availability as
															// false
				Instant inTime = Instant.now();
				Ticket ticket = new Ticket(parkingSpot, vehicleRegNumber, inTime);	
				// Seeking if it's a recurring user
				if (ticketDAO.isRecurringUser(ticket)) {
					System.out.println(
							"Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
				}
				ticketDAO.saveTicket(ticket);
				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
				System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
			}
	}

	private String getVehicleRegNumber() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}

	public ParkingSpot getNextParkingNumberIfAvailable() throws Exception {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getVehicleType();
			parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
				parkingSpot.setId(parkingNumber);
				parkingSpot.setParkingType(parkingType);
				parkingSpot.setAvailable(true);
			} else {
				throw new Exception("Error fetching parking number from DB. Parking slots might be full");
			}
		} catch (Exception e) {
			logger.error("Error fetching next available parking slot");
			throw new Exception(e); 
		}
		return parkingSpot;
	}

	public ParkingType getVehicleType() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1: {
			return ParkingType.CAR;
		}
		case 2: {
			return ParkingType.BIKE;
		}
		default: {
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
		}
	}

	public void processExitingVehicle() throws Exception { 
			String vehicleRegNumber = getVehicleRegNumber();
			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			Instant outTime = Instant.now();
			ticket.setOutTime(outTime);
			fareCalculatorService.calculateFare(ticket);
			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);
				System.out.println("Please pay the parking fare:" + ticket.getPrice());
				System.out.println(
						"Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
		}
	}

