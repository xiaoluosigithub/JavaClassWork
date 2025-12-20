package dao;

public interface UserDao {
    boolean existsByUserName(String userName);
    boolean validateCredentials(String userName, String password);
    int insertUser(String userName, String password);
}

