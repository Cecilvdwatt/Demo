package com.pepkor.db.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.pepkor.db.StatusEnum;

@Entity
@Table(name="Project", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class Project {
	
	//////////
	// FIELDS
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true, length=11)
	private	Long id;
	
	@Column(name="project_name", nullable=false)
	private	String project_name;
	
	@Column
	private String description; 

	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date lastUpdate;
	
	
	////////////
	// RELATIONS
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<WorkItem> items; 
	
	public Project() {}
	
	public Project(String pname, String pdescription)
	{
		this.setName(pname);
		this.setDescription(pdescription);
		this.setLastUpdate(new Date(System.currentTimeMillis()));
		
	}
	
	//////////////////////
	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return project_name;
	}

	public void setName(String name) {
		this.project_name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<WorkItem> getItems() {
		return items;
	}
	
	public void removeItem(WorkItem item)
	{
		items.remove(item);
	}

	public void setItems(Set<WorkItem> items) {
		this.items = items;
	}

	public java.util.Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(java.util.Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	public WorkItem getWorkItem(long id)
	{
		for(WorkItem item : items)
		{
			if(item.getId() == id)
			{
				return item;
			}
		}
		
		return null;
	}
	
	public Set<WorkItem> getToDoItems()
	{
		Set<WorkItem> toReturn = new HashSet<WorkItem>();
		
		for(WorkItem item : items)
		{
			if(item.getStatus().equalsIgnoreCase(StatusEnum.TODO.name()))
			{
				toReturn.add(item);
			}
		}
		
		return toReturn;
	}
	
	public Set<WorkItem> getDoingItems()
	{
		Set<WorkItem> toReturn = new HashSet<WorkItem>();
		
		for(WorkItem item : items)
		{
			if(item.getStatus().equalsIgnoreCase(StatusEnum.DOING.name()))
			{
				toReturn.add(item);
			}
		}
		
		return toReturn;
		
	}
	
	public Set<WorkItem> getDone()
	{
		Set<WorkItem> toReturn = new HashSet<WorkItem>();
		
		for(WorkItem item : items)
		{
			if(item.getStatus().equalsIgnoreCase(StatusEnum.DONE.name()))
			{
				toReturn.add(item);
			}
		}
		
		return toReturn;
		
	}
	

	public Set<WorkItem> getUnassigned()
	{
		Set<WorkItem> toReturn = new HashSet<WorkItem>();
		
		for(WorkItem item : items)
		{
			if(item.getUser() == null)
			{
				toReturn.add(item);
			}
		}
		
		return toReturn;
		
	}
	
	
}