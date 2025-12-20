package servlet;

import pojo.Address;
import service.AddressService;
import service.impl.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Date;

@WebServlet("/updateAddress")
public class UpdateAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            String idStr = req.getParameter("id");
            long id = Long.parseLong(idStr);
            Address address = addressService.getById(id);
            req.setAttribute("address", address);
            req.setAttribute("contactFilter", req.getParameter("contact"));
            req.setAttribute("id", req.getParameter("idParam"));
            req.setAttribute("page", Integer.parseInt(req.getParameter("page") == null ? "1" : req.getParameter("page")));
            req.setAttribute("pageSize", Integer.parseInt(req.getParameter("pageSize") == null ? "20" : req.getParameter("pageSize")));
            req.getRequestDispatcher("edit_address.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 加载编辑页失败：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        try {
            Address a = new Address();
            a.setId(Long.parseLong(req.getParameter("id")));
            a.setContact(req.getParameter("contact"));
            a.setAddressDesc(req.getParameter("addressDesc"));
            a.setPostCode(req.getParameter("postCode"));
            a.setTel(req.getParameter("tel"));
            String uid = req.getParameter("userId");
            if (uid != null && !uid.isEmpty()) a.setUserId(Long.parseLong(uid));
            String mby = req.getParameter("modifyBy");
            if (mby != null && !mby.isEmpty()) a.setModifyBy(Long.parseLong(mby));
            a.setModifyDate(new Date());

            boolean ok = addressService.update(a);
            String redirect = req.getContextPath() + "/query_address.jsp";
            String m = ok ? "修改成功" : "更新失败";
            writeJson(resp, ok, m, ok ? redirect : "");
        } catch (Exception e) {
            e.printStackTrace();
            String m = e.getMessage() == null ? "" : e.getMessage().replace("\\","\\\\").replace("\"","\\\"");
            writeJson(resp, false, "系统错误：" + m, "");
        }
    }
}
