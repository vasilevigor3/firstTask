package dao;

import models.Race;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.List;

import static utils.HibernateSessionFactoryUtil.getSessionFactory;

public class RaceDAOImpl implements DAO <Race,User> {

    @Override
    public Race findById(int id) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        Race race = session.get(Race.class, id);
        session.getTransaction().commit();
        session.close();
        return race;
    }

    @Override
    public List<Race> getAll() {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        List<Race> races = session.createQuery("From Race").list();
//        List<Race> races = (List<Race>) getSessionFactory()
//                .openSession().createQuery("From Race").list();
        session.getTransaction().commit();
        session.close();
        return races;
    }

    @Override
    public void update(Race race) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.update(race);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(Race race, User user) {
        Session session = getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        race.addUserToTheRace(user);
        session.update(race);
        tx1.commit();
        session.close();
    }

    @Override
    public void save(Race race) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(race);
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public void delete(Race race) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(race);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Race getEntityByString(String dateOfRace){

        Session session = getSessionFactory().openSession();
        Criteria userCriteria = session.createCriteria(Race.class);
        userCriteria.add(Restrictions.eq("dateOfRace", dateOfRace));
        Race race = (Race) userCriteria.uniqueResult();
        session.close();
        return race;
    }

    //TODO
    public List<Race> getEntityByString2(String s){
        s = "2020-01-21";
        String hql = "FROM Race where date_of_race = '"+ s +"'";
        Query query = getSessionFactory().openSession().createQuery(hql);
        List<Race> races = query.list();
        return races;
    }
}
