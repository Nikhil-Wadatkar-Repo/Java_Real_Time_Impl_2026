package com.mco;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class App2_Load {
	public static void main(String[] args) {
		Integer userId = 3;
		if (userId == null || userId <= 0) {
			System.err.println("A positive user ID is required.");
			return;
		}

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				GeekUserDetails details = session.get(GeekUserDetails.class, userId);

				if (details == null) {
					System.out.println("No user found with ID: " + userId);
				} else {
					System.out.println(details);
				}

				transaction.commit();
			} catch (RuntimeException exception) {
				if (transaction != null && transaction.isActive()) {
					try {
						transaction.rollback();
					} catch (RuntimeException rollbackException) {
						exception.addSuppressed(rollbackException);
					}
				}
				System.err.println("Unable to load user with ID " + userId + ": " + exception.getMessage());
			}
		} catch (RuntimeException exception) {
			System.err.println("Unable to open or close the Hibernate session: " + exception.getMessage());
		}
	}
}
