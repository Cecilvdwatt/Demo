package com.pk.db.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;

import com.pk.db.entity.Project;
import com.pk.db.entity.User;

public class ProjectDAO extends abstractDAO<Project> {

	
	/**
	 * Gets the Project by it's ID and force lazy load work items
	 */
	public Project get(Long id) throws SQLException {
		
		// so this is a bit of an ugly solution
		// I don't want to eagerly fetch the work items
		// because there's times I only want the project (i.e. get all and get range methods)
		
		// Now what I should do is expand my service layer to have business objects and keep my 
		// entity class completely cordoned off... that way I have more control over how, when and where my
		// entity classes get created and invoked... but I also have a finite amount of time... so a TODO on that. 
		
		// Anyway, I have hibernate.enable_lazy_load_no_trans switched on
		// so I'll force the lazy load of the items in here
		// and handle any exceptions in here
		
		// This will technically do two calls to the database but it's a hit I'm willing to take, and a small one at that
		
		try
		{
			Project toReturn =super.get(Project.class, id);
			if(toReturn != null)
			{
				toReturn.getItems().size(); // force lazy load
			}
			return toReturn;
		}
		catch(Throwable e)
		{
			super.errorSession();
			throw new SQLException(e); // SQL exception is probably a bit misleading
		}
	}
	
	/**
	 * Gets the Project by it's ID but does not force lazy work items
	 */
	public Project getLazyByID(Long id)
	{
		return super.get(Project.class, id);
	}
	
	public Project getByName(String name)
	{
		try
		{
			setupSession();

			Query<Project> query = session.createQuery("from Project where project_name = :project_name", Project.class);
	        query.setParameter("project_name", name);

	        List<Project> toReturn = query.list();

			breakdownSession();
			
			if(toReturn.size() > 0)
			{
				return toReturn.get(0);
			}
			else 
			{
				return null;
			}
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}

	public List<Project> getAll() {

		return super.getAll(Project.class);
	}

	public List<Project> getRange(int start, int number) {
		
		return super.getRange(Project.class, start, number);
	}
	
	@Override
	public Project add(Project toPersist) {

		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		return super.add(toPersist);
	}
	
	@Override
	public void update(Project toPersist) {

		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		super.update(toPersist);
	}


}
