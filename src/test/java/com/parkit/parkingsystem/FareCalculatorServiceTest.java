package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("The price is 1.5€ for a car parked 1 hour")
    @Test
    public void calculateFareCarForOneHour(){
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

    @DisplayName("The price is 1€ for a bike parked 1 hour")
    @Test
    public void calculateFareBikeForOneHour(){
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

    @DisplayName("Throw an error when the vehicle type isn't recognized")
    @Test
    public void getErrorWhenUnrecognizedTypeVehicle(){
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

    @DisplayName("Throw an error when the 'out time' is before the 'in time'")
    @Test
    public void calculateFareBikeWithInTimeAfterThanOutTime(){
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

    @DisplayName("The price is 0.75€ for a bike parked 1 hour")
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

    @DisplayName("The price is 1.13€ for a car parked 45 minutes")
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

    @DisplayName("The price is 36€ for a car parked 1 day")
    @Test
    public void calculateFareCarWithADayParkingTime(){
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
    
    @DisplayName("The parking is free for a vehicle that parks less than 30 minutes")
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
    
    @DisplayName("The price is 1.42€ for a recurrent car parked 1 hour")
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
    
    @DisplayName("The price is .95€ for a recurrent bike parked 1 hour")
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
    
    @DisplayName("Get the duration between 'out time' and 'in time'")
    @Test
    public void getDurationTest() {
    	ticket.setInTime(Instant.now().minusSeconds(60*60));
    	ticket.setOutTime(Instant.now());
    	double duration = fareCalculatorService.getDuration(ticket);
    	assertEquals(1,duration);
    }
    
    @DisplayName("Throw an exception when the ticket has a problem")
    @Test
	public void getErrorWhenTicketNotGood() {
		ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.TEST,true);
		Ticket ticket = new Ticket(parkingSpot,"TestE2E",Instant.now().minusSeconds(90*60));
		ticket.setOutTime(Instant.now());
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));

	}
}
