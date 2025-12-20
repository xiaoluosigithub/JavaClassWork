// 声明包名，表示该类属于servlet包
package servlet;

// 导入所需的类
import pojo.Address;                     // 导入地址数据访问对象
import service.AddressService;          // 导入地址服务接口
import service.impl.AddressServiceImpl; // 导入地址服务实现类

import javax.servlet.*;                // 导入Servlet API
import javax.servlet.http.*;           // 导入HTTP Servlet API
import javax.servlet.annotation.WebServlet; // 导入WebServlet注解
import java.io.IOException;             // 导入IO异常处理类
import java.util.Date;                  // 导入日期类

/**
 * 处理新增地址的 Servlet
 */
@WebServlet("/addAddress")  // 使用@WebServlet注解映射URL路径为/addAddress
public class AddAddressServlet extends HttpServlet {

    // 通过业务层处理逻辑，实例化地址服务实现类
    private final AddressService addressService = new AddressServiceImpl();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 设置请求和响应的字符编码及内容类型
        req.setCharacterEncoding("UTF-8");       // 设置请求编码为UTF-8
        resp.setContentType("text/html;charset=UTF-8"); // 设置响应内容类型及编码

        try {
            // 封装请求参数到Address对象中
            Address address = new Address();
            address.setContact(req.getParameter("contact"));         // 获取联系人姓名
            address.setAddressDesc(req.getParameter("addressDesc")); // 获取地址详情
            address.setPostCode(req.getParameter("postCode"));       // 获取邮政编码
            address.setTel(req.getParameter("tel"));                 // 获取联系电话
            address.setCreatedBy(Long.parseLong(req.getParameter("createdBy"))); // 获取创建者ID
            address.setCreationDate(new Date());                      // 设置创建时间为当前时间

            // 调用业务逻辑执行地址添加操作
            boolean success = addressService.addAddress(address);
            if (success) {
                resp.getWriter().println("<script>alert('添加成功');location.href='add_address.jsp';</script>");
            } else {
                resp.getWriter().println("<script>alert('添加失败');location.href='add_address.jsp';</script>");
            }
        } catch (Exception e) {
            // 捕获并打印异常堆栈信息，并向客户端返回错误消息
            e.printStackTrace();
            resp.getWriter().println("❌ 系统错误：" + e.getMessage());
        }
    }
}
