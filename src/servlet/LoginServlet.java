package servlet;

import dao.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login") // ✅ 关键：添加映射
public class LoginServlet extends HttpServlet {
    // 判断是否是 ajax 请求
    private boolean isAjax(HttpServletRequest req) {
        String h = req.getHeader("X-Requested-With"); // 判断是否是 ajax 请求
        String p = req.getParameter("ajax"); // 判断是否是 ajax 请求
        // 返回结果
        return (h != null && h.equalsIgnoreCase("XMLHttpRequest")) || (p != null && p.equalsIgnoreCase("true"));
    }
    // 写入 JSON 响应 传入 成功、失败、错误信息、重定向地址
    private void writeJson(HttpServletResponse resp, boolean success, String message, String redirect) throws IOException {
        resp.setCharacterEncoding("UTF-8"); // 设置编码
        resp.setContentType("application/json;charset=UTF-8"); // 设置内容类型及编码
        String r = redirect == null ? "" : redirect; // 重定向地址
        // 创建 JSON 字符串 转义符号，避免冲突
        String m = message == null ? "" : message.replace("\\", "\\\\").replace("\"", "\\\"");
        String json = "{" +
                "\"success\":" + (success ? "true" : "false") + "," +
                "\"message\":\"" + m + "\"," +
                "\"redirect\":\"" + r + "\"" +
                "}"; // 创建 JSON 字符串
        resp.getWriter().write(json); // 写入 JSON
    }
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
            if (isAjax(req)) {
                writeJson(resp, false, "用户名或密码不能为空", "");
            } else {
                req.setAttribute("error", "用户名或密码不能为空");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
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
                        if (isAjax(req)) {
                            writeJson(resp, true, "登录成功", req.getContextPath() + "/index.jsp");
                        } else {
                            resp.sendRedirect(req.getContextPath() + "/index.jsp");
                        }
                        return;
                    }
                }
            }

            if (isAjax(req)) {
                writeJson(resp, false, "用户名或密码错误", "");
            } else {
                req.setAttribute("error", "用户名或密码错误");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (isAjax(req)) {
                writeJson(resp, false, "登录异常：" + e.getMessage(), "");
            } else {
                req.setAttribute("error", "登录异常：" + e.getMessage());
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
