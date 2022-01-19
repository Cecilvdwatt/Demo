package com.pk.service;

import java.sql.SQLException;
import java.util.List;

import com.pk.db.dao.UserDAO;
import com.pk.db.entity.User;

public class UserService {

	
	public static User get(long id) throws SQLException
	{
		return new UserDAO().get(id);
	}
	
	public static boolean delete(long id)
	{
		return new UserDAO().delete(id);
	}
	
	public static List<User> getAll()
	{
		return new UserDAO().getAll();
	}
	
	public static User add(String name, String email)
	{
		User toAdd = new User(name, email);
		return new UserDAO().add(toAdd);
	}
	
	public static void update(User toUpdate)
	{
		new UserDAO().update(toUpdate);
	}
	
	public static User broadSearch(String toFind)
	{
		// bit of a messy method, but it helps to simplify things on the UI a bit
		// we ask the user for parameters and it might be difficult for the user to
		// remember the name or id 
		User toReturn = null;
		
		long id;
		
		try
		{
			id = Long.parseLong(toFind);
		}
		catch(Exception e) 
		{
			id = -1;
		}
		
		if(id != -1)
		{
			toReturn = new UserDAO().getLazy(id);
		}
		
		if(toReturn == null)
		{
			toReturn = new UserDAO().getByName(toFind);
		}
		
		return toReturn;
	}
	
	public static List<User> getProjectsAt(int group)
	{
		int startRow = 0;
		if(group > 0)
		{
			// so 0 based index, that means 
			// group 0 is 0 - 9
			// group 1 is 10 - 19
			
			startRow = group * 10;
		}
		
				
		return new UserDAO().getRange(startRow,10);
	}
	
	public static boolean updateName(long id, String newName)
	{
		return new UserDAO().updateName(id, newName);
	}
	
	public static boolean updateEmail(long id, String newEmail)
	{
		return new UserDAO().updateEmail(id, newEmail);
	}
	
	public static List<User> getUserAt(int group)
	{
		int startRow = 0;
		if(group > 0)
		{
			// so 0 based index, that means 
			// group 0 is 0 - 9
			// group 1 is 10 - 19
			
			startRow = group * 10;
		}
		
				
		return new UserDAO().getRange(startRow,10);
	}
}
