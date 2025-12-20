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

            StringBuilder sb = new StringBuilder();
            sb.append("{\"success\":true,");
            sb.append("\"message\":\"\",");
            sb.append("\"page\":").append(page).append(",");
            sb.append("\"pageSize\":").append(pageSize).append(",");
            sb.append("\"total\":").append(total).append(",");
            sb.append("\"maxPage\":").append(maxPage).append(",");
            sb.append("\"list\":[");
            for (int i = 0; i < list.size(); i++) {
                Address a = list.get(i);
                String contactVal = a.getContact() == null ? "" : a.getContact().replace("\\","\\\\").replace("\"","\\\"");
                String addrDesc = a.getAddressDesc() == null ? "" : a.getAddressDesc().replace("\\","\\\\").replace("\"","\\\"");
                String postCode = a.getPostCode() == null ? "" : a.getPostCode().replace("\\","\\\\").replace("\"","\\\"");
                String tel = a.getTel() == null ? "" : a.getTel().replace("\\","\\\\").replace("\"","\\\"");
                String creation = a.getCreationDate() == null ? "" : a.getCreationDate().toString().replace("\"","\\\"");
                sb.append("{")
                  .append("\"id\":").append(a.getId()).append(",")
                  .append("\"contact\":\"").append(contactVal).append("\",")
                  .append("\"addressDesc\":\"").append(addrDesc).append("\",")
                  .append("\"postCode\":\"").append(postCode).append("\",")
                  .append("\"tel\":\"").append(tel).append("\",")
                  .append("\"userId\":").append(a.getUserId()==null?0:a.getUserId()).append(",")
                  .append("\"createdBy\":").append(a.getCreatedBy()==null?0:a.getCreatedBy()).append(",")
                  .append("\"creationDate\":\"").append(creation).append("\"")
                  .append("}");
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]}");
            resp.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            String m = e.getMessage() == null ? "" : e.getMessage().replace("\\","\\\\").replace("\"","\\\"");
            String json = "{\"success\":false,\"message\":\"查询失败：" + m + "\",\"redirect\":\"\"}";
            resp.getWriter().write(json);
        }
    }
}
