package hibernate.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IRepository<T> {
	public void save(T entity);
	public Optional<T> find(Integer id);
	public List<T> findAll();
	public List<T> findAll(Predicate<T> predicate);
	public void update(T entity);
	public void delete(T entity);
	public boolean exists(T entity);
	public List<T> where(String column, String value);
	public List<T> query(String sql);
}
