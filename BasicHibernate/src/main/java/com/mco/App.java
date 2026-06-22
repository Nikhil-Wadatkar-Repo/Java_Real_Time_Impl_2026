package com.mco;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Hello world!
 */
public class App {
	 public static void main(String[] args)
	    {
	        // Open session
	        Session session = HibernateUtil.getSessionFactory()
	                              .openSession();

	        // Begin transaction
	        Transaction tx = session.beginTransaction();

	        // Create object
	        GeekUserDetails geekUser = new GeekUserDetails();
	        geekUser.setGeekUserId(3);
	        geekUser.setGeekUsername("GeekUser1");
	        geekUser.setNumberOfPosts(100);
	        geekUser.setCreatedBy("GeekUser1");
	        geekUser.setCreatedDate(new Date());

	        // Save object
	        session.save(geekUser);

	        // Commit transaction
	        tx.commit();

	        // Close session
	        session.close();
	    }
}
