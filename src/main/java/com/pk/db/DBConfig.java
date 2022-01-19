package com.pk.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBConfig {

	
	private static SessionFactory sessionFactory;
	private static Object mutex = new Object();
	
	public static void setSessionFactory(SessionFactory toSet)
	{
		synchronized(mutex)
		{
			sessionFactory = toSet;
		}
	}

	/**
	 * This method gets the Hibernate Session factory using the singleton pattern.
	 * 
	 * @return
	 * The SesionFactory to access the database.
	 */
	public static SessionFactory getSessionFactory()
		throws ExceptionInInitializerError
	{
		if(sessionFactory == null)
		{
			// don't want to synchronizze the method itself for performance reasons
			// so I prefer the synchronization block since this with the null check
			// this allows us to have some degree of concurrency (once the singleton has been 
			// initalised of course)
			synchronized(mutex)
			{
				// if we don't re-check for null we might end up reinitializing if we 
				// have multiple concurrent threads assessing this method for the first time. 
				if(sessionFactory == null)
				{
					try 
					{
						// configures settings from hibernate.cfg.xml 
						StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build(); 
						sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
					}
			        catch (Throwable e) 
					{
			            throw new ExceptionInInitializerError(e);
			        }
				}
		    }
		}
		
		return sessionFactory;
	}    
}
