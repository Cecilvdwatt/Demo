package com.pk;

import org.hibernate.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pk.console.PKConsole;
import com.pk.db.DBConfig;
import com.pk.db.dao.ProjectDAO;
import com.pk.db.entity.Project;

@SpringBootApplication
public class PKDemoApplication implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(PKDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Start");
		
		new PKConsole().run();
			
		System.out.println("End");
	}

}
