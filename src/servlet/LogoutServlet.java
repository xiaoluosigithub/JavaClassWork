package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    // 处理 GET 请求，注销用户
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 获取当前会话，不创建新会话
        HttpSession session = req.getSession(false);
        if (session != null) {
            // 移除当前用户属性
            session.removeAttribute("currentUser");
            // 使会话无效，销毁会话
            session.invalidate();
        }
        // 重定向到首页 
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    // 处理 POST 请求，注销用户
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}