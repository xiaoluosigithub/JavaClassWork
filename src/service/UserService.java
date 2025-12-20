package service;

public interface UserService {
    boolean login(String username, String password);
    boolean existsByUserName(String username);
    boolean register(String username, String password);
}

