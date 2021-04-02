package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {
	// change visibility (before was private) for the test
	static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public boolean saveTicket(Ticket ticket) throws Exception {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setTimestamp(3, Timestamp.from(ticket.getInTime()));
			ps.setNull(4, 0);
			ps.setBoolean(5, ticket.isRecurringUser());
			return ps.execute();
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
			throw new Exception(ex);
			} finally {
			dataBaseConfig.closeConnection(con);
		}
	}

	public Ticket getTicket(String vehicleRegNumber){
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(7)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4).toInstant());
				ticket.setRecurringUser(rs.getBoolean(6));
			} else {
				logger.error(
						"The vehicule with the number indicated wasn't found. Please check your vehicule number and try again.");
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);

		} catch (Exception ex) {
			logger.error("Error getting ticket", ex);
		}
		return ticket;
	}

	public boolean updateTicket(Ticket ticket) throws Exception {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, Timestamp.from(ticket.getOutTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
			throw new Exception(ex);

		} finally {
			dataBaseConfig.closeConnection(con);
		}
	}

	public boolean isRecurringUser(Ticket ticket) throws Exception {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.RECURRING_USER);
			ps.setString(1, ticket.getVehicleRegNumber());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket.setRecurringUser(true);
			}
		} catch (Exception ex) {
			logger.error("Error seeking if it's a recurring user", ex);
		}
		return ticket.isRecurringUser();
	}
}