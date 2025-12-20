package dao.impl;

import dao.UserDao;
import dao.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    // 检查用户名是否存在
    @Override
    public boolean existsByUserName(String userName) {
        // 检查用户名是否存在的 SQL 语句，根据 userName 查询是否存在
        String sql = "SELECT 1 FROM smbms_user WHERE userName = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // 如果结果集有数据，说明用户名存在，返回 true；否则返回 false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean validateCredentials(String userName, String password) {
        // 验证用户名和密码是否匹配的 SQL 语句，根据 userName 查询密码  
        String sql = "SELECT userPassword FROM smbms_user WHERE userName = ?";
        // 执行查询，根据 userName 查询密码
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // 设置查询参数，将 userName 绑定到 SQL 语句的第一个占位符
            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 从结果集中获取数据库存储的密码
                    String dbPwd = rs.getString("userPassword");
                    // 比较输入的密码与数据库存储的密码是否匹配
                    // 如果输入的密码不为 null 且与数据库存储的密码相等，返回 true；否则返回 false
                    return password != null && password.equals(dbPwd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // 插入新用户
    @Override
    public int insertUser(String userName, String password) {
        // 插入新用户的 SQL 语句，将 userName 和 password 插入到 smbms_user 表中    
        String sql = "INSERT INTO smbms_user(userName, userPassword, creationDate) VALUES (?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // 设置插入参数，将 userName 和 password 绑定到 SQL 语句的第一个和第二个占位符
            ps.setString(1, userName);
            ps.setString(2, password);
            // 执行插入操作，返回受影响行数 （成功插入一行返回 1，失败返回 0）
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

