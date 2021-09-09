package com.pepkor.service;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pepkor.db.DBConfig;
import com.pepkor.db.StatusEnum;
import com.pepkor.db.entity.Project;
import com.pepkor.db.entity.User;
import com.pepkor.db.entity.WorkItem;

class WorkItemServiceTest {
	
	@BeforeEach
	public void setup()
	{
		 // setup in memory hibernate factory
		Configuration configuration = new Configuration();
	      configuration.addAnnotatedClass(Project.class)
	        .addAnnotatedClass(User.class)
	        .addAnnotatedClass(WorkItem.class);
	      configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	      configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
	      
	      configuration.setProperty("hibernate.connection.url", "jdbc:h2:./data/db");
	      configuration.setProperty("hibernate.hbm2ddl.auto", "create");

	      DBConfig.setSessionFactory(configuration.buildSessionFactory());
	}

	@Test
	public void StatusUpdate() throws Exception {
		
		// create a new Work Item
		long id = WorkItemService.add("test", "test", "test", ProjectService.add("TestProject","This is a Test Project")).getId();
		
		// INITIAL VALUE
		
		// lookup work item
		WorkItem retrieved = WorkItemService.get(id);
		
		// New WorkItems start as TODo
		Assert.assertEquals(StatusEnum.TODO.name(), retrieved.getStatus());
		
		// PROGRESSING TO DOING 
		
		WorkItemService.progressStatus(retrieved);
		
		// check in memory updated correctly
		Assert.assertEquals(StatusEnum.DOING.name(), retrieved.getStatus());
		
		// retrieve the work item from the database again to make sure we're properly persisting the status change and it's not just in memory
		retrieved = WorkItemService.get(id);
		
		// check db value updated correctly
		Assert.assertEquals(StatusEnum.DOING.name(), retrieved.getStatus());
		
		// PROGRESSING TO DONE 
		
		WorkItemService.progressStatus(retrieved);
		
		// check in memory updated correctly
		Assert.assertEquals(StatusEnum.DONE.name(), retrieved.getStatus());
		
		// retrieve the work item from the database again to make sure we're properly persisting the status change and it's not just in memory
		retrieved = WorkItemService.get(id);
		
		// check db value updated correctly
		Assert.assertEquals(StatusEnum.DONE.name(), retrieved.getStatus());
		
		
		// PROGRESSING TO TODO 
		
		WorkItemService.progressStatus(retrieved);
		
		// check in memory updated correctly
		Assert.assertEquals(StatusEnum.TODO.name(), retrieved.getStatus());
		
		// retrieve the work item from the database again to make sure we're properly persisting the status change and it's not just in memory
		retrieved = WorkItemService.get(id);
		
		// check db value updated correctly
		Assert.assertEquals(StatusEnum.TODO.name(), retrieved.getStatus());
		
	}
	
	

}
