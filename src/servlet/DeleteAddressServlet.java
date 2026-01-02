package servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
            JSONObject obj = new JSONObject();
            obj.put("success", ok);
            obj.put("message", m);
            obj.put("redirect", "");
            resp.getWriter().write(JSON.toJSONString(obj));
        } catch (Exception e) {
            e.printStackTrace();
            String m = e.getMessage() == null ? "" : e.getMessage();
            JSONObject obj = new JSONObject();
            obj.put("success", false);
            obj.put("message", "系统错误：" + m);
            obj.put("redirect", "");
            resp.getWriter().write(JSON.toJSONString(obj));
        }
    }
}
