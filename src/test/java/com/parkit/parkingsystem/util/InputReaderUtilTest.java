package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {
	
	private static InputReaderUtil inputReaderUtil;
	
	@Disabled
	@Test
	public void readSelectionTest() {
		//InputStream input = new InputStream();
		Scanner scan = Mockito.mock(Scanner.class);
		when(scan.nextLine()).thenReturn("1");
		inputReaderUtil = new InputReaderUtil();
		int result = inputReaderUtil.readSelection();
		assertEquals(1,result);
	}
	@Disabled
	@Test
	public void shouldTakeUserInput() {
	    InputReaderUtil inputReader= new InputReaderUtil();

	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    Scanner scanner = new Scanner(in);
	}
}
