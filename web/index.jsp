<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/9/24
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="listener.OnlineUserCounter" %>
<html>
<head>
    <title>地址管理系统</title>
    <style>
        body { font-family: Arial; background: #f7f8fa; text-align: center; padding: 60px; }
        a { text-decoration: none; color: white; background: #0078d7; padding: 12px 24px; border-radius: 6px; margin: 10px; display: inline-block; }
        a:hover { background: #005fa3; }
        .auth-links { position: absolute; top: 20px; right: 20px; }
        .auth-links a { padding: 8px 16px; margin: 0 6px; font-size: 14px; }
        .auth-links span { color: #333; font-size: 14px; }
    </style>
</head>
<body>
    <div class="auth-links">
        <% String currentUser = (String) session.getAttribute("currentUser"); %>
        <% if (currentUser == null) { %>
            <a href="login.jsp">登录</a>
            <a href="register.jsp">注册</a>
        <% } else { %>
            <span>已登录：<%= currentUser %></span>
            <a href="logout">退出</a>
        <% } %>
        <span>在线用户：<%= OnlineUserCounter.getCount() %></span>
    </div>
<h1>🏠 地址管理系统</h1>
<a href="add_address.jsp">➕ 添加地址</a>
<a href="query_address.jsp">🔍 查询地址</a>
<script>
document.addEventListener('DOMContentLoaded', function() {
  const logoutLink = document.querySelector('.auth-links a[href="logout"]');
  if (logoutLink) {
    logoutLink.addEventListener('click', async function(e) {
      e.preventDefault();
      try {
        const res = await fetch('logout', {
          method: 'POST',
          headers: { 'X-Requested-With': 'XMLHttpRequest' }
        });
        const data = await res.json();
        alert(data.message || '');
        if (data.redirect) location.href = data.redirect;
      } catch (err) {
        alert('请求失败');
      }
    });
  }
});
</script>
</body>
</html>
