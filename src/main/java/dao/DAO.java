package dao;


import java.util.List;

public interface DAO<T,P> {

    T findById(int id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void update(T user, P race);

    void delete(T t);

    T getEntityByString(String s);


}
