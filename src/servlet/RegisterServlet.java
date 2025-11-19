package servlet;

import dao.DBUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register") // 
public class RegisterServlet extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 转发是服务器内部跳转，请求/响应对象保持不变，适合展示表单页与错误信息
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "用户名或密码不能为空");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        String checkSql = "SELECT id FROM smbms_user WHERE userName = ?";
        String insertSql = "INSERT INTO smbms_user(userName, userPassword, creationDate) VALUES (?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection()) {

            // 检查是否存在
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        req.setAttribute("error", "用户名已存在");
                        req.getRequestDispatcher("/register.jsp").forward(req, resp);
                        return;
                    }
                }
            }

            // 插入新用户
            try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                ps2.setString(1, username);
                ps2.setString(2, password);
                int rows = ps2.executeUpdate();

                if (rows > 0) {
                    HttpSession session = req.getSession(true);
                    session.setAttribute("currentUser", username);
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                } else {
                    req.setAttribute("error", "注册失败，请重试");
                    req.getRequestDispatcher("/register.jsp").forward(req, resp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "注册异常：" + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
