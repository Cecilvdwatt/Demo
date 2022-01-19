package com.pk.db.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.query.Query;

import com.pk.db.StatusEnum;
import com.pk.db.entity.Project;
import com.pk.db.entity.User;
import com.pk.db.entity.WorkItem;

public class WorkItemDAO extends abstractDAO<WorkItem> {

	public WorkItem get(Long id) 
	{
		return super.get(WorkItem.class, id);
	}

	public List<WorkItem> getAll() 
	{
		return super.getAll(WorkItem.class);
	}
	
	
	public WorkItem add(String name, String description, Project project)
	{
		return this.add(new WorkItem(name, description, project));
	}
	
	public boolean unassign(long id)
	{
		try
		{
			setupSession();

			Query<User> query = session.createQuery("update WorkItem set user=null, lastUpdate=CURRENT_TIMESTAMP() where id = :identity");
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
	
	public long countByStatusForUser(long userId, String status)
	{
		try
		{
			setupSession();

			Query query = session.createQuery("select count(*) from WorkItem where status=:status and FK_user=:userid");
			query.setString("status", status);
			query.setLong("userid", userId);
			Long count = (Long)query.uniqueResult();

			breakdownSession();
			
			return count;
		}
		catch(Throwable e)
		{
			errorSession();
			throw e;
		}
	}
	
	
	
	
	@Override
	public WorkItem add(WorkItem toPersist) {
		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		return super.add(toPersist);
	}
	
	@Override
	public void update(WorkItem toPersist) {
		toPersist.setLastUpdate(new Date(System.currentTimeMillis()));
		super.update(toPersist);
	}

}
