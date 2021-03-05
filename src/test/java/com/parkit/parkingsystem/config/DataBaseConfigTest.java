package com.parkit.parkingsystem.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class DataBaseConfigTest {
	
	DataBaseConfig dataBaseConfig = new DataBaseConfig();
	Connection testedConnection;
	@Mock
	Connection mockCon;
	
	@Test
	public void getConnectionTest() throws ClassNotFoundException, SQLException {
		 testedConnection = dataBaseConfig.getConnection();
		//Connection expectedConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/prod?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
		/*PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		verify(out).println(startsWith("Connexion à la database réussie"));
		*/
		//assertEquals(expectedConnection.getCatalog(),testedConnection.getCatalog());
		assertEquals("prod",testedConnection.getCatalog());
	}
	@Disabled
	@Test
	public void closeConnectionTest() throws SQLException, ClassNotFoundException {
		testedConnection = dataBaseConfig.getConnection();
		dataBaseConfig.closeConnection(testedConnection);
		assertEquals("prfd",testedConnection.getCatalog());
	}
}
