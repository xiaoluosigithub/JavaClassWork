package servlet;

import service.UserService;
import service.impl.UserServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login") // ✅ 关键：添加映射
public class LoginServlet extends HttpServlet {

    // 登录服务
    private final UserService userService = new UserServiceImpl();

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

    // 处理 GET 请求，显示登录页面
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 转发到登录页面
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    // 处理 POST 请求，登录验证
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 从请求参数中获取用户名和密码
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

        // 调用登录服务验证用户名和密码
        boolean ok = userService.login(username, password);
        if (ok) {
            // 登录成功，创建会话并存储用户名
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", username);
            if (isAjax(req)) {
                writeJson(resp, true, "登录成功", req.getContextPath() + "/index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }
            return;
        }
        if (isAjax(req)) {
            writeJson(resp, false, "用户名或密码错误", "");
        } else {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
