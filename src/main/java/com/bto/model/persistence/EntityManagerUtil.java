package com.bto.model.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EntityManagerUtil {

	private static int nbEntityManagerOuverts = 0;
	private static int nbEntityManagerFermes = 0;

	/**
	 * <p>
	 * Declare the persistence unit for this EntityManagerHelper ("entity").
	 * </p>
	 * <p>
	 * This is the only setting that might need to be changed between applications. Otherwise, this class can be dropped into any JPA application.
	 * </p>
	 */
	private static String DEFAULT_PERSISTENCE_UNIT = "local";

	private static EntityManagerFactory factory;

	public static void setPersistenceUnit(final String name) {
		factory = Persistence.createEntityManagerFactory(name);
	}

	public static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<EntityManager>();
	private static Logger logger = LoggerFactory.getLogger(EntityManagerUtil.class);

	public static EntityManagerFactory getEntityManagerFactory() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT);
		}
		return factory;
	}

	/**
	 * <p>
	 * Provide a per-thread EntityManager "singleton" instance.
	 * </p>
	 * <p>
	 * This method can be called as many times as needed per thread, and it will return the same EntityManager instance, until the manager is closed.
	 * </p>
	 * 
	 * @return EntityManager singleton for this thread
	 */
	public static EntityManager getEntityManager() {
		EntityManager manager = threadLocal.get();
		if (manager == null || !manager.isOpen()) {
			nbEntityManagerOuverts++;
			logger.trace(" <-- createEntityManager() try ");
			manager = getEntityManagerFactory().createEntityManager();
			threadLocal.set(manager);
			logger.trace(" <-- createEntityManager() success " + getEntityManager().toString() + " - nb occurences ouverts = " + nbEntityManagerOuverts
					+ " - nb occurences fermés = " + nbEntityManagerFermes);
		}
		return manager;
	}

	/**
	 * <p>
	 * Close the EntityManager and set the thread's instance to null.
	 * </p>
	 */
	public static void closeEntityManager() {

		String entitym = getEntityManager().toString();
		logger.trace(" <-- closeEntityManager() try ");

		EntityManager em = threadLocal.get();
		threadLocal.set(null);
		if (em != null) {
			nbEntityManagerFermes++;
			em.close();
			logger.trace(" <-- closeEntityManager() success " + entitym + " - nb occurences ouverts = " + nbEntityManagerOuverts + " - nb occurences fermés = "
					+ nbEntityManagerFermes);
		}

		//pour monitorer C3P0
/*		PooledDataSource dataSource = null;
		try {
			C3P0Registry.getNumPooledDataSources();
			// I am sure that I am using only one data source
			Iterator<Set> connectionIterator = C3P0Registry.getPooledDataSources().iterator();
			dataSource = (PooledDataSource) connectionIterator.next();

			logger.trace(" Connections in use: " + dataSource.getNumConnectionsAllUsers() + " , Busy Connections: " + dataSource.getNumBusyConnectionsAllUsers()
					+ " , Idle Connections: " + dataSource.getNumIdleConnectionsAllUsers() + " , Unclosed Orphaned Connections: "
					+ dataSource.getNumUnclosedOrphanedConnectionsAllUsers());

		} catch (Exception e) {
		}*/

	}

	/**
	 * <p>
	 * Initiate a transaction for the EntityManager on this thread.
	 * </p>
	 * <p>
	 * The Transaction will remain open until commit or closeEntityManager is called.
	 * </p>
	 */
	public static void beginTransaction() {
		if (threadLocal.get() != null) {
			logger.error("XXXX --> threadLocal.get() != null - " + getEntityManager().toString());

			if (getEntityManager().getTransaction().isActive()) {
				commit();
			}
			closeEntityManager();
		}

		logger.trace(" --> beginTransaction() getEntityManager...");
		EntityManager em = getEntityManager();
		logger.trace(" --> beginTransaction() getTransaction...");
		EntityTransaction tr = em.getTransaction();
		logger.trace(" --> beginTransaction() begin...");
		tr.begin();
		logger.trace(" --> beginTransaction() success " + getEntityManager().toString() + " - nb occurences ouverts = " + nbEntityManagerOuverts + " - nb occurences fermés = "
				+ nbEntityManagerFermes);
	}

	/**
	 * <p>
	 * Submit the changes to the persistance layer.
	 * </p>
	 * <p>
	 * Until commit is called, rollback can be used to undo the transaction.
	 * </p>
	 */
	public static void commit() {
		logger.trace(" <--> commit() try " + getEntityManager().toString() + " - nb occurences ouverts = " + nbEntityManagerOuverts + " - nb occurences fermés = "
				+ nbEntityManagerFermes);
		getEntityManager().getTransaction().commit();
		logger.trace(" <--> commit() success " + getEntityManager().toString() + " - nb occurences ouverts = " + nbEntityManagerOuverts + " - nb occurences fermés = "
				+ nbEntityManagerFermes);
	}

	/**
	 * <p>
	 * Create a query for the EntityManager on this thread.
	 * </p>
	 */
	public static Query createQuery(String query) {
		return getEntityManager().createQuery(query);
	}

	public static Query createNativeQuery(String query) {
		return getEntityManager().createNativeQuery(query);
	}

	/**
	 * <p>
	 * Create a query for the EntityManager on this thread.
	 * </p>
	 */
	public static Query createQuery(CriteriaQuery query) {
		return getEntityManager().createQuery(query);
	}

	/**
	 * <p>
	 * Flush the EntityManager state on this thread.
	 * </p>
	 */
	public static void flush() {
		getEntityManager().flush();
	}

	/**
	 * <p>
	 * Write an error message to the logging system.
	 * </p>
	 */
	public static void logError(String info, Throwable ex) {
		logger.error(info, ex);
	}

	/**
	 * <p>
	 * Undo an uncommitted transaction, in the event of an error or other problem.
	 * </p>
	 */
	public static void rollback() {
		getEntityManager().getTransaction().rollback();
	}

	/**
	 * <p>
	 * Undo an uncommitted transaction, in the event of an error or other problem.
	 * </p>
	 */
	public static void rollbackSilently() {
		try {
			getEntityManager().getTransaction().rollback();
		} catch (Throwable rbEx) {
			// shuuuuuut
		}
	}

	public static void setRollbackOnly() {
		if (!getEntityManager().getTransaction().getRollbackOnly()) {
			try {
				logger.debug("Trying to setRollbackOnly database transaction");
				getEntityManager().getTransaction().setRollbackOnly();
			} catch (Throwable rbEx) {
				logger.error("Could not setRollbackOnly transaction after exception!", rbEx);
			}
		}
	}

	public static EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}

	public static void save(Object entity) {
		 getEntityManager().persist(entity);
	}
}
