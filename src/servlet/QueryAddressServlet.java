package servlet;

import dao.DBUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 地址查询Servlet
 * 处理地址信息的查询请求，支持关键字搜索和全部查询
 */
@WebServlet("/queryAddress")
public class QueryAddressServlet extends HttpServlet {
    /**
     * 处理GET请求，根据关键字查询地址信息
     *
     * @param req  HttpServletRequest对象，包含客户端请求数据
     * @param resp HttpServletResponse对象，用于向客户端发送响应
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 设置请求和响应的字符编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        // 从请求参数中获取搜索关键字
        String keyword = req.getParameter("keyword"); // 从前端表单获取关键字
        // 创建存储查询结果的列表
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {
            // 声明PreparedStatement对象
            PreparedStatement ps;

            // 根据关键字是否存在决定查询策略
            if (keyword == null || keyword.trim().isEmpty()) {
                // 不输入关键字时，显示所有数据
                ps = conn.prepareStatement("SELECT * FROM smbms_address");
            } else {
                // 精确匹配 contact 或 addressDesc
                String sql = "SELECT * FROM smbms_address WHERE contact = ? OR addressDesc = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, keyword);
                ps.setString(2, keyword);
            }

            // 执行查询并处理结果集
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // 将每条记录封装成HashMap对象
                HashMap<String, String> row = new HashMap<>();
                row.put("id", rs.getString("id"));
                row.put("contact", rs.getString("contact"));
                row.put("addressDesc", rs.getString("addressDesc"));
                row.put("postCode", rs.getString("postCode"));
                row.put("tel", rs.getString("tel"));
                row.put("createdBy", rs.getString("createdBy"));
                row.put("creationDate", rs.getString("creationDate"));
                // 将记录添加到结果列表中
                list.add(row);
            }

            // 将查询结果存储到请求域中，并转发到JSP页面展示
            req.setAttribute("addressList", list);
            req.getRequestDispatcher("query_address.jsp").forward(req, resp);

        } catch (Exception e) {
            // 异常处理：打印堆栈信息并向客户端返回错误信息
            e.printStackTrace();
            resp.getWriter().println("❌ 查询失败：" + e.getMessage());
        }
    }
}
