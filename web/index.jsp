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
    <link rel="stylesheet" href="css/style.css">
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

    <div class="home-container">
        <h1>🏠 地址管理系统</h1>
        <div class="action-buttons">
            <a href="add_address.jsp">
                <span>➕</span>
                <span>添加地址</span>
            </a>
            <a href="query_address.jsp">
                <span>🔍</span>
                <span>查询地址</span>
            </a>
        </div>
    </div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function() {
  // 点击退出链接时发送退出请求
  var $logoutLink = $('.auth-links a[href="logout"]');
  // 绑定点击事件
  if ($logoutLink.length) {
    $logoutLink.on('click', function(e) {
      e.preventDefault();
      $.ajax({
        url: 'logout',
        type: 'POST',
        dataType: 'json',
        headers: { 'X-Requested-With': 'XMLHttpRequest' },
        success: function(data) {
          alert(data.message || '');
          if (data.redirect) location.href = data.redirect;
        },
        error: function() {
          alert('请求失败');
        }
      });
    });
  }
});
</script>
</body>
</html>
