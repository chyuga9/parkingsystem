package com.parkit.parkingsystem.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

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
	                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
	                break;
	            }
	            case BIKE: {
	                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
	                break;
	            }
	            default: throw new IllegalArgumentException("Unkown Parking Type");
	        }
        }
    }
}