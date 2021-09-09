package com.pepkor.console.page;

import java.sql.SQLException;
import java.util.List;

import com.pepkor.db.entity.User;
import com.pepkor.db.entity.WorkItem;
import com.pepkor.service.UserService;
import com.pepkor.util.ErrorUtil;

public class UserPage  extends AbstractPage  {

	User thisUser;
	
	public UserPage(iConsolePage parent, long id)
	{
		super(parent);
		try 
		{
			thisUser = UserService.get(id);
		} 
		catch (SQLException e) {
			displayText = "Could not load User. " + ErrorUtil.getErrorMsg(e) ;
		}
	}
	
	
	@Override
	public String getText() {
		StringBuilder sb = new StringBuilder();
				
		if(thisUser == null)
		{
			sb.append("Project Not Found!\n");
			
			sb.append("\n\nOptions:\n");
		}
		else
		{
			sb.append("\n------------------------------------------------\n"
					+ "USER DETAILS:"
					+ "\n------------------------------------------------\n"
					+ "User Name:        ").append(thisUser.getUserName()).append("\n"
					+ "User Description: ").append(thisUser.getEmail()).append("\n"
					+ "User Last Update: ").append(thisUser.getLastUpdate()).append("\n"
					+ "------------------------------------------------\n");
			
			if(thisUser.getWorkItems().size() == 0)
			{
				sb.append("\n\tNO WORK ITEMS");
			}
			else
			{
				for(WorkItem item : thisUser.getWorkItems())
				{
					sb.append("\n\tID: ").append(item.getId())
						.append(", Title:").append(item.getTitle())
						.append(", Status: ").append(item.getStatus())
						.append(", User: ");
					
					if(item.getUser() == null)
					{
						sb.append("UNASSIGNED");
					}
					else 
					{
						sb.append(item.getUser().getUserName());
					}
					
					sb.append(", Desc: ").append(item.getDescription());
						
				}
			}
			
			sb.append("\n\n------------------------------------------------\n");
			
			sb.append("\n\nOptions:\n");
			
			sb.append("Add Work Item:           A {{title}} {{description}} {{project}}\n");
			sb.append("Delete Work Item:        D {{work item id}}\n");
			sb.append("Update Work Item Status: US {{work item id}}\n");
			sb.append("Unassign Work Item:      UN {{work item id}}\n");

		
		}
		
		sb.append("Back:                   B\n"
				+ "Exit:                   E\n");
		
		if(!displayText.isEmpty())
		{
			sb.append("\n\n").append(displayText).append("\n\n");
		}
		
		
		return sb.toString();	
	}

	@Override
	public iConsolePage processInput(String input) {
		displayText = "";
		
		if(input.isEmpty())
		{
			displayText = "ERROR: No input give";
			return this;
		}
			
		List<String> splited = tokenizeInput(input);
		
		switch(splited.get(0).toUpperCase())
		{
			case "A":
			{
				if(splited.size() < 4)
				{
					displayText = "Not all parameters provided";
				}
				else 
				{
					
				}
				break;
			}
			case "D":
			{
				if(thisUser != null)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						
					}
				}
				break;
			}	
			case "US":
			{
				if(thisUser != null)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						
					}
				}
				break;
			}
			case "UN":
			{
				if(thisUser != null)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						
					}
				}
				break;
			}	
			case "B":
			{
				parent.refresh();
				return parent;
			}
			case "E":
			{
				return null;
			}
			
			default:
			{
				displayText = "Invalid option provided: " + splited.get(0);
				break;
			}
		}
		
		
		return this;
	}

	@Override
	public void refresh() {
		// nothing to refresh
		
	}

}
