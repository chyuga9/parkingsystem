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

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = inHour.until(outHour,ChronoUnit.HOURS);

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