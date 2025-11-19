**项目结构**

- 表现层（JSP）
    - `web/index.jsp` 入口，含登录/注册按钮和功能入口
    - `web/login.jsp` 登录表单与错误展示 `e:\JavaWeb\demo01\web\login.jsp:35-37`
    - `web/register.jsp` 注册表单与错误展示
    - `web/add_address.jsp` 地址新增表单
    - `web/query_address.jsp` 查询结果表格
- 控制层（Servlet）
    - 登录：`servlet.LoginServlet` 处理登录 `e:\JavaWeb\demo01\src\servlet\LoginServlet.java:18-58`
    - 注册：`servlet.RegisterServlet` 处理注册
    - 新增地址：`servlet.AddAddressServlet` 处理表单提交 `e:\JavaWeb\demo01\src\servlet\AddAddressServlet.java:25-54`
    - 查询地址：`servlet.QueryAddressServlet` 处理查询与转发 `e:\JavaWeb\demo01\src\servlet\QueryAddressServlet.java:18-41`
- 过滤器
    - `filter.AuthFilter` 登录拦截与编码设置 `e:\JavaWeb\demo01\src\servlet\filter\AuthFilter.java:1-47`
        - UTF-8 编码设置：`request.setCharacterEncoding("utf-8")` 与 `response.setCharacterEncoding("utf-8")`，对应你当前打开的第 22 行
        - 放行列表：登录/注册页与静态资源；其他路径要求已登录
- 业务层（Service）
    - `service.AddressService` 与实现 `service.impl.AddressServiceImpl` `e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:18-66`
- 数据访问层（DAO）
    - 通用模板：`dao.BaseDao` 提供 `executeUpdate` 与 `executeQuery`
    - 连接工具：`dao.DBUtil` 读取 `db.properties` 并创建连接 `e:\JavaWeb\demo01\src\dao\DBUtil.java:12-56`
    - 地址 DAO：接口 `dao.AddressDao` 与实现 `dao.impl.AddressDaoImpl` `e:\JavaWeb\demo01\src\dao\impl\AddressDaoImpl.java:12-45`
    - 实体：`dao.Address` 实体类 `e:\JavaWeb\demo01\src\dao\Address.java:1-97`

**核心流程**

- 登录
    - 表单提交到 `POST /login` → 查 `smbms_user` 比对明文密码 → 成功后在 Session 里放 `currentUser` → 跳转首页
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\LoginServlet.java:32-47`
- 注册
    - 表单提交到 `POST /register` → 先查重，再插入用户，登录并跳转
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\RegisterServlet.java:18-71`
- 鉴权
    - 过滤器放行登录/注册与静态资源；其它路径需要 Session 里有 `currentUser`
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\filter\AuthFilter.java:22-38`
- 地址新增
    - 表单提交到 `POST /addAddress` → Service 校验 → DAO 插入
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\AddAddressServlet.java:25-54`、`e:\JavaWeb\demo01\src\dao\impl\AddressDaoImpl.java:12-23`
- 地址查询
    - `GET /queryAddress?keyword=...` → Service 先取全量，再内存过滤 → 结果放入 `request` → 转发到 JSP
    - 代码参考：`e:\JavaWeb\demo01\src\servlet\QueryAddressServlet.java:22-33`、`e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:39-65`

**关键代码理解**

- 编码与拦截
    - 编码设置位置：`AuthFilter.doFilter` 中 `request.setCharacterEncoding("utf-8")` 与 `response.setCharacterEncoding("utf-8")` `e:\JavaWeb\demo01\src\servlet\filter\AuthFilter.java:22-24`
    - 白名单策略：是否放行由 URI 结尾匹配决定 `e:\JavaWeb\demo01\src\servlet\filter\AuthFilter.java:26-33`
- JDBC 封装
    - `BaseDao` 用可变参数绑定占位符，`executeQuery` 使用回调解析结果集 `e:\JavaWeb\demo01\src\dao\BaseDao.java:22-45`
    - 连接初始化读取 `db.properties` 并 `Class.forName` 注册驱动 `e:\JavaWeb\demo01\src\dao\DBUtil.java:20-35`
- 业务分层
    - Service 负责参数校验与组合 DAO，体现“控制层不直接碰数据库”的分层思想 `e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:19-30`

**值得注意的小问题与练习**

- 字段不落库的差异
    - `add_address.jsp` 收集了 `userId`，`Address` 实体也有 `userId` 字段 `e:\JavaWeb\demo01\web\add_address.jsp:22-28`、`e:\JavaWeb\demo01\src\dao\Address.java:15`
    - 但 `AddressDaoImpl.addAddress` 的 `INSERT` 没有 `userId`，查询也没从库里读 `userId`，导致页面展示为 `null` `e:\JavaWeb\demo01\src\dao\impl\AddressDaoImpl.java:14-22,31-41`
    - 练习：把 `userId` 加入插入与查询映射，并在 JSP 展示真实值。
- 安全性改进
    - 登录使用明文密码比对，建议改为哈希存储（如 `BCrypt`），并在注册时写入哈希。
- 鉴权细化
    - 白名单目前不包含 `add_address.jsp` 与 `query_address.jsp`，因此这两个页面在未登录时会被重定向到登录页；你可以根据需求调整。
- 查询性能
    - `searchAddresses` 先查全表再在内存过滤，数据量大时会慢；练习改为在 DAO 层用 `WHERE contact LIKE ? OR addressDesc LIKE ?` 直接数据库过滤。

**学习建议与路径**

- 路径一：跟着“登录”
    - 打点：在 `LoginServlet` 的成功路径与失败路径打印日志，观察 Session 中 `currentUser` 的设置与跳转 `e:\JavaWeb\demo01\src\servlet\LoginServlet.java:41-50`
    - 改动：增加“退出登录”Servlet，清理 Session 后回首页。
- 路径二：跟着“地址新增/查询”
    - 打点：在 `AddressServiceImpl` 校验失败返回处与 DAO 返回值处打印，确认插入成功与失败的分支 `e:\JavaWeb\demo01\src\service\impl\AddressServiceImpl.java:21-30`
    - 改动：把 `userId` 完整贯通（表单 → 实体 → DAO → 查询→ JSP）。
- 路径三：理解“拦截器”
    - 修改白名单，观察不同页面的访问控制变化 `e:\JavaWeb\demo01\src\servlet\filter\AuthFilter.java:26-33`

**常见问题排查**

- 访问页面 404
    - 检查 `@WebServlet` 的路径与表单 `action` 是否一致，比如 `LoginServlet` 是 `@WebServlet("/login")`，表单 `action="login"` `e:\JavaWeb\demo01\src\servlet\LoginServlet.java:10`、`e:\JavaWeb\demo01\web\login.jsp:26`
- 中文乱码
    - 过滤器已设置 UTF-8 编码；JSP 页头也设置了 `contentType="text/html;charset=UTF-8"`；确保浏览器与数据库连接字符串都启用了 `characterEncoding=UTF-8` `e:\JavaWeb\demo01\src\db.properties:3`
- 数据库连不上
    - 检查 `db.properties`、MySQL 服务是否启动、驱动 JAR 是否可用；也可用 `test_user.java` 做最小化连通性测试 `e:\JavaWeb\demo01\src\test_user.java:1-59`

如果你希望，我可以带着你一步步修复 `userId` 的贯通、加上退出登录、把查询改为数据库过滤，并跑通一次端到端流程。你更想先从哪一部分开始？