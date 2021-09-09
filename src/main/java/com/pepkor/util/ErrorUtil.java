package com.pepkor.util;

public class ErrorUtil {

	/*
	 * Method will navigate through the causal chain of the exception and extract the messages and return them 
	 * as a string
	 */
	public static String getErrorMsg(Throwable e)
	{
		String toReturn = e.getMessage();;
		
		if(e.getCause() != null)
		{
			toReturn += "\n" + getErrorMsg(e.getCause());
		}
		
		return toReturn;
		
	}
}
