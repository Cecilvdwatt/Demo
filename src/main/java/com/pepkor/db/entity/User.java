package com.pepkor.db.entity;


import java.sql.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="User", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class User {
	
	////////////
	// FIELDS
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", nullable=false, unique=true, length=11)
	private Long id;
	
	@Column(nullable=false)
	private String userName;	
	
	@Column
	private String email;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date lastUpdate;
	
	////////////
	// RELATIONS
		
	@OneToMany(cascade = CascadeType.ALL)
	private Set<WorkItem> workItems;
	
	
	public User()
	{
		
	}
	
	public User(String name, String email)
	{
		this.setUserName(name);
		this.setEmail(email);
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<WorkItem> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(Set<WorkItem> workItems) {
		this.workItems = workItems;
	}

	public java.util.Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(java.util.Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	
}
