<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>编辑地址</title>
    <style>
        body { font-family: Arial; background: #f5f6fa; }
        form { width: 400px; margin: 80px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        input, button { width: 100%; padding: 10px; margin-top: 10px; }
        button { background: #0078d7; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background: #005fa3; }
    </style>
</head>
<body>
<form action="updateAddress" method="post">
    <h2>✏️ 编辑地址</h2>
    <input type="hidden" name="id" value="${address.id}">
    <input type="text" name="contact" placeholder="联系人" value="${address.contact}" required>
    <input type="text" name="addressDesc" placeholder="地址描述" value="${address.addressDesc}" required>
    <input type="text" name="postCode" placeholder="邮政编码" value="${address.postCode}">
    <input type="text" name="tel" placeholder="电话" value="${address.tel}">
    <input type="text" name="userId" placeholder="用户ID" value="${address.userId}" required>
    <input type="text" name="modifyBy" placeholder="修改者ID" value="${address.modifyBy}">
    <input type="hidden" name="keyword" value="${keyword}">
    <input type="hidden" name="page" value="${page}">
    <input type="hidden" name="pageSize" value="${pageSize}">
    <button type="submit">提交</button>
    <a href="queryAddress?keyword=${keyword}&page=${page}&pageSize=${pageSize}" style="display:block;margin-top:10px;text-align:center;">返回列表</a>
</form>
</body>
</html>
