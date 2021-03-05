package com.parkit.parkingsystem.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

		public static NumberFormat df = new DecimalFormat("#.##");
		
		
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        Instant inHour = ticket.getInTime();
        Instant outHour = ticket.getOutTime();

        
        double duration = inHour.until(outHour,ChronoUnit.MINUTES);
        duration /=60;
        // If a car uses the parking less than half an hour, it's free
        if(duration <= 0.5) {
        		ticket.setPrice(0);
        }else {
	        switch (ticket.getParkingSpot().getParkingType()){
	            case CAR: {
	                // Using Math.round(fare * 100) / 100 to format number as "#.##"
	            	// If it's recurring user, there is 5% discount
	            	if(ticket.isRecurringUser()) {
	            		ticket.setPrice(Math.round(duration * Fare.CAR_RATE_PER_HOUR * 0.95 * 100.0)/100.0);
	            	}else {
	            		ticket.setPrice(Math.round(duration * Fare.CAR_RATE_PER_HOUR * 100.0)/100.0);
	            	}
	            	break;
	            }
	            case BIKE: {
	            	// Using Math.round(fare * 100) / 100 to format number as "#.##"
	            	// If it's recurring user, there is 5% discount
	            	if(ticket.isRecurringUser()) {
	            		ticket.setPrice(Math.round(duration * Fare.BIKE_RATE_PER_HOUR * 0.95 * 100.0)/100.0);
	            	}else {
	            		ticket.setPrice(Math.round(duration * Fare.BIKE_RATE_PER_HOUR * 100.0)/100.0);
	            	}
	            	break;
	            }
	            default: throw new IllegalArgumentException("Unkown Parking Type");
	        }
	        
        }
    }
}