<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>登录</title>
    <style>
        body { font-family: Arial; background: #f5f6fa; }
        form { width: 380px; margin: 80px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        input, button { width: 100%; padding: 10px; margin-top: 10px; }
        button { background: #0078d7; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background: #005fa3; }
        .error { color: red; text-align: center; }
        .links { text-align: center; margin-top: 10px; }
        .links a { color: #0078d7; text-decoration: none; }
    </style>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script type="text/javascript">
    $(function() {
        $('form').submit(function(e) {
            e.preventDefault(); // 阻止表单提交
            // 获取用户名和密码
            var u = $.trim($(this).find('[name="username"]').val());
            var p = $.trim($(this).find('[name="password"]').val());
            if (!u || !p) {
                alert('请输入用户名和密码');
                return;
            }
            $.ajax({
                url: 'login',
                type: 'POST',
                data: {
                    username: u,
                    password: p
                },
                dataType: 'json',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                },
                success: function(data) {
                    if (data.success && data.redirect) {
                        alert(data.message || '登录成功');
                        window.location.href = data.redirect;
                    } else {
                        alert(data.message || '登录失败');
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
<form action="login" method="post">
    <h2>🔐 登录</h2>
    <input type="text" name="username" placeholder="用户名" required>
    <input type="password" name="password" placeholder="密码" required>
    <button type="submit">登录</button>
    <div class="links">
        <a href="register.jsp">没有账号？去注册</a>
        <a href="index.jsp" style="margin-left:12px;">返回首页</a>
    </div>
    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
</form>
</body>
</html>
