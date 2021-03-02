package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {
	
	private static InputReaderUtil inputReaderUtil;
	
	@Disabled
	@Test
	public void readSelectionTest() {
		System.out.print(1);
		int result = inputReaderUtil.readSelection();
		assertEquals(1,result);
	}
}
