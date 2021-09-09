package com.pepkor.service;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pepkor.db.DBConfig;
import com.pepkor.db.entity.Project;
import com.pepkor.db.entity.User;
import com.pepkor.db.entity.WorkItem;

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
	
	@Test
	public void UserMaxAssignment() {
		User toTest = UserService.add("Test User", "Test User");
		
		
	}

}
