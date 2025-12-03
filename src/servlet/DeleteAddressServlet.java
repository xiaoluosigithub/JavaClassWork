package servlet;

import service.AddressService;
import service.impl.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/deleteAddress")
public class DeleteAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            long id = Long.parseLong(req.getParameter("id"));
            boolean ok = addressService.deleteById(id);
            String keyword = req.getParameter("keyword");
            String page = req.getParameter("page");
            String pageSize = req.getParameter("pageSize");
            if (ok) {
                resp.sendRedirect("queryAddress?keyword=" + (keyword==null?"":keyword) + "&page=" + (page==null?"1":page) + "&pageSize=" + (pageSize==null?"5":pageSize));
            } else {
                resp.getWriter().println("❌ 删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 系统错误：" + e.getMessage());
        }
    }
}
