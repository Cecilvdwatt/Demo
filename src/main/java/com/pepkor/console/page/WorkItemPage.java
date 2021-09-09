package com.pepkor.console.page;

import java.util.List;

import com.pepkor.db.entity.WorkItem;
import com.pepkor.service.WorkItemService;
import com.pepkor.util.ErrorUtil;

public class WorkItemPage  extends AbstractPage {

	private WorkItem thisWorkItem;
	
	public WorkItemPage(iConsolePage parent, long id) {
		
		super(parent);
		this.thisWorkItem = WorkItemService.get(id);
		
	}

	@Override
	public String getText() {
		
		StringBuilder sb = new StringBuilder(
				"\n------------------------------------------------\n"
				+ "WORKITEM DETAILS:"
				+ "\n------------------------------------------------\n");
		
		if(thisWorkItem == null)
		{
			sb.append("Work Item Not Found!");
			
			sb.append("\n------------------------------------------------\n"
					+ "Options:");
		}
		else
		{			
			sb.append("WorkItem Name:        ").append(thisWorkItem.getTitle())
				.append("\nWorkItem Description: ").append(thisWorkItem.getDescription())
				.append("\nStatus:               ").append(thisWorkItem.getStatus());
					
			if(thisWorkItem.getUser() == null)
			{
				sb.append("\nUser:                 UNASSIGNED");
			}
			else
			{
				sb.append("\nUser:                 ").append(thisWorkItem.getUser().getUserName());
			}
			
			sb.append("\nProject:              ").append(thisWorkItem.getProject().getName())
				.append("\nLast Update:          ").append(thisWorkItem.getLastUpdate()).append("\n");
			
			sb.append("\n------------------------------------------------\n\nOptions:"
					+ "\nUpdate Name:        UN {{new name}}"
					+ "\nUpdate Description: UD {{new description}}"
					+ "\nProgress Status:    U"
					+ "\nUnassign User:      UU"
					+ "\nAssign User:        A {{user}}");
		}
		
		
		
		sb.append("\nBack:               B"
				+ "\nExit:               E");
		
		if(!displayText.isEmpty())
		{
			sb.append("\n\n").append(displayText).append("\n");
		}
		
		
		return sb.toString();
	}

	@Override
	public iConsolePage processInput(String input) {
		
		displayText = "";
		
		if(input.isEmpty())
		{
			displayText = "No input give";
			return this;
		}
			
		List<String> splited = tokenizeInput(input);
		
		switch(splited.get(0).toUpperCase())
		{
			case "UN":
			{
				if(splited.size() < 2)
				{
					displayText = "New name not provided";
				}
				else 
				{
					if(thisWorkItem != null)
					{
						try 
						{
							thisWorkItem.setTitle(splited.get(1));
							WorkItemService.update(thisWorkItem);
							displayText = "Name Upated";
						}
						catch(Throwable e)
						{
							displayText = "Could not update name. " + ErrorUtil.getErrorMsg(e);
						}
					}
				}
				break;
			}
			case "UD":
			{
				if(splited.size() < 2)
				{
					displayText = "New description not provided";
				}
				else 
				{
					if(thisWorkItem != null)
					{
						try 
						{
							thisWorkItem.setDescription(splited.get(1));
							WorkItemService.update(thisWorkItem);
							displayText = "Description Updated";
						}
						catch(Throwable e)
						{
							displayText = "Could not update description. " + ErrorUtil.getErrorMsg(e);
						}
					}
				}
				break;
			}
			case "U":
			{
				try {
					WorkItemService.progressStatus(thisWorkItem);
					displayText = "Status Updated";
				} 
				catch (Exception e) 
				{
					displayText = "Could not Progress Status of Work Item. " + ErrorUtil.getErrorMsg(e);
				}
				
				break;
			}
			case "UU":
			{
				WorkItemService.unassignUser(thisWorkItem);
				displayText = "User Unassigned";
				break;
			}
			case "A":
			{
				if(splited.size() < 2)
				{
					displayText = "New description not provided";
				}
				else 
				{
					if(thisWorkItem != null)
					{
						try
						{
							WorkItemService.assignUser(thisWorkItem, splited.get(1));
						}
						catch(Throwable e)
						{
							displayText = "Could not Assign User. " + ErrorUtil.getErrorMsg(e);
						}
					}
				}
				break;
			}
			case "B":
			{
				try
				{
					parent.refresh();
					return parent;
				}
				catch(Exception e)
				{
					displayText = "Could not go back. " + ErrorUtil.getErrorMsg(e);
				}
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
	
	enum State
	{
		WELCOME, 
		UPDATE,
		PROGRESSED,
		ERROR
	}

	@Override
	public void refresh() {
		if(thisWorkItem != null)
			thisWorkItem = WorkItemService.get(thisWorkItem.getId());
		
	}

}
