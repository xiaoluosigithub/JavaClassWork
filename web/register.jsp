<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>注册</title>
    <style>
        body { font-family: Arial; background: #f5f6fa; }
        form { width: 380px; margin: 80px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        input, button { width: 100%; padding: 10px; margin-top: 10px; }
        button { background: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background: #1e7e34; }
        .links { text-align: center; margin-top: 10px; }
        .links a { color: #0078d7; text-decoration: none; }
        .error { color: red; text-align: center; }
    </style>
    <script>
        function validate() {
            const u = document.forms[0].username.value.trim();
            const p = document.forms[0].password.value.trim();
            if (!u || !p) { alert('请输入用户名和密码'); return false; }
            return true;
        }
    </script>
</head>
<body>
<form action="register" method="post" onsubmit="return validate()">
    <h2>📝 注册</h2>
    <input type="text" name="username" placeholder="用户名" required>
    <input type="password" name="password" placeholder="密码" required>
    <button type="submit">注册</button>
    <div class="links">
        <a href="login.jsp">已有账号？去登录</a>
        <a href="index.jsp" style="margin-left:12px;">返回首页</a>
    </div>
    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
</form>
</body>
</html>