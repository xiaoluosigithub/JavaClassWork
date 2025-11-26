**项目结构**

- 表现层（JSP）
    - `web/index.jsp` 入口，含登录/注册、显示在线人数与退出链接
    - `web/login.jsp` 登录
    - `web/register.jsp` 注册
    - `web/add_address.jsp` 新增地址
    - `web/query_address.jsp` 查询地址（支持分页：首页/上一页/下一页/尾页、页大小、跳页）
- 控制层（Servlet）
    - 登录：`servlet.LoginServlet` 处理登录 
    - 注册：`servlet.RegisterServlet` 处理注册
    - 退出登录：`servlet.LogoutServlet` 处理退出登录
    - 新增地址：`servlet.AddAddressServlet` 处理表单提交
    - 查询地址：`servlet.QueryAddressServlet` 处理查询、分页参数解析与转发 
- 过滤器（filter）
    - `filter.AuthFilter` 登录拦截
        - 放行列表：登录/注册页与静态资源；其他路径要求已登录
- 监听器（listener）
    - `listener.OnlineUserCounter` 会话监听器，统计在线用户，会话属性/销毁时 ++/--
- 业务层（Service）
    - 接口: `service.AddressService` 地址服务（新增 `countAddresses` 与分页版 `searchAddresses`）
      - 实现 `service.impl.AddressServiceImpl`
- 数据访问层（DAO）
    - 通用模板：`dao.BaseDao` 提供 `executeUpdate` 与 `executeQuery`
    - 连接工具：`dao.DBUtil` 读取 `db.properties` 并创建连接 
    - 地址 DAO：接口 `dao.AddressDao` 与实现 `dao.impl.AddressDaoImpl`（新增 `count` 与 `findList` 分页） 
    - 实体：`dao.Address` 实体类 

**核心流程**

- 登录
    - 表单提交到 `POST /login` → 查 `smbms_user` 比对明文密码 → 成功后在 Session 里放 `currentUser` → 跳转首页
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\LoginServlet.java:32-53`
- 注册
    - 表单提交到 `POST /register` → 先查重，再插入用户，登录并跳转
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\RegisterServlet.java:18-71`
- 鉴权
    - 过滤器放行登录/注册与静态资源；其它路径需要 Session 里有 `currentUser`
    - 代码参考：`e:\JavaWeb\demo01\src\filter\AuthFilter.java:22-33`
- 地址新增
    - 表单提交到 `POST /addAddress` → Service 校验 → DAO 插入
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\AddAddressServlet.java:25-54`、`e:\JavaWeb\demo01\src\dao\impl\AddressDaoImpl.java:12-23`
- 地址查询
    - `GET /queryAddress?keyword=...&page=...&pageSize=...` → Servlet 解析分页参数并做默认值 → Service 计算偏移量 → DAO 使用 `COUNT` 与 `LIMIT 偏移量, 固定条数` 查询 → 结果与分页信息放入 `request` → 转发到 JSP
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\QueryAddressServlet.java:29-48`、`e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:67-76`、`e:\JavaWeb\demo01\src\dao\impl\AddressDaoImpl.java:47-81`
- 注销与在线人数
    - `GET /logout` → 移除 `currentUser` 并失效会话，监听器触发在线人数 --，首页显示当前在线用户数
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\LogoutServlet.java:9-23`、`e:\JavaWeb\demo01\src\listener\OnlineUserCounter.java:1-49`、`e:\JavaWeb\demo01\web\index.jsp:23-30`

**关键代码理解**

- 编码与拦截
    - 编码设置位置：`AuthFilter.doFilter` 中 `request.setCharacterEncoding("utf-8")` 与 `response.setCharacterEncoding("utf-8")` `e:\JavaWeb\demo01\src\filter\AuthFilter.java:22-24`
    - 白名单策略：是否放行由 URI 结尾匹配决定 `e:\JavaWeb\demo01\src\filter\AuthFilter.java:26-33`
- JDBC 封装
    - `BaseDao` 用可变参数绑定占位符，`executeQuery` 使用回调解析结果集 `e:\JavaWeb\demo01\src\dao\BaseDao.java:22-45`
    - 连接初始化读取 `db.properties` 并 `Class.forName` 注册驱动 `e:\JavaWeb\demo01\src\dao\DBUtil.java:20-35`
- 业务分层
    - Service 负责参数校验与组合 DAO，体现“控制层不直接碰数据库”的分层思想 `e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:19-30`
    - 分页相关：`countAddresses` 与分页版 `searchAddresses` 负责计数与计算偏移量 `e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:67-76`
- 分页参数保护
    - Servlet 中对空字符串默认处理，避免 `NumberFormatException`：`e:\JavaWeb\demo01\src\servlet\QueryAddressServlet.java:31-35`
- 在线用户计数
    - 监听 `Session` 属性增删与会话销毁实现 ++/--，并提供 `getCount()` 供 JSP 显示 `e:\JavaWeb\demo01\src\listener\OnlineUserCounter.java:1-49`
