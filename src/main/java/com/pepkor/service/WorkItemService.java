package com.pepkor.service;

import java.sql.SQLException;

import com.pepkor.db.StatusEnum;
import com.pepkor.db.dao.UserDAO;
import com.pepkor.db.dao.WorkItemDAO;
import com.pepkor.db.entity.Project;
import com.pepkor.db.entity.User;
import com.pepkor.db.entity.WorkItem;

public class WorkItemService {
	
	public static WorkItem get(long id)
	{
		return new WorkItemDAO().get(id);
	}
	public static void add(WorkItem toPersist)
	{
		new WorkItemDAO().add(toPersist);
	}
	
	public static void update(WorkItem toPersist)
	{
		new WorkItemDAO().update(toPersist);
	}
	
	public static void unassignUser(WorkItem toUnassign)
	{
		toUnassign.setUser(null);
		update(toUnassign);
	}
	
	public static void assignUser(WorkItem toAssignTo, String user) throws SQLException
	{
		User toFind = UserService.broadSearch(user);
		
		if(toFind == null)
		{
			throw new SQLException("Could not Find User");
		}
		else 
		{
			toAssignTo.setUser(toFind);
			update(toAssignTo);
		}
				
	}
	
	public static void unassignUser(long id)
	{
		new WorkItemDAO().unassign(id);
	}
	
	public static void delete(WorkItem todel)
	{
		new WorkItemDAO().delete(todel);
	}
	
	public static WorkItem add(String name, String description, String user, Project project) throws Exception
	{
		User toFind = UserService.broadSearch(user);
		
		if(toFind == null)
		{
			return add(name, description, project);
		}
		else
		{
			if(toFind.getWorkItems().size() < 3)
			{
				return new WorkItemDAO().add(new WorkItem(name, description,toFind, project));
			}
			else
			{
				throw new Exception("User Already has 3 Assigned Work Items");
			}
		}
	}
	
	public static WorkItem add(String name, String description, Project project)
	{
		return new WorkItemDAO().add(name, description, project);
	}
	
	public static void progressStatus(WorkItem toProg)
	{
		
		StatusEnum currentStatus = StatusEnum.valueOf(toProg.getStatus());
		
		if(currentStatus == StatusEnum.TODO)
		{
			toProg.setStatus(StatusEnum.DOING.name());
		}
		else if(currentStatus == StatusEnum.DOING)
		{
			toProg.setStatus(StatusEnum.DONE.name());
		}
		else if(currentStatus == StatusEnum.DONE)
		{
			toProg.setStatus(StatusEnum.TODO.name());
		}
		
		
		update(toProg);
		
	}
}
