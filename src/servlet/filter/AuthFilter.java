package servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// 创建过滤器
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 设置请求和响应的字符编码为UTF-8
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        // 定义不需要进行认证的资源列表
        String uri = request.getRequestURI();
        if (uri.endsWith("login.jsp") || uri.endsWith("register.jsp")
                || uri.endsWith("login") || uri.endsWith("register")
                || uri.endsWith("index.jsp") || uri.equals(request.getContextPath() + "/")
                || uri.endsWith(".css") || uri.endsWith(".js")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查用户是否已登录
        HttpSession session = request.getSession();
        if (session.getAttribute("currentUser") == null) {
            // 未登录用户重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 已登录用户继续执行后续过滤器或目标资源
        chain.doFilter(request, response);
    }
}

