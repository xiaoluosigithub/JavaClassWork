package servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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

    // 处理 GET 请求，根据 ID 和联系人查询地址列表
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // 获取请求参数
            String idStr = req.getParameter("id");
            String contact = req.getParameter("contact");
            Long id = null;
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    id = Long.parseLong(idStr.trim());
                } catch (NumberFormatException ignored) {
                    id = null;
                }
            }
            // 获取分页参数
            String pStr = req.getParameter("page");
            // 获取每页记录数
            String psStr = req.getParameter("pageSize");
            // 获取当前页码
            int page = (pStr == null || pStr.isEmpty()) ? 1 : Integer.parseInt(pStr);
            // 获取每页记录数
            int pageSize = (psStr == null || psStr.isEmpty()) ? 20 : Integer.parseInt(psStr);
            // 获取总记录数
            int total = addressService.countAddresses(id, contact);
            // 计算最大页数
            int maxPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            // 校验页码
            if (page < 1) page = 1;
            if (maxPage > 0 && page > maxPage) page = maxPage;
            // 查询
            List<Address> list = addressService.searchAddresses(id, contact, page, pageSize);

            JSONObject root = new JSONObject();
            root.put("success", true);
            root.put("message", "");
            root.put("page", page);
            root.put("pageSize", pageSize);
            root.put("total", total);
            root.put("maxPage", maxPage);
            JSONArray arr = new JSONArray();
            for (Address a : list) {
                JSONObject item = new JSONObject();
                item.put("id", a.getId());
                item.put("contact", a.getContact() == null ? "" : a.getContact());
                item.put("addressDesc", a.getAddressDesc() == null ? "" : a.getAddressDesc());
                item.put("postCode", a.getPostCode() == null ? "" : a.getPostCode());
                item.put("tel", a.getTel() == null ? "" : a.getTel());
                item.put("userId", a.getUserId() == null ? 0 : a.getUserId());
                item.put("createdBy", a.getCreatedBy() == null ? 0 : a.getCreatedBy());
                item.put("creationDate", a.getCreationDate() == null ? "" : a.getCreationDate().toString());
                arr.add(item);
            }
            root.put("list", arr);
            resp.getWriter().write(JSON.toJSONString(root));
        } catch (Exception e) {
            e.printStackTrace();
            String m = e.getMessage() == null ? "" : e.getMessage();
            JSONObject obj = new JSONObject();
            obj.put("success", false);
            obj.put("message", "查询失败：" + m);
            obj.put("redirect", "");
            resp.getWriter().write(JSON.toJSONString(obj));
        }
    }
}
