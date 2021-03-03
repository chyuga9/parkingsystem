package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
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
	    private static void setUp() throws Exception{
	        ticketDAO = new TicketDAO();
	        ticketDAO.dataBaseConfig = dataBaseTestConfig;
	        dataBasePrepareService = new DataBasePrepareService();
	    }
	 
	 @BeforeEach
	 public void setUpPerTest() {
	        dataBasePrepareService.clearDataBaseEntries();
	 }
	 @Test
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
			ps.setString(1,"test1234");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            immatriculation = rs.getString(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        assertEquals("test1234",immatriculation);
		}
	 
	 @Disabled
	 @Test // je souhaite lancer une erreur à cause d'une connexion qui n'a pas plus s'établir mais il ne prend pas en compte mon mock
		public void noConnectionForTicketTest() {
	        parkingSpotDAO.dataBaseConfig = mockDataBaseTestConfig;	
		 try {
			when(mockDataBaseTestConfig.getConnection()).thenReturn(null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			ticketDAO.saveTicket(ticket);
			assertThrows(Exception.class, () -> ticketDAO.saveTicket(ticket));
	 }
	 
	 @Test
		public void updateTicketTest() {
	        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		 ticket = new Ticket(parkingSpot,"4321test",Instant.now().minusSeconds(60*60));
		 	ticket.getParkingSpot().setId(1);
			when(mockTicketDAO.saveTicket(ticket)).thenReturn(true);
			ticketDAO.saveTicket(ticket);
			//when(mockTicket.getPrice()).thenReturn(1.5);
			//when(mockTicket.getOutTime()).thenReturn(Instant.now());
			ticket.setOutTime(Instant.now());
			ticket.setPrice(1.5);
			//when(mockTicket.getId()).thenReturn(1);
			ticketDAO.updateTicket(ticket);
			double price = 0;
	    	Connection con = null;
			try {
				con = dataBaseTestConfig.getConnection();
				PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET_TEST);
				ps.setString(1,"4321test");
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()) {
	            price = rs.getInt(1);
	            } else {
	            	System.out.println("jai rien");
	            }
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        assertEquals(1.5,price);
		}
	 
	 @Disabled
	 @Test
		public void getTicketTest() {
			ticket = new Ticket(parkingSpot,"4321test",1.5,Instant.now().minusSeconds(60*60),Instant.now(),false);
			when(mockTicketDAO.saveTicket(ticket)).thenReturn(true);
			ticketDAO.getTicket("4321test");
			int moi = 23;
			assertEquals("test1234",moi);
		}
		
}
