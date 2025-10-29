package servlet;

import dao.Address;
import service.AddressService;
import service.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@WebServlet("/addAddress")
public class AddAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Address address = new Address();
        address.setContact(req.getParameter("contact"));
        address.setAddressDesc(req.getParameter("addressDesc"));
        address.setPostCode(req.getParameter("postCode"));
        address.setTel(req.getParameter("tel"));
        address.setCreatedBy(Long.parseLong(req.getParameter("createdBy")));
        address.setCreationDate(new Date());

        boolean success = addressService.addAddress(address);
        resp.getWriter().println(success ? "✅ 地址添加成功！" : "❌ 地址添加失败！");
    }
}
