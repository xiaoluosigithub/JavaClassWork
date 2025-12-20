package servlet;

import service.UserService;
import service.impl.UserServiceImpl;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register") // 
public class RegisterServlet extends HttpServlet {
    private boolean isAjax(HttpServletRequest req) {
        String h = req.getHeader("X-Requested-With");
        String p = req.getParameter("ajax");
        return (h != null && h.equalsIgnoreCase("XMLHttpRequest")) || (p != null && p.equalsIgnoreCase("true"));
    }

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

    private final UserService userService = new UserServiceImpl();
    
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
            if (isAjax(req)) {
                writeJson(resp, false, "用户名或密码不能为空", "");
            } else {
                req.setAttribute("error", "用户名或密码不能为空");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
            return;
        }

        if (userService.existsByUserName(username)) {
            if (isAjax(req)) {
                writeJson(resp, false, "用户名已存在", "");
            } else {
                req.setAttribute("error", "用户名已存在");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
            return;
        }

        boolean ok = userService.register(username, password);
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
