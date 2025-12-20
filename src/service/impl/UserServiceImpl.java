package service.impl;

import service.UserService;
import dao.UserDao;
import dao.impl.UserDaoImpl;

public class UserServiceImpl implements UserService {
    // 依赖注入UserDao
    private final UserDao userDao = new UserDaoImpl();

    // 登录验证
    @Override
    public boolean login(String username, String password) {
        // 验证用户名和密码是否为空
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        // 验证用户名和密码是否匹配
        return userDao.validateCredentials(username.trim(), password);
    }

    // 检查用户名是否存在   
    @Override
    public boolean existsByUserName(String username) {
        // 验证用户名是否为空
        if (username == null || username.trim().isEmpty()) return false;
        // 检查用户名是否存在
        return userDao.existsByUserName(username.trim());
    }

    // 注册新用户   
    @Override
    public boolean register(String username, String password) {
        // 验证用户名和密码是否为空
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }
        // 检查用户名是否已存在
        if (userDao.existsByUserName(username.trim())) {
            return false;
        }
        // 注册新用户
        return userDao.insertUser(username.trim(), password) > 0;
    }
}

