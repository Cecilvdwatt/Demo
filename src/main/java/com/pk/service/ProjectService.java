package com.pk.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pk.db.dao.ProjectDAO;
import com.pk.db.dao.WorkItemDAO;
import com.pk.db.entity.Project;
import com.pk.db.entity.WorkItem;

public class ProjectService {

	public static Project add(String name, String description)
	{
		return new ProjectDAO().add(new Project(name, description));
	}
	
	public static Project add(Project toPersist)
	{
		return new ProjectDAO().add(toPersist);
	}
	
	public static void update(Project toPersist)
	{
		new ProjectDAO().update(toPersist);
	}
	
	public static Project get(long id) throws SQLException
	{
		return new ProjectDAO().get(id);
	}
	
	public static void delete(long id) throws SQLException
	{
		// we're just checking if it exists so we can throw a meaningful error message here
		Project exists = new ProjectDAO().getLazyByID(id);
		
		if(exists == null)
		{
			throw new SQLException("Could not find the project that matched id: " + id);
		}
		else 
		{
			new ProjectDAO().delete(exists);
		}
	}
	
	public static Project broadSearch(String toFind)
	{
		Project toReturn = null;
		
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
			toReturn = new ProjectDAO().getLazyByID(id);
		}
		
		if(toReturn == null)
		{
			toReturn = new ProjectDAO().getByName(toFind);
		}
		
		return toReturn;
	}
	
	public static List<Project> getAllProjects()
	{
		return new ProjectDAO().getAll();	
	}
	
	public static List<Project> getProjectsAt(int group)
	{
		int startRow = 0;
		if(group > 0)
		{
			// so 0 based index, that means 
			// group 0 is 0 - 9
			// group 1 is 10 - 19
			
			startRow = group * 10;
		}
		
				
		return new ProjectDAO().getRange(startRow,10);
	}
	
	
	
}
