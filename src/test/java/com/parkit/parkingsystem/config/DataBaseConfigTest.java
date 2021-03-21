package com.parkit.parkingsystem.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class DataBaseConfigTest {
	
	DataBaseConfig dataBaseConfig = new DataBaseConfig();
	Connection testedConnection;
	@Mock
	Connection mockCon;
	
	@DisplayName("Check that the connection with the Database")
	@Test
	public void getConnectionTest() throws ClassNotFoundException, SQLException {
		 testedConnection = dataBaseConfig.getConnection();
		 assertEquals("prod",testedConnection.getCatalog());
	}
}
