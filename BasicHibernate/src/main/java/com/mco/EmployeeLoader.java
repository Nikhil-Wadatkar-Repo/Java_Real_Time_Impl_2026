package com.mco;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Hello world!
 */
public class EmployeeLoader {
	 public static void main(String[] args)
	    {
	        // Open session
	        Session session = HibernateUtil.getSessionFactory()
	                              .openSession();

	        // Begin transaction
	        Transaction tx = session.beginTransaction();

	        // Create object
	        List<Employee> employees = Employee.getEmployees();
	        
	        employees.forEach(emp->{
	        	session.save(emp);
	        });

	        // 

	        // Commit transaction
	        tx.commit();

	        // Close session
	        session.close();
	    }
}
