<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/10/15
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>添加地址</title>
    <style>
        body { font-family: Arial; background: #f5f6fa; }
        form { width: 400px; margin: 80px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        input, button { width: 100%; padding: 10px; margin-top: 10px; }
        button { background: #0078d7; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background: #005fa3; }
    </style>
</head>
<body>
<form action="addAddress" method="post">
    <h2>➕ 添加地址</h2>
    <input type="text" name="contact" placeholder="联系人" required>
    <input type="text" name="addressDesc" placeholder="地址描述" required>
    <input type="text" name="postCode" placeholder="邮政编码">
    <input type="text" name="tel" placeholder="电话">
    <input type="text" name="createdBy" placeholder="创建者ID" required>
    <input type="text" name="userId" placeholder="用户ID" required>
    <button type="submit">提交</button>
    <a href="index.jsp" style="display:block;margin-top:10px;text-align:center;">返回首页</a>
</form>
</body>
</html>
