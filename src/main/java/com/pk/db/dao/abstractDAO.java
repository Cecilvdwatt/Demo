package com.pk.db.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.pk.db.DBConfig;

public abstract class abstractDAO<T> {

	// Although I've have very little opportunity to work in Spring
	// I have previously done some research on it.
	// Once thing I saw that was interesting as the Aspect Orientated Programming
	// and I thought wouldn't it be interesting if I could use AspectJ and do my
	// setup and cleanup of my database connections in the @Before and @After tags
	// sadly AspectJ is kinda... finicky. Couldn't get it work, likely some config file 
	// somewhere didn't have a switch flipped.
	// Oh well all that would have done was "automated" a couple of calls, 
	// I can still just do it manually. 
	protected Session session;
	protected Transaction transaction;
	
	protected void setupSession()
	{
		session = DBConfig.getSessionFactory().openSession();
		transaction = session.beginTransaction();
	}
	
	protected void breakdownSession()
	{
	
		transaction.commit();
		session.close();
	}
	
	protected void errorSession()
	{
		if(transaction != null)
			transaction.rollback();
		
		if(session != null)
			session.close();
	}
	
	
	public T add(T toPersist) {
		
		try
		{
			setupSession();
			session.persist(toPersist);
			breakdownSession();
			
			return toPersist;
			
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	public void update(T toPersist) {
		
		try
		{
			setupSession();
			session.saveOrUpdate(toPersist);
			breakdownSession();
			
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}


	public void delete(T toDel) {
		
		try
		{
			setupSession();
			session.remove(toDel);
			breakdownSession();
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	protected T get(Class<T> entityClass, Long id) 
	{

		try
		{
			setupSession();
			T toReturn = (T) session.find(entityClass, id);
			breakdownSession();
			
			return toReturn;
			
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	protected List<T> getAll(Class<T> entityClass) 
	{
		try
		{
			setupSession();
			
			// Create CriteriaQuery
			CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityClass);
	        criteriaQuery.from(entityClass);
	        List<T> toReturn = session.createQuery(criteriaQuery).getResultList();
			
			breakdownSession();
			
			return toReturn;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	protected List<T> getRange(Class<T> entityClass, int start, int number)
	{
		try
		{
			setupSession();
			
			// Create CriteriaQuery
			Criteria criteriaQuery = session.createCriteria(entityClass);
			criteriaQuery.addOrder(Order.asc("id"));
			criteriaQuery.setFirstResult(start);
			criteriaQuery.setMaxResults(number);
			
	        List<T> toReturn = criteriaQuery.list();
			
			breakdownSession();
			
			return toReturn;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
}
