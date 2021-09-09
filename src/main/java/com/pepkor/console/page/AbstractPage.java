package com.pepkor.console.page;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPage implements iConsolePage  {
	
	protected String displayText = "";
	protected iConsolePage parent = null;
	
	
	public AbstractPage() {}

	public AbstractPage(iConsolePage parent)
	{
		this.parent = parent;
	}	
	
	
	protected long getCheckID(List<String> split)
	{
		if(split.size() < 2)
		{
			displayText = "ERROR: Please provide a user id";
			return -1;
		}
		
		Long id;
		try
		{
			id = Long.parseLong(split.get(1));
		}
		catch(Exception e)
		{
			displayText = "ERROR: Please provide a numeric id";
			return -1;
		}
		
		return id;
	}
	
	protected List<String> tokenizeInput(String input)
	{
		List<String> toReturn = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
		while (m.find())
		{
			toReturn.add(m.group(1).replace("\"", ""));
		}
		
		return toReturn;
	}
	
	
}
