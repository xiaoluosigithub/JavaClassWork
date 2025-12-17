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
    <script>
        // 等待DOM完全加载完成后执行
        document.addEventListener('DOMContentLoaded', function() {
            // 获取form表单
            const form = document.querySelector('form');
            // 显示提示信息的辅助函数
            // msg: 要显示的提示消息，如果为空则显示空alert
            function show(msg) {
                alert(msg || '');
            }
            // 为表单添加提交事件监听器
            form.addEventListener('submit', async function(e) {
                // 阻止表单的默认提交行为（避免页面刷新）
                e.preventDefault();

                // 获取并清理输入的用户名和密码
                // trim() 方法去除字符串两端的空白字符
                const u = form.username.value.trim();
                const p = form.password.value.trim();

                // 验证输入：检查用户名或密码是否为空
                if (!u || !p) {
                    show('请输入用户名和密码');
                    return; // 终止函数执行
                }

                // 创建URL编码格式的表单数据
                // URLSearchParams 用于处理 application/x-www-form-urlencoded 格式的数据
                const body = new URLSearchParams({
                    username: u,
                    password: p
                });

                try {
                    // 发送异步POST请求到服务器的login端点
                    const res = await fetch('login', {
                        method: 'POST',           // 请求方法为POST
                        headers: {
                            // 设置内容类型为表单编码格式
                            'Content-Type': 'application/x-www-form-urlencoded',
                            // 标识这是一个AJAX请求（传统Web开发中常用）
                            'X-Requested-With': 'XMLHttpRequest'
                        },
                        // 将表单数据转换为字符串作为请求体
                        body: body.toString()
                    });

                    // 解析服务器响应的JSON数据
                    const data = await res.json();

                    // 检查响应数据是否表示登录成功且有重定向地址
                    if (data.success && data.redirect) {
                        // 显示成功消息（如果服务器提供了message字段则使用，否则使用默认消息）
                        alert(data.message || '登录成功');
                        // 跳转到指定的重定向URL
                        window.location.href = data.redirect;
                    } else {
                        // 登录失败，显示服务器返回的错误消息或默认消息
                        alert(data.message || '登录失败');
                    }

                } catch (err) {
                    // 捕获网络错误或其他异常，显示通用错误信息
                    show('请求失败');
                }
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
