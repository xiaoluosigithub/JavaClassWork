<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>编辑地址</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form action="updateAddress" method="post" class="styled-form">
    <h2>✏️ 编辑地址</h2>
    <input type="hidden" name="id" value="${address.id}">
    <input type="text" name="contact" placeholder="联系人" value="${address.contact}" required>
    <input type="text" name="addressDesc" placeholder="地址描述" value="${address.addressDesc}" required>
    <input type="text" name="postCode" placeholder="邮政编码" value="${address.postCode}">
    <input type="text" name="tel" placeholder="电话" value="${address.tel}">
    <input type="text" name="userId" placeholder="用户ID" value="${address.userId}" required>
    <input type="text" name="modifyBy" placeholder="修改者ID" value="${address.modifyBy}">
    <input type="hidden" name="contactFilter" value="${contactFilter}">
    <input type="hidden" name="idParam" value="${id}">
    <input type="hidden" name="page" value="${page}">
    <input type="hidden" name="pageSize" value="${pageSize}">
    <button type="submit">提交</button>
    <div class="links">
        <a href="query_address.jsp?id=${id}&contact=${contactFilter}&page=${page}&pageSize=${pageSize}">返回列表</a>
    </div>
</form>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function() {
  // 提交编辑地址表单
  $('form').submit(function(e) {
    e.preventDefault();
    var $f = $(this); // 获取表单元素
    var id = $f.find('[name="id"]').val(); // 获取地址ID
    var contact = $.trim($f.find('[name="contact"]').val()); // 获取联系人
    var addressDesc = $.trim($f.find('[name="addressDesc"]').val()); // 获取地址描述
    var postCode = $.trim($f.find('[name="postCode"]').val()); // 获取邮政编码
    var tel = $.trim($f.find('[name="tel"]').val()); // 获取电话
    var userId = $.trim($f.find('[name="userId"]').val()); // 获取用户ID
    var modifyBy = $.trim($f.find('[name="modifyBy"]').val()); // 获取修改者ID
    $.ajax({
      url: 'updateAddress',
      type: 'POST',
      dataType: 'json',
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      data: {
        id: id,
        contact: contact,
        addressDesc: addressDesc,
        postCode: postCode,
        tel: tel,
        userId: userId,
        modifyBy: modifyBy
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
