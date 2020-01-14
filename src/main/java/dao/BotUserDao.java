package dao;

import models.BotUser;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

import static utils.HibernateSessionFactoryUtil.getSessionFactory;

public class BotUserDao {

    public BotUser findById(BigInteger id) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        BotUser botUser = session.get(BotUser.class, id);
        session.getTransaction().commit();
        session.close();
        return botUser;
    }

    public void save(BotUser botUser) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.save(botUser);
        session.getTransaction().commit();
        session.close();
    }
    public void update(BotUser botUser) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.update(botUser);
        session.getTransaction().commit();
        session.close();
    }

    public BotUser getUserByUserName(String botUserName) {

        // SELECT bot_user_name FROM bot_user WHERE bot_user_name = ""+botUserName+"""
        EntityManager em = Persistence.createEntityManagerFactory("BotUser").createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BotUser> botUserCriteria = cb.createQuery(BotUser.class);
        Root<BotUser> botUserRoot = botUserCriteria.from(BotUser.class);

        botUserCriteria.select(botUserRoot);
        botUserCriteria.where(cb.equal(botUserRoot.get("botUserName"), botUserName));

        TypedQuery<BotUser> query = em.createQuery(botUserCriteria);

        BotUser botUser = query.getSingleResult();
        em.close();
        return botUser;
    }

    public List<BotUser> getAllBotUsers() {
        EntityManager em = Persistence.createEntityManagerFactory("BotUser").createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BotUser> botUserCriteria = cb.createQuery(BotUser.class);
        Root<BotUser> botUserRoot = botUserCriteria.from(BotUser.class);

        em.getTransaction().begin();
        TypedQuery<BotUser> query = em.createQuery(botUserCriteria);
        List<BotUser> results = query.getResultList();
        botUserCriteria.select(botUserRoot);
        em.close();
        return results;
    }

    public void delete(BotUser botUser) {

        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(botUser);
        session.getTransaction().commit();
        session.close();
    }

}
