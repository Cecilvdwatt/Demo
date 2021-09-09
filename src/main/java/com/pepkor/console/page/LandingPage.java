package com.pepkor.console.page;

public class LandingPage extends AbstractPage {
	
	
	
	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n------------------------------------------------\n"
				+ "WELCOME TO DEMO APP!\n"
				+ "------------------------------------------------\n"
				+ "What do you want to do? \n"
				+ "1 : View Projects \n"
				+ "2 : View Users \n"
				+ "3 : Exit \n"
				+ "\n------------------------------------------------\n");
		
		if(!displayText.isEmpty())
		{
			sb.append("\n\n").append(displayText).append("\n\n");
		}
		
		return sb.toString();
	}

	@Override
	public iConsolePage processInput(String input) {
		
		displayText = "";
		
		int intInput;
		try
		{
			intInput = Integer.parseInt(input.strip());
		}
		catch(Exception e)
		{
			displayText = "Invalid Input: " + input;
			return this;
		}
		
		if(intInput < 1 || intInput > 3)
		{
			displayText = "Please specify a number between 1 and 3";
			return this;
		}
		else if(intInput == 1)
		{
			return new ProjectListPage(this);
		}
		else if(intInput == 2)
		{
			return new UserListPage(this);
		}
		else //if(intInput == 3) we'll log a error is we don't give is a number between 1 and 4
		{
			return null;
		}
	}

	@Override
	public void refresh() {
		// nothing needing refreshing
		
	}

}
