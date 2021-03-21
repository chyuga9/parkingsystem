package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.UserInput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;
	
	@Mock
	private static UserInput userInput;
	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;
	@Mock
	private static Ticket ticket;
	@Mock
	private static ParkingSpot parkingSpot;
	
	@BeforeEach
	private void setUpPerTest() {
		try {
			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@DisplayName("A vehicle can exit correctly")
	@Test
	public void processExitingVehicleTest() throws Exception {
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket(parkingSpot, "ABCDEF", Instant.now().minusSeconds(60 * 60));
			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");

		}
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
	}

	@DisplayName("A car enters the parking and the methods getNextAvailableSlot, updateParking and saveTicket are launched")
	@Test 
	public void processIncomingCarTest() throws Exception {
		try {
			when(inputReaderUtil.readSelection()).thenReturn(1);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
			when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
			parkingService.processIncomingVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");

		}
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.CAR);
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
	}

	@DisplayName("A bike enters the parking and the methods getNextAvailableSlot, updateParking and saveTicket are launched")
	@Test
	public void processIncomingBikeTest() throws Exception {
		try {
			when(inputReaderUtil.readSelection()).thenReturn(2);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);
			when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
			parkingService.processIncomingVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");

		}
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.BIKE);
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
	}

	@DisplayName("Throw an exception when the input for vehicle isn't good")
	@Test 
	public void getErrorOnProcessIncomingUnknownTypeVehiculeTest() {		 
		when(inputReaderUtil.readSelection()).thenReturn(341);
		assertThrows(IllegalArgumentException.class, ()-> parkingService.getVehicleType());
	}

	@DisplayName("When it's a recurring user, a personnalized message is displayed")
	@Test
	public void processIncomingRecurringVehiculeTest() {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		try{
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(ticketDAO.isRecurringUser(any(Ticket.class))).thenReturn(true);
		parkingService.processIncomingVehicle();
		}catch(Exception e) {
			e.printStackTrace();
		}
		verify(out).println(startsWith("Welcome back!"));
	}
	
	@DisplayName("A error message is displayed when the Vehicle Registration number isn't right")
	@Test 
	public void getErrorOnExit() throws Exception {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,true);
		Ticket ticket1 = new Ticket(parkingSpot,"TestE2E",Instant.now().minusSeconds(90*60));
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("BTDGFH");
		when(ticketDAO.getTicket("BTDGFH")).thenReturn(ticket1);
		when(ticketDAO.updateTicket(ticket1)).thenReturn(false);
		parkingService.processExitingVehicle();
		verify(out).println(startsWith("Unable to update ticket "));
	}
	
	@DisplayName("Throw an exception when no parking slot is available")
	@Test 
	public void getErrorOnNextSlotAvailable() throws Exception {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);
		assertThrows(Exception.class, () -> parkingService.getNextParkingNumberIfAvailable());
	}
}
