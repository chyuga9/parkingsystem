package com.parkit.parkingsystem.integration.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.startsWith;


import java.io.PrintStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.UserInput;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

	private static InteractiveShell interactiveShell = new InteractiveShell();

	@Mock
	UserInput userInput;
	
	@Mock
	InputReaderUtil inputReaderUtil;

	@Mock
	ParkingService parkingService;

	@Mock
	ParkingSpotDAO parkingSpotDAO;

	@Mock
	TicketDAO ticketDAO;
	
	@DisplayName("The menu is loaded")
	@Test
	public void loadMenuTest() {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		interactiveShell.loadMenu();
		verify(out).println(startsWith("Please select an option. Simply enter the number to choose an action"));
	}
	

}
