package services;

import dao.DAO;
import dao.UserDAOImpl;
import models.Race;
import models.User;

import java.util.List;

public class UserService {

    private DAO<User, Race> userDAOImpl = new UserDAOImpl();

    public UserService() {
    }

    public User findUser(int id) {
        return userDAOImpl.findById(id);
    }

    public User getEntityByString(String userName) {
        return userDAOImpl.getEntityByString(userName);
    }

    public void saveUser(User user) {
        userDAOImpl.save(user);
    }

    public void deleteUser(User user) {
        userDAOImpl.delete(user);
    }

    public void updateUser(User user, Race race) {
        userDAOImpl.update(user, race);
    }

    public void updateUser(User user) {
        userDAOImpl.update(user);
    }

    public List<User> findAllUsers() {
        return userDAOImpl.getAll();
    }

}
