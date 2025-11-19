package servlet;

import dao.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login") // ✅ 关键：添加映射
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "用户名或密码不能为空");
            // 跳转到源页面，并显示错误信息
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 获取用户信息
        String sql = "SELECT id, userName, userPassword FROM smbms_user WHERE userName = ?";
        // 创建数据库连接
        try (Connection conn = DBUtil.getConnection();
             // 创建一个 预编译 SQL 语句对象。
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 将第一个 参数设置为用户名
            ps.setString(1, username);
            // 执行查询 得到 rs 结果集
            try (ResultSet rs = ps.executeQuery()) {
                // 数据库中是否存在 匹配的用户名和密码？
                if (rs.next()) {
                    // 获取数据库中的密码
                    String dbPwd = rs.getString("userPassword");
                    // 判断密码是否匹配
                    if (password.equals(dbPwd)) {
                        HttpSession session = req.getSession(true);
                        // 存储当前用户
                        session.setAttribute("currentUser", username);
                        // 重定向到主页
                        resp.sendRedirect(req.getContextPath() + "/index.jsp");
                        return;
                    }
                }
            }

            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "登录异常：" + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
