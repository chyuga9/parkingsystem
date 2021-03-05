package com.parkit.parkingsystem.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingSystem {

	public static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	public static ParkingSpotDAO parkingSpotDAO;
	public static TicketDAO ticketDAO;
	public static ParkingService parkingService;
	public static DataBasePrepareService dataBasePrepareService;


	@Mock
	public static InputReaderUtil inputReaderUtil;
	@Mock
	public static Ticket mockTicket;
	@Mock
	public static ParkingSpot mockParkingSpot;
	@Mock
	TicketDAO mockTicketDAO;

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
	}

	@Test
	public void userWantsToParkHisCarAndLeaveBeforeThirtyMinutes() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService1.processIncomingVehicle();
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(0, price);
	}

	@Test
	public void userWantsToParkHisBikeAndLeaveBeforeThirtyMinutes() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService1.processIncomingVehicle();
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(0, price);
	}
	@Test
	public void userWantsToParkHisCarAndLeaveAfterThirtyMinutes() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("TestE2E");
		//Comment mettre le temps du ticket à Plus de 30 minutes
		ParkingService parkingService1 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService1.processIncomingVehicle();
		parkingService1.processExitingVehicle();
		double price = ticketDAO.getTicket("TestE2E").getPrice();
		assertEquals(0, price);
	}
}