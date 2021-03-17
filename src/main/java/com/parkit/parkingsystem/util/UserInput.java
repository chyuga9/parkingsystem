package com.parkit.parkingsystem.util;

import java.util.Scanner;

public class UserInput {

    private static Scanner scan = new Scanner(System.in);
    
    public String readInput() {
    	String input = scan.nextLine();
    	return input;
    }
    public int readNumInput() {
    	int input = Integer.parseInt(scan.nextLine());
    	return input;
    }
}
