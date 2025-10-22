package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通用数据库操作类，封装增删改等基础方法
 */
public abstract class BaseDao {

    // 通用的更新方法：INSERT、UPDATE、DELETE
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 通用的查询模板方法，子类可重写 parseResult() 来解析结果集
    protected void executeQuery(String sql, ResultSetHandler handler, Object... params) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                handler.handle(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 用于解析结果集的接口
    public interface ResultSetHandler {
        void handle(ResultSet rs) throws SQLException;
    }
}
