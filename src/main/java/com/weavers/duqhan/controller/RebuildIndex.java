package com.weavers.duqhan.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Transactional
@RequestMapping("/buildIndex/**")
public class RebuildIndex{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private SessionFactory sessionFactory;
	
	public EntityManager getEntityManager() {
        return entityManager;
    }
	
	@Transactional
	@RequestMapping(value = "/build-index", method = RequestMethod.POST)
	public  void rebuildIndex() throws Exception {
	      try {
	    	  
	    	 sessionFactory = getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
	    	 Session session = sessionFactory.openSession();
	         FullTextSession fullTextSession = Search.getFullTextSession(session);
	         fullTextSession.createIndexer().transactionTimeout(120).startAndWait();
	         
	      }
	      catch(Exception e) {
	    	  e.printStackTrace();
	         throw e;
	      }
	}

	
}