package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@Mock
	private static ParkingService mockParkingService;

	@Mock
	private static Ticket ticket1;

	@Mock
	private static ParkingSpot parkingSpot;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}
	
	@DisplayName("The ticket is saved in DB")
	@Test
	public void testTicketIsRegistered() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		String immatriculation = null;
		Connection con = null;
		try {
			con = dataBaseTestConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET_TEST);
			ps.setString(1, "ABCDEF");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				immatriculation = rs.getString(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals("ABCDEF", immatriculation);

	}
	@DisplayName("The parking table is updated after a car getting in")
	@Test
	public void testCarParkingSpotIsUnavailable() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		int numberSpotAvailable = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertEquals(2, numberSpotAvailable);
	}

	@DisplayName("The parking table is updated after a bike getting in")
	@Test
	public void testBikeParkingSpotIsUnavailable() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(2);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		int numberSpotAvailable = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
		assertEquals(5, numberSpotAvailable);
	}

	@DisplayName("The parking table is updated a vehicle leaving")
	@Test
	public void testParkingSpotIsAvailable() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Thread.sleep(500);
		int firstSpotAvailableAfterCarComing = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		parkingService.processExitingVehicle();
		int firstSpotAvailableAfterCarExiting = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertEquals(2, firstSpotAvailableAfterCarComing);
		assertEquals(1, firstSpotAvailableAfterCarExiting);
	}
}
