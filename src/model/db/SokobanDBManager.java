package model.db;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import commons.Record;

public class SokobanDBManager 
{
	private static SokobanDBManager instance = new SokobanDBManager();
	private SessionFactory factory;

	public static SokobanDBManager getInstance() 
	{
		return instance;
	}

	private SokobanDBManager() 
	{
		// to show the severe message
		Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);

		// reading the XML so he can connect to the DB
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}
	
	public List<Record> recordsQuery(QueryParameters params)
	{
		Session session = null;
		Record record=null;
		Query query=null;
		List<Record>list = null;
		
		try 
		{
			session = factory.openSession();
			if(params.getLevelId().equals("null") && params.getUserName().equals("null"))
			{
				query = session.createQuery("from Records as rec ORDER BY rec."+params.getOrderBy());
			}
			else if(!params.getLevelId().equals("null"))
			{
				query = session.createQuery("from Records as rec where rec.levelID=:levelID "+
										  "ORDER BY rec."+params.getOrderBy());
				query.setParameter("levelID", params.getLevelId());
			}
			
			else if(!params.getUserName().equals("null"))
			{
				query=session.createQuery("from Records as rec where rec.userName=:userName "+
										  "ORDER BY rec."+params.getOrderBy());
				query.setParameter("userName", params.getUserName());
			}
			
			list = query.getResultList();
			Iterator<Record>it=list.iterator();
			
			while(it.hasNext())
			{
				record=it.next();
				System.out.println(record);
			}
		
		} 
		catch (HibernateException ex) 
		{
			System.out.println(ex.getMessage());
		} 
		finally 
		{
			if (session != null)
				session.close();
		}
		
		return list;
	}
	
	public void getAllRecords()
	{
		Session session = null;
		Record record;
		try 
		{
			session = factory.openSession();
			Query query=session.createQuery("from Records");
			List<Record>list=query.getResultList();
			Iterator<Record>it=list.iterator();
			
			while(it.hasNext())
			{
				record=it.next();
				System.out.println(record);
			}
		
		} 
		catch (HibernateException ex) 
		{
			System.out.println(ex.getMessage());
		} 
		finally 
		{
			if (session != null)
				session.close();
		}
	}
	
	public void add(Object obj)
	{
		Session session = null;
		Transaction tx = null;

		try 
		{
			session = factory.openSession();
			tx = session.beginTransaction();

			session.save(obj);
			tx.commit();
		} 
		catch (HibernateException ex) 
		{
			if (tx != null)
				tx.rollback();
			System.out.println(ex.getMessage());
		} 
		finally 
		{
			if (session != null)
				session.close();
		}
	}
	
	public void close() 
	{
		factory.close();
	}

}