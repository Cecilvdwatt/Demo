package com.pk.console.page;

import java.util.List;

import com.pk.db.entity.User;
import com.pk.service.UserService;
import com.pk.util.ErrorUtil;

public class UserListPage extends AbstractPage {


	private int projectGroup = 0;
	
	public UserListPage(iConsolePage parent)
	{
		super(parent);
	}	
	
	
	@Override
	public String getText() {


		StringBuilder sb = new StringBuilder(
				"\n------------------------------------------------\n"
				+ "VIEWING USERS:"
				+ "\n------------------------------------------------\n"
				+ "\nViewing User Group (").append(projectGroup).append("):\n\n");
		
		List<User> allUsers = UserService.getUserAt(projectGroup);
		
		int userCount = allUsers.size();
		
		sb.append("------------------------------------------------");
		if(allUsers.size() > 0)
		{
			for(User user : allUsers)
			{
				sb.append("\n\tID:").append(user.getId())
					.append(", Name: ").append(user.getUserName())
					.append(", Email: ").append(user.getEmail())
					.append(", Last Update: ").append(user.getLastUpdate());
			}
		}
		else 
		{
			sb.append("\n\tNO USERS");
		}
		sb.append("\n------------------------------------------------\n");
		
		sb.append("Options:\n");
		sb.append("Add :                  A {{Name}} {{Email}}\n");
		sb.append("Delete:                D {{user id}}\n");
		sb.append("Update Name:           UN {{user id}} {{new name}}\n");
		sb.append("Update Email:          UE {{user id}} {{new email}}\n");
		sb.append("View User:             V {{user id}}\n");
		
		if(userCount == 10)
		{
			sb.append("View Next Group:       N\n");		
		}
		if(projectGroup > 0)
		{
			sb.append("View Previous Group:   P\n");
		}
		
		sb.append("Go Back:               B\n");
		sb.append("Exit:                  E\n");
		
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
			displayText = "No input give";
			return this;
		}
			
		List<String> splited = tokenizeInput(input);
		
		switch(splited.get(0).toUpperCase())
		{
			case "A":
			{
				try
				{
					if(splited.size() < 2)
					{
						displayText = "ERROR: Please provide all required parameters";					
					}
					else 
					{
						UserService.add(splited.get(1), splited.get(2));
						displayText = "New User Added";		
					}
					break;
				}
				catch(Throwable e)
				{
					displayText = "ERROR: Could not add User. " + ErrorUtil.getErrorMsg(e);
				}
				break;
			}
			case "D":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{

					try
					{
						if(UserService.delete(id))
						{
							
							displayText = "User Deleted";
						}
						else 
						{
							displayText = "Could not find the User for ID:" + id;
						}
					}
					catch(Throwable e)
					{
						displayText = "ERROR: Could not delete User. " + ErrorUtil.getErrorMsg(e);
					}
				}
				break;
			}
			case "UN":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{
					try
					{
						if(UserService.updateName(id, splited.get(2)))
						{
							displayText = "User Name Updated";
						}
						else
						{
							displayText = "Could not find user to update";
						}
					}
					catch(Throwable e)
					{
						displayText = "ERROR: Could not update name. " + ErrorUtil.getErrorMsg(e);
					}
					
					
				}
				break;
			}
			case "UE":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{
					try
					{
						if(UserService.updateEmail(id, splited.get(2)))
						{
							displayText = "User Email Updated";
						}
						else 
						{
							displayText = "Could not find user to update";
						}
					}
					catch(Throwable e)
					{
						displayText = "ERROR: Could not update email. " + ErrorUtil.getErrorMsg(e);
					}
					
				}
				break;
			}
			case "V":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{
					return new UserPage(this, id);
				}
				
				break;
			}
			case "N":
			{
				projectGroup++;
				break;
			}
			case "P":
			{
				if(projectGroup > 0)
				{
					projectGroup--;
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
			}
		}
		
		return this;
	}


	@Override
	public void refresh() {
		// Nothing needing refresh here
		
	}
	
}
