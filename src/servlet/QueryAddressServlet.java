package servlet;

import dao.Address;
import service.AddressService;
import service.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/queryAddress")
public class QueryAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String keyword = req.getParameter("keyword");
        List<Address> list = addressService.searchAddresses(keyword);

        req.setAttribute("addressList", list);
        req.getRequestDispatcher("query_address.jsp").forward(req, resp);
    }
}
