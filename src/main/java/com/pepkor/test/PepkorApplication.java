package com.pepkor.test;

import org.hibernate.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pepkor.console.PepkorConsole;
import com.pepkor.db.DBConfig;
import com.pepkor.db.dao.ProjectDAO;
import com.pepkor.db.entity.Project;

@SpringBootApplication
public class PepkorApplication implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(PepkorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Start");
		
		new PepkorConsole().run();
			
		System.out.println("End");
	}

}
