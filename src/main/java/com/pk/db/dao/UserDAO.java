package com.pk.db.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.query.Query;

import com.pk.db.entity.User;

public class UserDAO extends abstractDAO<User> {

	public User get(Long id) throws SQLException {

		try
		{

			User toReturn =super.get(User.class, id);
			if(toReturn != null)
			{
				toReturn.getWorkItems().size(); // force lazy load
			}

			return toReturn;
		}
		catch(Throwable e)
		{
			super.errorSession();
			throw new SQLException(e); // SQL exception is probably a bit misleading
		}
		
	}
	
	public User getLazy(Long id)
	{
		return super.get(User.class, id);
	}

	public List<User> getAll() {

		return super.getAll(User.class);
	}
	
	
	public User getByName(String name)
	{
		try
		{
			setupSession();

			Query<User> query = session.createQuery("from User where userName = :user_name", User.class);
	        query.setParameter("user_name", name);

	        List<User> toReturn = query.list();

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
	
	@Override
	public User add(User toPersist) {
		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		return super.add(toPersist);
	}
	
	@Override
	public void update(User toPersist) {
		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		super.update(toPersist);
	}
	
	

	public boolean delete(long id)
	{
		try
		{
			setupSession();

			Query<User> query = session.createQuery("delete User where id = :identity");
	        query.setParameter("identity", id);

	        int updatedRows = query.executeUpdate();
	        
	        
	        query = session.createQuery("update WorkItem set FK_user = null where FK_user = :identity");
	        query.setParameter("identity", id);

	        query.executeUpdate();

			breakdownSession();
			
			return updatedRows > 0;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	public boolean updateName(long id, String newName)
	{
		try
		{
			setupSession();

			Query<User> query = session.createQuery("update User set userName=:newname , lastUpdate=CURRENT_TIMESTAMP() where id = :identity");
			query.setParameter("newname", newName);
	        query.setParameter("identity", id);

	        int updatedRows = query.executeUpdate();

			breakdownSession();
			
			return updatedRows > 0;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	public boolean updateEmail(long id, String newEmail)
	{
		try
		{
			setupSession();

			Query<User> query = session.createQuery("update User set email=:newemail, lastUpdate=CURRENT_TIMESTAMP() where id = :identity");
			query.setParameter("newemail", newEmail);
	        query.setParameter("identity", id);

	        int updatedRows = query.executeUpdate();

			breakdownSession();
			
			return updatedRows > 0;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	public List<User> getRange(int start, int number)
	{
		return super.getRange(User.class, start, number);
	}
	

}
