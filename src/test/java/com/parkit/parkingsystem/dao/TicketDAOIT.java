package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.internal.matchers.Contains;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

@ExtendWith(MockitoExtension.class)
public class TicketDAOIT {

	private static TicketDAO ticketDAO;
	private static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static DataBasePrepareService dataBasePrepareService;
	private static Ticket ticket;

	@Mock
	Ticket mockTicket;
	@Mock
	ParkingSpot parkingSpot;
	@Mock
	DataBaseTestConfig mockDataBaseTestConfig;
	@Mock
	TicketDAO mockTicketDAO;

	@BeforeAll
	private static void setUp() throws Exception {
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	public void setUpPerTest() {
		dataBasePrepareService.clearDataBaseEntries();
	}

	@Test // FONCTIONNE
	public void saveTicketTest() {
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		when(mockTicket.getParkingSpot()).thenReturn(parkingSpot);
		when(mockTicket.getParkingSpot().getId()).thenReturn(1);
		when(mockTicket.getVehicleRegNumber()).thenReturn("test1234");
		when(mockTicket.getInTime()).thenReturn(Instant.now());
		ticketDAO.saveTicket(mockTicket);
		String immatriculation = null;
		Connection con = null;
		try {
			con = dataBaseTestConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET_TEST);
			ps.setString(1, "test1234");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				immatriculation = rs.getString(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals("test1234", immatriculation);
	}

	@Test // FONCTIONNE
	public void updateTicketTest() {
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticket = new Ticket(parkingSpot, "4321test", Instant.now().minusSeconds(60 * 60));
		when(parkingSpot.getId()).thenReturn(1);
		ticketDAO.saveTicket(ticket);
		ticket.setOutTime(Instant.now());
		ticket.setPrice(1.5);
		ticket.setId(1);
		ticketDAO.updateTicket(ticket);
		double price = 0;
		Connection con = null;
		try {
			con = dataBaseTestConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET_TEST);
			ps.setString(1, "4321test");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				price = rs.getDouble(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(1.5, price);
	}

	@Test // FONCTIONNE
	public void getTicketTest() {
		ticket = new Ticket(parkingSpot, "test12", Instant.now().minusSeconds(60 * 60));
		Ticket ticket2 = new Ticket(parkingSpot, "test12", 1.5, Instant.now().minusSeconds(60 * 60), Instant.now(),
				false);
		when(parkingSpot.getId()).thenReturn(1);
		ticketDAO.saveTicket(ticket);
		ticket.setOutTime(Instant.now());
		ticket.setPrice(1.5);
		ticket.setId(1);
		ticketDAO.updateTicket(ticket);
		Ticket ticket3 = ticketDAO.getTicket("test12");
		assertEquals(ticket2.getPrice(), ticket3.getPrice());
	}
	@Test // FONCTIONNE
	public void isRecurringTest() throws Exception {
		ticket = new Ticket(parkingSpot, "test876", Instant.now().minusSeconds(60 * 60));
		Ticket ticket2 = new Ticket(parkingSpot, "test876", 1.5, Instant.now().minusSeconds(49 * 60 * 60),Instant.now().minusSeconds(48 * 60 * 60),false);
		when(parkingSpot.getId()).thenReturn(1);
		ticketDAO.saveTicket(ticket2);
		ticket2.setOutTime(Instant.now().minusSeconds(48 * 60 * 60));
		ticket2.setPrice(1.5);
		ticket2.setId(1);
		ticketDAO.updateTicket(ticket2);
		ticketDAO.isRecurringUser(ticket);
		assertEquals(true, ticket.isRecurringUser());

	}
	@Disabled
	@Test  // NE FONCTIONNE PAS
	public void errorWhenSavingTicket() throws ClassNotFoundException, SQLException {
		ParkingSpot parkingSpot2 = new ParkingSpot(1,ParkingType.TEST,false);
		//DataBaseConfig mockDBC = mock(DataBaseConfig.class);
		//when(mockDBC.getConnection()).thenReturn(null);
		//ticket = new Ticket(parkingSpot2, "test234", null);
		//ticket = new Ticket(parkingSpot2, "test234", Instant.now().minusSeconds(60 * 60));
		assertThrows(Exception.class,() -> ticketDAO.saveTicket(ticket));
	}
	@Disabled
	@Test // NE FONCTIONNE PAS
	public void errorWhenGettingTicket() {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		ticketDAO.getTicket("fdlskfml");
		verify(out).println(Mockito.contains("The vehicule with the number "));
	}
}
