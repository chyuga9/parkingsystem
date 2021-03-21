package com.parkit.parkingsystem.integration.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Matchers.anyInt;


import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
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
	
	//je dois trouver un moyen de faire readSelection malgré le fait qu'un inputReaderUtil est instancié dans la méthode originale
	@Disabled
	@Test
	public void loadInterfaceAndSelect1Test() throws Exception {
		// faire la meme chose pour tous les cas, as besoin d'assert
		//PowerMockito.mockStatic(InputReaderUtil.class);
		when(inputReaderUtil.readSelection()).thenReturn(1);
		interactiveShell.loadInterface();
		verify(parkingService, times(1)).processIncomingVehicle();
	}
	
	@Disabled
	@Test
	public void loadInterfaceAndSelect2Test() throws Exception {
   		when(inputReaderUtil.readSelection()).thenReturn(2);
		interactiveShell.loadInterface();
		verify(parkingService, times(1)).processExitingVehicle();	
	}
	
	@Disabled
	@Test
	public void loadInterfaceAndSelect3Test() throws Exception {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		try {
			when(inputReaderUtil.readSelection()).thenReturn(3);
    		} catch (Exception e) {
			e.printStackTrace();
		}
		interactiveShell.loadInterface();
		verify(out).println(startsWith("Exiting from the system!"));
	}
	@Disabled
	@Test
	public void loadInterfaceAndSelectAnotherNumberTest() throws Exception {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		try {
			when(inputReaderUtil.readSelection()).thenReturn(anyInt());
    		} catch (Exception e) {
			e.printStackTrace();
		}
		interactiveShell.loadInterface();
		verify(out).println(startsWith("Unsupported option. Please enter a number corresponding to "));
	}

	@Test
	public void loadMenuTest() {
		PrintStream out = mock(PrintStream.class);
		System.setOut(out);
		interactiveShell.loadMenu();
		verify(out).println(startsWith("Please select an option. Simply enter the number to choose an action"));
	}
	

}
