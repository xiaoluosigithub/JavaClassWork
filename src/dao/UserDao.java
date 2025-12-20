package dao;

public interface UserDao {
    // 检查用户名是否存在
    boolean existsByUserName(String userName); 
    
    // 验证用户名和密码是否匹配
    boolean validateCredentials(String userName, String password); 
    
    // 插入新用户
    int insertUser(String userName, String password); 
}

