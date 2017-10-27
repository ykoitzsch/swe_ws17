package hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory = null;
	
	private static SessionFactory configureSessionFactory() throws HibernateException{
		if(sessionFactory == null){
			sessionFactory = new Configuration().configure().buildSessionFactory();	
		}
		return sessionFactory;
	}
	
	public static SessionFactory getSessionFactory(){
		return configureSessionFactory();
	}	
}
