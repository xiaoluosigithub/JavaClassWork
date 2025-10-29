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

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // 获取关键字并调用业务逻辑层
            String keyword = req.getParameter("keyword");
            List<Address> list = addressService.searchAddresses(keyword);

            // 将结果传递到 JSP
            req.setAttribute("addressList", list);
            req.getRequestDispatcher("query_address.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 查询失败：" + e.getMessage());
        }
    }
}
