package dao;

import java.util.List;

public interface DAO<T, ID> {
	
	T findById(ID id);

	List<T> get10(int numPage);
	
	List<T> getAll();

	int insert(T t);

	int update(T t);

	int delete(T id);
}
