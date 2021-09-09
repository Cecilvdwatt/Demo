package com.pepkor.db.entity;

import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.pepkor.db.StatusEnum;

@Entity
@Table(name="WorkItem", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class WorkItem implements Comparable<WorkItem> {
	
	/////////
	// FIELDS
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", nullable=false, unique=true, length=11)
	private Long id;
	
	@Column(nullable=false)
	private String title;
	
	@Column
	private String description;
	
	@Column
	private String status;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date lastUpdate;
	
	////////////
	// RELATIONS
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="FK_user")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name ="FK_project", nullable=false)
	private Project project;
	

	public WorkItem()
	{
		
	}

	public WorkItem(String title, String desc, User user, Project project)
	{
		setTitle(title);
		setDescription(desc);
		setStatus(StatusEnum.TODO.name());
		setUser(user);
		setProject(project);
		this.setLastUpdate(new Date(System.currentTimeMillis()));
		
	}
	
	public WorkItem(String title, String desc, Project project)
	{
		setTitle(title);
		setDescription(desc);
		setStatus(StatusEnum.TODO.name());
		setProject(project);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(java.util.Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	} 

	
	@Override
	public int compareTo(WorkItem o) {
		return this.getId().compareTo(o.getId());
	}
	
}
