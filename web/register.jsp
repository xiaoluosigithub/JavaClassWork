<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>注册</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script type="text/javascript">
    $(function() {
        // 提交注册表单
        $('form').submit(function(e) {
            e.preventDefault();
            var u = $.trim($(this).find('[name="username"]').val());
            var p = $.trim($(this).find('[name="password"]').val());
            if (!u || !p) {
                alert('请输入用户名和密码');
                return;
            }
            $.ajax({
                url: 'register',
                type: 'POST',
                data: { username: u, password: p },
                dataType: 'json',
                headers: { 'X-Requested-With': 'XMLHttpRequest' },
                success: function(data) {
                    if (data.success && data.redirect) {
                        alert(data.message || '注册成功');
                        window.location.href = data.redirect;
                    } else {
                        alert(data.message || '注册失败');
                    }
                },
                error: function() {
                    alert('请求失败');
                }
            });
        });
    });
    </script>
</head>
<body>
<form action="register" method="post" class="styled-form">
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
