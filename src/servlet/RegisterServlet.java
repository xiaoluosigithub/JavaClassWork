package servlet;

import service.UserService;
import service.impl.UserServiceImpl;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register") // 
public class RegisterServlet extends HttpServlet {

    // 注册服务实例
    private final UserService userService = new UserServiceImpl();

    // 检查请求是否为 AJAX 请求
    private boolean isAjax(HttpServletRequest req) {
        String h = req.getHeader("X-Requested-With");
        String p = req.getParameter("ajax");
        return (h != null && h.equalsIgnoreCase("XMLHttpRequest")) || (p != null && p.equalsIgnoreCase("true"));
    }

    // 写入 JSON 响应
    private void writeJson(HttpServletResponse resp, boolean success, String message, String redirect) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String r = redirect == null ? "" : redirect;
        String m = message == null ? "" : message.replace("\\", "\\\\").replace("\"", "\\\"");
        String json = "{" +
                "\"success\":" + (success ? "true" : "false") + "," +
                "\"message\":\"" + m + "\"," +
                "\"redirect\":\"" + r + "\"" +
                "}";
        resp.getWriter().write(json);
    }
    
    // 处理 GET 请求，展示注册表单页
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 转发是服务器内部跳转，请求/响应对象保持不变，适合展示表单页与错误信息
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
    
    // 处理 POST 请求，处理注册表单提交
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 从请求参数中获取用户名和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // 验证用户名和密码是否为空
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            // 如果用户名或密码为空，根据是否为 AJAX 请求返回不同响应
            if (isAjax(req)) {
                writeJson(resp, false, "用户名或密码不能为空", "");
            } else {
                req.setAttribute("error", "用户名或密码不能为空");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
            return;
        }
        // 检查用户名是否已存在
        if (userService.existsByUserName(username)) {
            // 如果用户名已存在，根据是否为 AJAX 请求返回不同响应
            if (isAjax(req)) {
                writeJson(resp, false, "用户名已存在", "");
            } else {
                req.setAttribute("error", "用户名已存在");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
            return;
        }
        // 调用服务层注册方法，注册用户
        boolean ok = userService.register(username, password);
        // 如果注册成功，根据是否为 AJAX 请求返回不同响应
        if (ok) {
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", username);
            if (isAjax(req)) {
                writeJson(resp, true, "注册成功", req.getContextPath() + "/index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }
        } else {
            if (isAjax(req)) {
                writeJson(resp, false, "注册失败，请重试", "");
            } else {
                req.setAttribute("error", "注册失败，请重试");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
        }
    }
}
