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
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        String sql = "SELECT id, userName, userPassword FROM smbms_user WHERE userName = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPwd = rs.getString("userPassword");
                    if (password.equals(dbPwd)) {
                        HttpSession session = req.getSession(true);
                        session.setAttribute("currentUser", username);
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
