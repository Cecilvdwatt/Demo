package com.pk.console.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pk.db.entity.WorkItem;
import com.pk.service.ProjectService;
import com.pk.service.WorkItemService;
import com.pk.util.ErrorUtil;

public class ProjectPage extends AbstractPage {
	

	private com.pk.db.entity.Project thisProject;

	private ViewState currentViewSate = ViewState.ALL;

	public ProjectPage(iConsolePage parent, long id) {
		super(parent);
		
		try 
		{
			thisProject = ProjectService.get(id);
		} 
		catch (Throwable e) 
		{
			thisProject = null;
			displayText = "ERROR: Could not load project: " + ErrorUtil.getErrorMsg(e);
		}
	}

	@Override
	public String getText() {
		
		StringBuilder sb = new StringBuilder();
		
		int filterItemCount = -1;
		int itemCount = -1;
		
		if(thisProject == null)
		{
			sb.append("Project Not Found!\n");
			
			sb.append("\n\nOptions:\n");
		}
		else
		{		
			sb.append("\n------------------------------------------------\n"
					+ "PROJECT DETAILS:"
					+ "\n------------------------------------------------\n"
					+ "Project Name:        ").append(thisProject.getName()).append("\n"
					+ "Project Description: ").append(thisProject.getDescription()).append("\n"
					+ "Last Update:         ").append(thisProject.getLastUpdate()).append("\n"
					+ "------------------------------------------------\n");
			
			Set<WorkItem> items;
			
			// we might potentially have a lot of items, so offer a bit of filtering
			// I considered implementing something similar to the ProjectList where there is a 
			// "paging" but that seems like a bother here. 
			// Although in principle it wouldn't be too difficult, since we're already loading all the items
			// so we'd just need a global variable to keep track of which "group" is currently being shown...
			// maybe make that a TODO ? But at this point most of the work in this project is going into the 
			// console side... which has been fun and all if somewhat (very) laborious. I see why people
			// moved away from complicated console applications. 
			
			if(currentViewSate == ViewState.TODO)
			{
				items = thisProject.getToDoItems();
			}
			else if(currentViewSate == ViewState.DOING)
			{
				items = thisProject.getDoingItems();
			}
			else if(currentViewSate == ViewState.DONE)
			{
				items = thisProject.getDoingItems();
			}
			else if(currentViewSate == ViewState.UNASSIGNED)
			{
				items = thisProject.getUnassigned();
			}
			else 
			{
				items = thisProject.getItems();
			}
			
			filterItemCount = items.size();
			itemCount = thisProject.getItems().size();
			
			if(filterItemCount == 0)
			{
				sb.append("\n\tNO WORK ITEMS");
			}
			else
			{
				sb.append("Filter:" + currentViewSate.name());
				
				List<WorkItem> sortedList = new ArrayList<WorkItem>(items);
				java.util.Collections.sort(sortedList);
				
				for(WorkItem item : sortedList)
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

			if(thisProject != null)
			{
				
			    sb.append("Add Work Item:          A {{work items name}} {{work item description}} {{OPTIONAL:user}}\n"
			    		+ "Update Project Name:    UN {{new name}}\n"
			    		+ "Update Description:     UD {{new description}}\n");
				
			    if(itemCount > 0)
				{
					sb.append("Delete Work Item:       D {{work item id}}:\n"
							+ "View Work Item:         V {{work item id}}\n"
							+ "Update Work Item State: US {{work item id}}\n");
					
					// no need to filter if there's nothing
					if(currentViewSate == ViewState.TODO)
					{
						sb.append("View Status Doing:      DOING\n"
								+ "View Status Done:       DONE\n"
								+ "View Status All:        ALL\n"
								+ "View Unassigned:        UNASSIGNED\n");
					}
					else if(currentViewSate == ViewState.DOING)
					{
						sb.append("View Status Doing:      TODO\n"
								+ "View Status Done:       DONE\n"
								+ "View Status All:        ALL\n"
								+ "View Unassigned:        UNASSIGNED\n");
					}
					else if(currentViewSate == ViewState.DONE)
					{
						sb.append("View Status Doing:      TODO\n"
								+ "View Status Doing:      DOING\n"
								+ "View Status All:        ALL\n"
								+ "View Unassigned:        UNASSIGNED\n");
					}
					else if(currentViewSate == ViewState.ALL)
					{
						sb.append("View Status Doing:      TODO\n"
								+ "View Status Doing:      DOING\n"
								+ "View Status Done:       DONE\n"
								+ "View Unassigned:        UNASSIGNED\n");
					}
					else if (currentViewSate == ViewState.UNASSIGNED)
					{
						sb.append("View Status Doing:      TODO\n"
								+ "View Status Doing:      DOING\n"
								+ "View Status Done:       DONE\n"
								+ "View Status All:        ALL\n");
					}
				}
			}
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
			displayText = "ERROR: No input given";
			return this;
		}
			
		List<String> splited = tokenizeInput(input);
		
		switch(splited.get(0).toUpperCase())
		{
			case "A":
			{
				if(splited.size() < 3)
				{
					displayText = "ERROR: Please provide all required parameters";					
				}
				else if(thisProject != null)
				{
					try
					{
						WorkItem newItem;
						if(splited.size() == 3)
						{
							newItem = WorkItemService.add(splited.get(1), splited.get(2), thisProject);
						}
						else 
						{
							newItem = WorkItemService.add(splited.get(1), splited.get(2), splited.get(3), thisProject);
						}
						
						thisProject.getItems().add(newItem);
						
						displayText = "Work Item Added";

					}
					catch(Throwable e)
					{
						displayText = "Error Adding Work Item" + ErrorUtil.getErrorMsg(e);
					}
				}
				break;
			}
			case "V":
			{
				long id = getCheckID(splited);
				if(id != -1)
				{
					return new WorkItemPage(this, id);
				}
				break;
			}
			case "D":
			{
				if(thisProject != null)
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						WorkItem toDel = thisProject.getWorkItem(id);
						
						if(toDel == null)
						{
							displayText = "Could not find the Work Item for ID:" + id;
						}
						else 
						{
							try
							{
								WorkItemService.delete(toDel);
								
								thisProject.getItems().remove(toDel);
								displayText = "Work Item Deleted";
							}
							catch(Throwable e)
							{
								displayText = "ERROR: Could not delete Work Item. " + ErrorUtil.getErrorMsg(e);
							}
						}
					}
				}
				break;
				
			}
			case "US":
			{
				if(thisProject !=  null) 
				{
					long id = getCheckID(splited);
					if(id != -1)
					{
						// Not sure if it's quicker to loop through the existing work items
						// or get the work item from the database again...
						// I guess it depends on the size, certainly the database would be 
						// better if we had thousands of work items per project (well I'm brute forcing my way 
						// items at the moment), but at the scope of this 
						// project I think we can get away with working with the in memory data
						WorkItem toProgress = thisProject.getWorkItem(id);
						
						if(toProgress == null)
						{
							displayText = "Could not find the Work Item for ID:" + id;
						}
						else 
						{
							try {
								WorkItemService.progressStatus(toProgress);
								displayText = "Work Item Status Progressed";
							} 
							catch (Exception e) 
							{
								displayText = "Could not progress status of Work Item. " + ErrorUtil.getErrorMsg(e);
							}
							
						}
					}
				}
				break;
			}
			case "UN":
			{
				if(splited.size() < 2)
				{
					displayText = "ERROR: Please provide all required parameters";					
				}
				else if(thisProject != null)
				{
					thisProject.setName(splited.get(1));
					ProjectService.update(thisProject);
				}
				break;
			}
			case "UD":
			{
				if(splited.size() < 2)
				{
					displayText = "ERROR: Please provide all required parameters";					
				}
				else if(thisProject != null)
				{
					thisProject.setDescription(splited.get(1));
					ProjectService.update(thisProject);
				}
				break;
			}
			case "TODO":
			{
				currentViewSate = ViewState.TODO;
				break;
			}
			case "DOING":
			{
				currentViewSate = ViewState.DOING;
				break;
			}
			case "DONE":
			{
				currentViewSate = ViewState.DONE;
				break;
			}
			case "ALL":
			{
				currentViewSate = ViewState.ALL;
				break;
			}
			case "UNASSIGNED":
			{
				currentViewSate = ViewState.UNASSIGNED;
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

	enum ViewState
	{
		ALL,
		TODO,
		DOING,
		DONE,
		UNASSIGNED
	}

	@Override
	public void refresh() {
		if(thisProject != null)
		{
			try 
			{
				thisProject = ProjectService.get(thisProject.getId());
			} 
			catch (SQLException e) 
			{
				displayText = "Error Rrefreshing. " + ErrorUtil.getErrorMsg(e);
			}
		}
	}

}
