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
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form action="addAddress" method="post" class="styled-form">
    <h2>➕ 添加地址</h2>
    <input type="text" name="contact" placeholder="联系人" required>
    <input type="text" name="addressDesc" placeholder="地址描述" required>
    <input type="text" name="postCode" placeholder="邮政编码">
    <input type="text" name="tel" placeholder="电话">
    <input type="text" name="createdBy" placeholder="创建者ID" required>
    <input type="text" name="userId" placeholder="用户ID" required>
    <button type="submit">提交</button>
    <div class="links">
        <a href="index.jsp">返回首页</a>
    </div>
</form>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function() {
  // 提交添加地址表单
  $('form').submit(function(e) {
    e.preventDefault();
    var $f = $(this); // 获取表单元素
    var contact = $.trim($f.find('[name="contact"]').val()); // 获取联系人
    var addressDesc = $.trim($f.find('[name="addressDesc"]').val()); // 获取地址描述
    var postCode = $.trim($f.find('[name="postCode"]').val()); // 获取邮政编码
    var tel = $.trim($f.find('[name="tel"]').val()); // 获取电话
    var createdBy = $.trim($f.find('[name="createdBy"]').val()); // 获取创建者ID
    var userId = $.trim($f.find('[name="userId"]').val()); // 获取用户ID  
    $.ajax({
      url: 'addAddress',
      type: 'POST',
      dataType: 'json',
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      data: {
        contact: contact,
        addressDesc: addressDesc,
        postCode: postCode,
        tel: tel,
        createdBy: createdBy,
        userId: userId
      },
      success: function(data) {
        alert(data.message || '');
        if (data.redirect) location.href = data.redirect;
      },
      error: function() {
        alert('请求失败');
      }
    });
  });
});
</script>
</body>
</html>
