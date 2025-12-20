package service;

public interface UserService {
    // 用户登录
    boolean login(String username, String password);

    // 检查用户名是否已存在
    boolean existsByUserName(String username);

    // 用户注册
    boolean register(String username, String password);
}

