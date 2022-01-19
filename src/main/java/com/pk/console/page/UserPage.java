package com.pk.console.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pk.db.entity.User;
import com.pk.db.entity.WorkItem;
import com.pk.service.UserService;
import com.pk.service.WorkItemService;
import com.pk.util.ErrorUtil;

public class UserPage  extends AbstractPage  {

	private User thisUser;
	
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
					+ "User Email:       ").append(thisUser.getEmail()).append("\n"
					+ "User Last Update: ").append(thisUser.getLastUpdate()).append("\n"
					+ "------------------------------------------------\n");
			
			if(thisUser.getWorkItems().size() == 0)
			{
				sb.append("\n\tNO WORK ITEMS");
			}
			else
			{
				List<WorkItem> sortedList = new ArrayList<WorkItem>(thisUser.getWorkItems());
				java.util.Collections.sort(sortedList);
				
				for(WorkItem item : sortedList)
				{
					sb.append("\n\tID: ").append(item.getId())
						.append(", Title:").append(item.getTitle())
						.append(", Status: ").append(item.getStatus());
					
					sb.append(", Desc: ").append(item.getDescription());
						
				}
			}
			
			sb.append("\n\n------------------------------------------------\n");
			
			sb.append("\n\nOptions:\n");
			sb.append("Add Work Item:           A {{title}} {{description}} {{project}}\n");
			
			if(thisUser.getWorkItems().size() > 0)
			{
				sb.append("Delete Work Item:        D {{work item id}}\n");
				sb.append("Update Work Item Status: US {{work item id}}\n");
				sb.append("Unassign Work Item:      U {{work item id}}\n");
			}
			sb.append("Update User Name:        UN {{new name}}\n");
			sb.append("Update Email:            UE {{new email}}\n");

		
		}
		
		sb.append("Back:                    B\n"
				+ "Exit:                    E\n");
		
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
					try 
					{
						thisUser.getWorkItems().add(WorkItemService.add(splited.get(1), splited.get(2), thisUser, splited.get(3)));						
					} 
					catch (Exception e) 
					{
						displayText = "Could not add work item. " + ErrorUtil.getErrorMsg(e);
					}
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
						WorkItem found = null;
						for(WorkItem item : thisUser.getWorkItems())
						{
							if(item.getId() == id)
							{
								found = item;
								break;
							}
						}
						
						if(found != null)
						{
							thisUser.getWorkItems().remove(found);
							WorkItemService.delete(found);
						}
						else 
						{
							displayText = "Could not find the Work Item";
						}
					}
				}
				break;
			}
			case "U":
			{
				if(thisUser != null && thisUser.getWorkItems().size() > 0)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						WorkItem found = null;
						for(WorkItem item : thisUser.getWorkItems())
						{
							if(item.getId() == id)
							{
								found = item;
								break;
							}
						}
						
						thisUser.getWorkItems().remove(found);
						if(found != null)
						{
							WorkItemService.unassignUser(found);
						}
						else 
						{
							displayText = "Could not find the Work Item";
						}
						
					}
				}
				break; 
			}
			case "US":
			{
				if(thisUser != null && thisUser.getWorkItems().size() > 0)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						WorkItem found = null;
						for(WorkItem item : thisUser.getWorkItems())
						{
							if(item.getId() == id)
							{
								found = item;
								break;
							}
						}
						
						if(found != null)
						{
							try 
							{
								WorkItemService.progressStatus(found);
							} catch (Exception e) 
							{
								displayText = "Could not progress status. " + ErrorUtil.getErrorMsg(e);
							}
						}
						else 
						{
							displayText = "Could not find the Work Item";
						}
						
					}
				}
				break; 
			}
			case "UN":
			{
				if(thisUser != null)
				{

					// thinking about it is it a good idea to persist the entire object back the the database.
					// well this is probably more important of a consideration if we had multiple user modifying the data
					// so we could run into a situation where one user modified the description but another user still has the 
					// old description and that get's persisted to the database...
					// well the requirements didn't state this, and it certainly is easier (and less time consuming) to just persisting the entire object...
					try
					{
						thisUser.setUserName(splited.get(1));
						UserService.update(thisUser);
					}
					catch(Throwable e)
					{
						displayText = "Could not update User Name. " + ErrorUtil.getErrorMsg(e);
					}
						
					
				}
				break;
			}
			case "UE":
			{
				if(thisUser != null)
				{
					try
					{
						thisUser.setEmail(splited.get(1));
						UserService.update(thisUser);
					}
					catch(Throwable e)
					{
						displayText = "Could not update email. " + ErrorUtil.getErrorMsg(e);
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

	@Override
	public void refresh() throws Exception {
		thisUser = UserService.get(thisUser.getId());

	}

}
