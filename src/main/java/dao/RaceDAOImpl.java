package dao;

import models.Race;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;
import java.util.List;

public class RaceDAOImpl implements DAO<Race> {
    @Override
    public Race findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Race.class, id);
    }

    @Override
    public List<Race> getAll() {
        List<Race> races = (List<Race>) HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Race").list();
        return races;
    }

    @Override
    public void save(Race race) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(race);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Race race) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(race);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Race race) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(race);
        tx1.commit();
        session.close();
    }
}
