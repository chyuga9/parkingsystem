package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private Instant outTime;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        
    }


    @Test
    public void calculateFareCar(){
        // Arrange
    	
    	Instant inTime = Instant.now().minusSeconds(60*60) ;
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        // Act
        fareCalculatorService.calculateFare(ticket);
        
        // Assert
        assertEquals(Fare.CAR_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    public void calculateFareBike(){
    	// Arrange
        Instant inTime = Instant.now().minusSeconds(60*60) ;
        outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        // Act
        fareCalculatorService.calculateFare(ticket);
        
        // Assert
        assertEquals(Fare.BIKE_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType(){
    	// Arrange
        Instant inTime = Instant.now().minusSeconds(60*60) ;
        outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        // Act / Assert ?
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        // Arrange
    	Instant inTime = Instant.now().plusSeconds(60*60) ;
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        // Arrange
    	Instant inTime = Instant.now().minusSeconds(45*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        // Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals(Math.round(0.75 * Fare.BIKE_RATE_PER_HOUR*100)/100.0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	// Arrange
    	Instant inTime = Instant.now().minusSeconds(45*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        // Act
        fareCalculatorService.calculateFare(ticket);
        // Assert
        assertEquals(Math.round(0.75 * Fare.CAR_RATE_PER_HOUR*100)/100.0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	// Arrange
    	Instant inTime = Instant.now().minusSeconds(24*60*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        // Act
        fareCalculatorService.calculateFare(ticket);
        // Assert
        assertEquals(Math.round(24 * Fare.CAR_RATE_PER_HOUR*100)/100.0, ticket.getPrice());
    }
    @Test
    public void calculateFareCarWithLessThanHalfHour() {
    	//Arrange
    	Instant inTime = Instant.now().minusSeconds(3*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        ticket.setParkingSpot(parkingSpot);
        // Act
        fareCalculatorService.calculateFare(ticket);
        // Assert
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithDiscountForRecurrentUserForAnHour() {
    	// Arrange
    	Instant inTime = Instant.now().minusSeconds(60*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
        // Act
        fareCalculatorService.calculateFare(ticket);
        // Assert
        assertEquals(Math.round( 0.95 * Fare.CAR_RATE_PER_HOUR * 100.0)/100.0, ticket.getPrice());
    }
    @Test
    public void calculateFareBikeWithDiscountForRecurrentUserForAnHour() {
    	// Arrange
    	Instant inTime = Instant.now().minusSeconds(60*60);
    	outTime = Instant.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);

        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);
        // Act
        fareCalculatorService.calculateFare(ticket);
        // Assert
        assertEquals(Math.round( 0.95 * Fare.BIKE_RATE_PER_HOUR * 100.0)/100.0, ticket.getPrice());
    }
}
