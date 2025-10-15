package servlet;

import dao.DBUtil;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 添加地址信息的Servlet
 * 处理用户提交的地址信息并保存到数据库
 */
@WebServlet("/addAddress")
public class AddAddressServlet extends HttpServlet {
    /**
     * 处理POST请求，接收地址表单数据并插入数据库
     *
     * @param req  HttpServletRequest对象，包含客户端请求数据
     * @param resp HttpServletResponse对象，用于向客户端发送响应
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 设置请求和响应的字符编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        // 获取表单参数
        String contact = req.getParameter("contact");           // 联系人
        String addressDesc = req.getParameter("addressDesc");   // 地址详情
        String postCode = req.getParameter("postCode");         // 邮政编码
        String tel = req.getParameter("tel");                   // 联系电话
        String createdBy = req.getParameter("createdBy");       // 创建者ID
        String userId = req.getParameter("userId");             // 用户ID

        try (Connection conn = DBUtil.getConnection()) {
            // 准备SQL插入语句
            String sql = "INSERT INTO smbms_address (contact, addressDesc, postCode, tel, createdBy, creationDate, userId) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 设置SQL参数
            ps.setString(1, contact);
            ps.setString(2, addressDesc);
            ps.setString(3, postCode);
            ps.setString(4, tel);
            ps.setLong(5, Long.parseLong(createdBy));
            ps.setLong(6, Long.parseLong(userId));

            // 执行插入操作
            int result = ps.executeUpdate();
            if (result > 0) {
                resp.getWriter().println("✅ 地址添加成功！");
            } else {
                resp.getWriter().println("❌ 地址添加失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("❌ 数据库错误：" + e.getMessage());
        }
    }
}
