package dao;

import models.Race;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import static utils.HibernateSessionFactoryUtil.getSessionFactory;

public class UserDAOImpl implements DAO <User,Race> {

    @Override
    public User findById(int id) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.getTransaction().commit();
        session.close();
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = (List<User>)  getSessionFactory()
                .openSession().createQuery("From User").list();
        return users;
    }

    @Override
    public void update(User user) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void save(User user) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(User user, Race race) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        user.addRaceToUser(race);
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }
    @Override
    public User getEntityByString(String userName){
        Session session = getSessionFactory().openSession();
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("userName", userName));
        User user = (User) userCriteria.uniqueResult();
        session.close();
        return user;
    }

    }
