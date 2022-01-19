package com.pk.console;

import java.util.Scanner;

import com.pk.console.page.LandingPage;
import com.pk.console.page.iConsolePage;

public class PKConsole {

	private iConsolePage currentPage;
	
	public void run()
	{
		Scanner scanner = new Scanner(System.in);
		
		currentPage = new LandingPage();
		
		do
		{
			System.out.println(currentPage.getText());
			currentPage = currentPage.processInput(scanner.nextLine());
			
			
		}
		while(currentPage != null);
		
		System.out.println("Exiiting");
		
		scanner.close();
	}
	
}
