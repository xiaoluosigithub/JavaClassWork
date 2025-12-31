<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script type="text/javascript">
    $(function() {
        // 检查 localStorage 是否有保存的密码
        if (localStorage.getItem('remember_username')) {
            $('[name="username"]').val(localStorage.getItem('remember_username'));
            $('[name="password"]').val(localStorage.getItem('remember_password'));
            $('#rememberMe').prop('checked', true);
        }

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
                        // 处理记住密码
                        if ($('#rememberMe').is(':checked')) {
                            localStorage.setItem('remember_username', u);
                            localStorage.setItem('remember_password', p);
                        } else {
                            localStorage.removeItem('remember_username');
                            localStorage.removeItem('remember_password');
                        }
                        
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
<form action="login" method="post" class="styled-form">
    <h2>🔐 登录</h2>
    <input type="text" name="username" placeholder="用户名" required>
    <input type="password" name="password" placeholder="密码" required>
    <div class="checkbox-container">
        <input type="checkbox" id="rememberMe" name="rememberMe">
        <label for="rememberMe">记住密码</label>
    </div>
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
