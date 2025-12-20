package servlet;

import pojo.Address;
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

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // 获取请求参数
            String keyword = req.getParameter("keyword");
            // 获取分页参数
            String pStr = req.getParameter("page");
            // 获取每页记录数
            String psStr = req.getParameter("pageSize");
            // 获取当前页码
            int page = (pStr == null || pStr.isEmpty()) ? 1 : Integer.parseInt(pStr);
            // 获取每页记录数
            int pageSize = (psStr == null || psStr.isEmpty()) ? 5 : Integer.parseInt(psStr);
            // 获取总记录数
            int total = addressService.countAddresses(keyword);
            // 计算最大页数
            int maxPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            // 校验页码
            if (page < 1) page = 1;
            if (maxPage > 0 && page > maxPage) page = maxPage;
            // 查询
            List<Address> list = addressService.searchAddresses(keyword, page, pageSize);

            // 设置数据
            req.setAttribute("addressList", list);
            req.setAttribute("keyword", keyword);
            req.setAttribute("page", page);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("total", total);
            req.setAttribute("maxPage", maxPage);

            // 转发
            req.getRequestDispatcher("query_address.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 查询失败：" + e.getMessage());
        }
    }
}
