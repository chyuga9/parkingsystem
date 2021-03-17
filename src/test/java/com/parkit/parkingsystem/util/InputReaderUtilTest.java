package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

	private static InputReaderUtil inputReaderUtil = new InputReaderUtil();
	private static FareCalculatorService fare = new FareCalculatorService();

	@Mock
	UserInput userInput;
	@Disabled
	@Test
	public void readSelectionTest() {
		// InputStream input = new InputStream();
		when(userInput.readNumInput()).thenReturn(1);
		int result = inputReaderUtil.readSelection();
		assertEquals(1, result);
	}
	@Disabled
	@Test
	public void shouldTakeUserInput() throws Exception {
		when(userInput.readInput()).thenReturn("input");
		String regNum = inputReaderUtil.readVehicleRegistrationNumber();
		assertEquals("input", regNum);
	}
	@Disabled
	@Test
	public void test() {
		
		//assertEquals("car",fare.test(ParkingType.BIKE));
		//assertThrows(IllegalArgumentException.class, () -> fare.test(ParkingType.BIKE));
	}
	
}
