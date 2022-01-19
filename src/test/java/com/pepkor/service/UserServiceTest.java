package com.pepkor.service;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pk.db.DBConfig;
import com.pk.db.entity.Project;
import com.pk.db.entity.User;
import com.pk.db.entity.WorkItem;
import com.pk.service.ProjectService;
import com.pk.service.UserService;
import com.pk.service.WorkItemService;

class UserServiceTest {

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
	
	
	/**
	 * Test that when we attempt to Progress the status of a Work Item assigned to a user that already
	 * has 3 Work Items in doing that the correct exception is thrown
	 */
	@Test
	public void UserItemProgressTest() throws Exception {
		User userToTest = UserService.add("Test User", "Test User");
		Project projectToAdd = ProjectService.add("TestProject","This is a Test Project");
		
		WorkItem toPersist = new WorkItem("WI1", "desc", userToTest, projectToAdd);
		WorkItemService.add(toPersist);
		WorkItemService.progressStatus(toPersist); // put it in doing
		
		toPersist = new WorkItem("WI2", "desc", userToTest, projectToAdd);
		WorkItemService.add(toPersist);
		WorkItemService.progressStatus(toPersist); // put it in doing
		
		toPersist = new WorkItem("WI3", "desc", userToTest, projectToAdd);
		WorkItemService.add(toPersist);
		WorkItemService.progressStatus(toPersist); // put it in doing
		
		
		Exception exception = assertThrows(Exception.class, () -> {
			
			// we only allow 3 items  in a doing category so this should add but fail when we progress
			WorkItem toFail = new WorkItem("WI4", "desc", userToTest, projectToAdd);
			WorkItemService.add(toFail);
			
			// This should fail since we already have 3 items in doing
			WorkItemService.progressStatus(toFail); 
	    });
		

	    String expectedMessage = "Maximum number of tasks in doing status for associated user";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	/**
	 * Test that when we assign a Work Item in the doing state to a User that already has 3 items in the doing state
	 * that the correct exception is thrown 
	 * @throws Exception 
	 */
	@Test
	public void UserItemAssignmentStatusTest() throws Exception {
		
		User userToTest = UserService.add("Test User", "Test User");
		
		Project projectToAdd = ProjectService.add("TestProject","This is a Test Project");
		
		
		WorkItem item1 = new WorkItem("WI1", "desc", projectToAdd);
		WorkItemService.add(item1);
		WorkItemService.progressStatus(item1); // put it in doing
		
		WorkItem item2 = new WorkItem("WI2", "desc", projectToAdd);
		WorkItemService.add(item2);
		WorkItemService.progressStatus(item2); // put it in doing
		
		WorkItem item3 = new WorkItem("WI3", "desc", projectToAdd);
		WorkItemService.add(item3);
		WorkItemService.progressStatus(item3); // put it in doing

		WorkItemService.assignUser(item1, userToTest.getId().toString());
		WorkItemService.assignUser(item2, userToTest.getId().toString());
		WorkItemService.assignUser(item3, userToTest.getId().toString());
		
		
		Exception exception = assertThrows(Exception.class, () -> {
			
			WorkItem item4 = new WorkItem("WI4", "desc", projectToAdd);
			WorkItemService.add(item4);
			WorkItemService.progressStatus(item4); // put it in doing
			
			//this should fail because we already hit the status limit
			WorkItemService.assignUser(item4, userToTest.getId().toString());
	    });
		
		
		String expectedMessage = "Maximum number of tasks in doing status for associated user";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));
		
	}

}
