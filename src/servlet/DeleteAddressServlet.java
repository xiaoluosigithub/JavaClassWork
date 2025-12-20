package servlet;

import service.AddressService;
import service.impl.AddressServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/deleteAddress")
public class DeleteAddressServlet extends HttpServlet {
    // 通过业务层处理逻辑，实例化地址服务实现类
    private final AddressService addressService = new AddressServiceImpl();
    // 处理 POST 请求，根据 ID 删除地址
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        try {
            long id = Long.parseLong(req.getParameter("id"));
            boolean ok = addressService.deleteById(id);
            String m = ok ? "删除成功" : "删除失败";
            String json = "{" +
                    "\"success\":" + (ok ? "true" : "false") + "," +
                    "\"message\":\"" + m + "\"," +
                    "\"redirect\":\"\"" +
                    "}";
            resp.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            String m = e.getMessage() == null ? "" : e.getMessage().replace("\\","\\\\").replace("\"","\\\"");
            String json = "{\"success\":false,\"message\":\"系统错误：" + m + "\",\"redirect\":\"\"}";
            resp.getWriter().write(json);
        }
    }
}
