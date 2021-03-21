package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAOIT {

	static ParkingSpotDAO parkingSpotDAO;
	static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    static DataBasePrepareService dataBasePrepareService;
    ParkingSpot parkingSpot;
    
    @Mock
    ParkingSpot mockParkingSpot;
    @Mock
    DataBaseTestConfig mockDbc;
    
    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        dataBasePrepareService = new DataBasePrepareService();
    }
 
 @BeforeEach
 public void setUpPerTest() {
     parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService.clearDataBaseEntries();
 }
    
    @Test // FONCTIONNE
	public void getNextAvailableSlotTest() throws Exception {
		int spotNumber = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		assertEquals(1,spotNumber);
	}
    
    @Test // FONCTIONNE
	public void updateParkingTest() throws Exception {
    	
    	/* avec le mock il me dit dès la 1e instruction (ligne 54) que l'objet est null
    	 * when(mockParkingSpot.isAvailable()).thenReturn(false);
    	when(mockParkingSpot.getId()).thenReturn(1);
    	when(mockParkingSpot.getParkingType()).thenReturn(ParkingType.CAR);
    	*/
    	parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
		parkingSpotDAO.updateParking(parkingSpot);
    	Connection con = null;
    	int id = 0;
		try {
			con = dataBaseTestConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.PARKING_AVAILABILITY);
			ps.setString(1,ParkingType.CAR.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            id = rs.getInt(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertEquals(1,id);
    }
    
    @Test // FONCTIONNE
    public void getErrorOnGettingNextAvailableSpot() {
    	//Connection mockCon = mock(Connection.class);
    	DataBaseConfig mockDBC = mock(DataBaseConfig.class);
    	parkingSpotDAO.dataBaseConfig = mockDBC;
    	assertThrows(Exception.class, () -> parkingSpotDAO.getNextAvailableSlot(ParkingType.TEST));
    }
    @Test // FONCTIONNE
    public void getErrorOnUpdatingParking() {
    	//Connection mockCon = mock(Connection.class);
    	DataBaseConfig mockDBC = mock(DataBaseConfig.class);
    	parkingSpotDAO.dataBaseConfig = mockDBC;
    	assertThrows(Exception.class, () -> parkingSpotDAO.updateParking(mockParkingSpot));
    }
    
}
