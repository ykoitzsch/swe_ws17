package repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import hibernate.HibernateUtil;
import model.Model;


public class RepositoryImpl<T extends Model> implements IRepository<T>{

	private Class<T> type;
	public RepositoryImpl(Class<T> type){
		this.type = type;
		Logger.getLogger(getClass().getName()).log(Level.INFO, "ClassType of sub-class is " + this.type.toString());
	}
	
	private Transaction tx = null;
	private Session session = null;
	
	@Override
	public void save(T entity) {
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(entity);
			session.flush();
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null) session.close();
		}
	}
	
	@Override
	public void update(T entity) {
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.update(entity);
			session.flush();
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null) session.close();
		}
	}

	@Override
	public void delete(T entity) {
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.delete(session.merge(entity));
			session.flush();
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null) session.close();
		}
	}

	@Override
	public boolean exists(T entity) {
		boolean aux = false;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			aux = find(entity.getId()).isPresent();
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null) session.close();
		}
		return aux;
	}
	@Override
	public Optional<T> find(Integer id) {
		T object = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			object = session.load(type, id);
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null) session.close();
		}
		return Optional.of(object);
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(Predicate predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> where(String column, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> query(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}
