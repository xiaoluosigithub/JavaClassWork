package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseDao {
    // 执行更新操作（INSERT、UPDATE、DELETE）   
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
             PreparedStatement ps = conn.prepareStatement(sql)) { // 预编译 SQL 语句
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]); // 设置参数
            }
            return ps.executeUpdate(); // 执行更新操作，返回受影响行数
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常栈 trace
        }
        return 0; // 如果发生异常，返回 0 表示操作失败  
    }
}
