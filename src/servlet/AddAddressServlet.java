package servlet;

import dao.Address;
import service.AddressService;
import service.impl.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Date;

/**
 * 处理新增地址的 Servlet
 */
@WebServlet("/addAddress")
public class AddAddressServlet extends HttpServlet {

    // 通过业务层处理逻辑
    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // 封装请求参数
            Address address = new Address();
            address.setContact(req.getParameter("contact"));
            address.setAddressDesc(req.getParameter("addressDesc"));
            address.setPostCode(req.getParameter("postCode"));
            address.setTel(req.getParameter("tel"));
            address.setCreatedBy(Long.parseLong(req.getParameter("createdBy")));
            address.setCreationDate(new Date());

            // 调用业务逻辑
            boolean success = addressService.addAddress(address);

            resp.getWriter().println(success ? "✅ 地址添加成功！" : "❌ 地址添加失败！");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 系统错误：" + e.getMessage());
        }
    }
}
