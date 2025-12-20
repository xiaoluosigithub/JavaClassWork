package service.impl;

import service.UserService;
import dao.UserDao;
import dao.impl.UserDaoImpl;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        return userDao.validateCredentials(username.trim(), password);
    }

    @Override
    public boolean existsByUserName(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return userDao.existsByUserName(username.trim());
    }

    @Override
    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        if (userDao.existsByUserName(username.trim())) {
            return false;
        }
        return userDao.insertUser(username.trim(), password) > 0;
    }
}

