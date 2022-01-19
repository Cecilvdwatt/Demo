package com.pk.service;

import java.sql.SQLException;

import com.pk.db.StatusEnum;
import com.pk.db.dao.UserDAO;
import com.pk.db.dao.WorkItemDAO;
import com.pk.db.entity.Project;
import com.pk.db.entity.User;
import com.pk.db.entity.WorkItem;

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
	
	public static void assignUser(WorkItem toAssignTo, String user) throws Exception
	{
		User toFind = UserService.broadSearch(user);
		
		if(toFind == null)
		{
			throw new SQLException("Could not Find User");
		}
		else 
		{
			// If we're going to assign a work item that is already in the doings status to a user
			// we need to make sure that user doesn't already have 3 doing items assigned to him
			if(	toAssignTo.getStatus().equals(StatusEnum.DOING.name()) &&
				new WorkItemDAO().countByStatusForUser(toFind.getId(), StatusEnum.DOING.name()) == 3)
			{
				throw new Exception("Maximum number of tasks in doing status for associated user");
			}
			
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
	
	public static WorkItem add(String name, String description, User user, String project) throws Exception
	{
		Project toFind = ProjectService.broadSearch(project);
		
		if(toFind == null)
		{
			throw new Exception("Could not find the specified project!");
		}
		else
		{
			// All new Work Items have a todo status, so we don't need to check if the DOING
			// limit has been hit here since it can't be
			return new WorkItemDAO().add(new WorkItem(name, description, user, toFind));
		}
	}
	
	public static WorkItem add(String name, String description, Project project)
	{
		return new WorkItemDAO().add(name, description, project);
	}
	
	public static void progressStatus(WorkItem toProg) throws Exception
	{
		StatusEnum currentStatus = StatusEnum.valueOf(toProg.getStatus());
		
		if(currentStatus == StatusEnum.TODO)
		{
			// A user can only have 3 tasks in a doing state so we need to check before progressing
			// Work Item is eager to fetch but User is lazy to fetch so there shouldn't be a
			// needless database hit here
			
			if(toProg.getUser() != null)
			{
				if(new WorkItemDAO().countByStatusForUser(toProg.getUser().getId(), StatusEnum.DOING.name()) == 3)
				{
					throw new Exception("Maximum number of tasks in doing status for associated user");
				}
			}
			
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
