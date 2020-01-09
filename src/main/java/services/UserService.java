package services;

import dao.DAO;
import dao.UserDAOImpl;
import models.User;

import java.util.List;

public class UserService {

    private DAO <User> userDAOImpl = new UserDAOImpl();

    public UserService() {
    }

    public User findUser(int id) {
        return userDAOImpl.findById(id);
    }

//    public User finByUniqueField(String user_name) { return userDAOImpl.finByUniqueField(user_name); }

    public void saveUser(User user) {
        userDAOImpl.save(user);
    }

    public void deleteUser(User user) {
        userDAOImpl.delete(user);
    }

    public void updateUser(User user) {
        userDAOImpl.update(user);
    }

    public List<User> findAllUsers() {
        return userDAOImpl.getAll();
    }

}
