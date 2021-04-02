package com.parkit.parkingsystem.constants;

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER,IN_TIME, OUT_TIME, RECURRING_VEHICLE) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, t.RECURRING_VEHICLE, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
    public static final String GET_TICKET_TEST = "select t.VEHICLE_REG_NUMBER, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
    public static final String UPDATE_TICKET_TEST = "select t.PRICE, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    public static final String UPDATE_IN_TIME_TICKET_TEST = "update ticket set IN_TIME=? where ID=?";
    
    public static final String RECURRING_USER = "select * from ticket where VEHICLE_REG_NUMBER = ?";
    
    public static final String PARKING_AVAILABILITY = "select * from parking where AVAILABLE = false and TYPE = ?";
}
