package com.parkit.parkingsystem.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.UserInput;

@ExtendWith(MockitoExtension.class)
public class ParkingSystem {

	public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	public static ParkingSpotDAO parkingSpotDAO;
	public static TicketDAO ticketDAO;
	public static ParkingService parkingService;
	public static DataBasePrepareService dataBasePrepareService;
	public static InteractiveShell interactiveShell;
	public static Ticket ticket;
	public static ParkingSpot parkingSpot;

	@Mock
	public static InputReaderUtil inputReaderUtil;
	@Mock
	public static Ticket mockTicket;
	@Mock
	public static ParkingSpot mockParkingSpot;
	@Mock
	public static TicketDAO mockTicketDAO;
	@Mock
	public static UserInput userInput;

	@BeforeAll
	private static void setUp() {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		dataBasePrepareService.clearDataBaseEntries();
		interactiveShell = new InteractiveShell();
	}

	@DisplayName("The user parks his car less than 30 minutes and leaves for free")
	@Test // les mocks ne sont pas pris en compte avec loadInterface()
	public void userWantsToParkHisCarAndLeaveBeforeThirtyMinutes() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		//interactiveShell.loadInterface();
		parkingService1.processIncomingVehicle();
		Thread.sleep(1000);
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(0, price);
	}

	@DisplayName("The user parks his bike less than 30 minutes and leaves for free")
	@Test
	public void userWantsToParkHisBikeAndLeaveBeforeThirtyMinutes() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		//interactiveShell.loadInterface();
		parkingService1.processIncomingVehicle();
		Thread.sleep(1000);
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(0, price);
	}
	
	@DisplayName("The user parks his car 90 minutes and pays 2.25€ when leaving")
	@Test
	public void userWantsToParkHisCarAndLeaveAfterThirtyMinutes() throws Exception {
		parkingSpot = new ParkingSpot(1,ParkingType.CAR,true);
		ticket = new Ticket(parkingSpot,"TestE2E",Instant.now().minusSeconds(90*60));
		ticketDAO.saveTicket(ticket);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(2.25, price,0.05);
	}
	
	@DisplayName("The user parks his bike 90 minutes and pays 1.5€ when leaving")
	@Test
	public void userWantsToParkHisBikeAndLeaveAfterThirtyMinutes() throws Exception {
		parkingSpot = new ParkingSpot(4,ParkingType.BIKE,true);
		ticket = new Ticket(parkingSpot,"TestE2E",Instant.now().minusSeconds(90*60));
		ticketDAO.saveTicket(ticket);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(1.5, price,0.05);
	}
}