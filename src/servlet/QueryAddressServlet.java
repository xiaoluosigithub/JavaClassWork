package servlet;

import dao.Address;
import service.AddressService;
import service.impl.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

/**
 * 处理地址查询的 Servlet
 */
@WebServlet("/queryAddress")
public class QueryAddressServlet extends HttpServlet {

    // 创建业务逻辑层对象
    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 设置请求和响应的字符编码及内容类型
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // 获取关键字并调用业务逻辑层
            String keyword = req.getParameter("keyword");
            // 调用业务逻辑层
            List<Address> list = addressService.searchAddresses(keyword);

            // 将结果传递到 JSP
            req.setAttribute("addressList", list);
            // 转发
            req.getRequestDispatcher("query_address.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 查询失败：" + e.getMessage());
        }
    }
}
