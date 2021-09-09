package com.pepkor.console.page;

import java.util.List;

import com.pepkor.db.entity.Project;
import com.pepkor.service.ProjectService;
import com.pepkor.util.ErrorUtil;

public class ProjectListPage extends AbstractPage {

	
	private int projectGroup = 0;
	
	public ProjectListPage(iConsolePage parent)
	{
		super(parent);
	}	
	
	
	@Override
	public String getText() {
		
		StringBuilder sb = new StringBuilder(
				"\n------------------------------------------------\n"
				+ "VIEWING PROJECTS:"
				+ "\n------------------------------------------------\n"
				+ "\tViewing Projects (Group ").append(projectGroup).append("):\n"
				+ "------------------------------------------------\n");
		
		// it's probably not really worth caching this for performance, this isn't going to be a 
		// high transaction demo app, so just get the new list each time. 
		List<Project> holders = ProjectService.getProjectsAt(projectGroup);
		
		int projectsCount = holders.size();

		if(projectsCount > 0)
		{
			for(Project holder : holders)
			{
				sb.append("\n\tID: ").append(holder.getId()).append(", Name: ").append(holder.getName()).append(", Desc: ").append(holder.getDescription());
			}
		}
		else 
		{
			sb.append("\n\tNO PROJECTS");
		}
		
		sb.append("\n------------------------------------------------\n"
				+ "\n\nOptions:\n"
				+ "Add Project:           A {{Project Name}} {{Project Description}}\n");
		
		if(projectsCount > 0)
		{
			sb.append(
				  "View Specific Project: V {{Project ID}}\n"
				+ "Delete Project:        D {{Project ID}}\n");		
		}
		if(projectsCount == 10)
		{
			sb.append(
				  "View Next Group:       N\n");		
		}
		if(projectGroup > 0)
		{
			sb.append("View Previous Group:   P\n");
		}
		
		sb.append("Go Back:               B\n"
				+ "Exit:                  E\n");
		
		
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
				if(splited.size() != 3)
				{
					displayText = "Please provide at least 3 parameters";
				}
				else 
				{
					try
					{
						ProjectService.add(splited.get(1), splited.get(2));
						displayText = "Added Project";
					}
					catch(Throwable e)
					{
						displayText = "ERROR: COuld not a Project. " + ErrorUtil.getErrorMsg(e);
					}
				}
				break;
			}
			case "V":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{
					return new ProjectPage(this, id);
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
						ProjectService.delete(id);
						displayText = "Project Deleted";
					}
					catch(Throwable e)
					{
						displayText = "ERROR: Could not delete Project. " + ErrorUtil.getErrorMsg(e);
					}
					
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
		// nothing needing refreshing
		
	}


}
