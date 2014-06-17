package com.hibernateone;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hibernateone.entity.Department;
import com.hibernateone.entity.Employee;
import com.hibernateone.entity.Stock;
import com.hibernateone.entity.StockDetail;

public class HibernateOneTest {

	private static final String PERSISTENCE_UNIT_NAME = "empDept";
	private static final String SPRING_JPA_CONFIG = "jpaConfig.xml";
	private static EntityManagerFactory factory;

	@Test
	public void jPAOneToMany() {
		
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Department department1 = new Department("java");
		Department department2 = new Department(".net");
		em.persist(department1);
		em.persist(department2);
		em.persist(new Employee("Jakab Gipsz", department1));
		em.persist(new Employee("Captain Nemo", department2));
		em.getTransaction().commit();

		Query q = em.createQuery("From Employee");
		@SuppressWarnings("unchecked")
		List<Employee> empList = q.getResultList();
		for (Employee emp : empList) {
			System.out.println(emp);
		}
	}
	
	
	@Test
	public void jPAwithSpringOneToMany() {

		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(SPRING_JPA_CONFIG);  
		factory = (EntityManagerFactory) appContext.getBean("mydbemfb");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Department department1 = new Department("java");
		Department department2 = new Department(".net");
		em.persist(department1);
		em.persist(department2);
		em.persist(new Employee("Jakab Gipsz", department1));
		em.persist(new Employee("Captain Nemo", department2));
		em.getTransaction().commit();

		Query q = em.createQuery("From Employee");
		@SuppressWarnings("unchecked")
		List<Employee> empList = q.getResultList();
		for (Employee emp : empList) {
			System.out.println(emp);
		}
		appContext.close();
	}
	@Test
	public void hibernateOneToMany() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();

		Department department1 = new Department("java");
		Department department2 = new Department(".net");
		session.save(department1);
		session.save(department2);

		session.save(new Employee("Jakab Gipsz", department1));
		session.save(new Employee("Captain Nemo", department2));

		session.getTransaction().commit();

		org.hibernate.Query createQuery = session.createQuery("From Employee");

		@SuppressWarnings("unchecked")
		List<Employee> resultList = createQuery.list();
		System.out.println("num of employess:" + resultList.size());
		for (Employee next : resultList) {
			System.out.println("next employee: " + next);
		}
		session.close();
		HibernateUtil.getSessionFactory().close();

	}
	
	@Test
	public void hibernateOneToOne() {
		System.out.println("Hibernate one to one (Annotation)");
		Session session = HibernateUtil.getSessionFactory().openSession();
 		session.beginTransaction();
 
		Stock stock = new Stock();
 		stock.setStockCode("7052");
		stock.setStockName("PADINI");
 
		StockDetail stockDetail = new StockDetail();
		stockDetail.setCompName("PADINI Holding Malaysia");
		stockDetail.setCompDesc("one stop shopping");
		stockDetail.setRemark("vinci vinci");
		stockDetail.setListedDate(new Date());
 
		stock.setStockDetail(stockDetail);
		stockDetail.setStock(stock);
 
		session.save(stock);
		session.getTransaction().commit();
		org.hibernate.Query createQuery = session.createQuery("From Stock");

		@SuppressWarnings("unchecked")
		List<Stock> resultList = createQuery.list();
		System.out.println("num of Stock:" + resultList.size());
		for (Stock next : resultList) {
			System.out.println("next Stock: " + next);
		}
		session.close();
		HibernateUtil.getSessionFactory().close();
	}

}
