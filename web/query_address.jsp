<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/10/15
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, pojo.Address" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <title>查询地址</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="meta"><span class="time">当前时间：<%= new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", java.util.Locale.CHINA).format(new java.util.Date()) %></span></div>

<!-- 查询地址表单 -->
<form action="queryAddress" method="get" class="search-form search-bar">
    <input type="number" name="id" value="${param.id}" placeholder="输入ID">
    <input type="text" name="contact" value="${param.contact}" placeholder="输入联系人（支持模糊）">
    <input type="hidden" name="page" value="1"/>
    <input type="hidden" name="pageSize" value="20"/>
    <button type="submit">查询</button>
    <a href="index.jsp" class="btn" style="margin-left:10px;background:#6c757d;">返回首页</a>
</form>

<!-- 查询结果表格 -->
<table>
    <tr>
        <th>ID</th><th>联系人</th><th>地址描述</th><th>邮编</th>
        <th>电话</th><th>用户ID</th><th>创建时间</th><th>操作</th>
    </tr>
    <tbody id="rows"></tbody>
</table>
<div id="empty" style="text-align:center;color:red;display:none;margin-top:20px;">未找到匹配的记录。</div>

<!-- 分页导航 -->
<div class="pagination">

    <a href="#" id="first">首页</a>
    <a href="#" id="prev">上一页</a>
    <a href="#" id="next">下一页</a>
    <a href="#" id="last">尾页</a>
    <div style="margin-top:8px;" id="info"></div>

    <form method="get" action="queryAddress" style="display:inline-block;margin-top:8px;">
        <input type="hidden" name="id" />
        <input type="hidden" name="contact" />
        <input type="number" name="page" value="1" min="1" style="width:80px;"/>
        <input type="hidden" name="pageSize" value="20"/>
        <button type="submit">GO</button>
    </form>

    <form method="get" action="queryAddress" style="display:inline-block;margin-top:8px;">
        <input type="hidden" name="id" />
        <input type="hidden" name="contact" />
        <input type="number" name="pageSize" value="20" min="1" style="width:80px;"/>
        <input type="hidden" name="page" value="1"/>
        <button type="submit">设置每页</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function() {
  var $form = $('form[action="queryAddress"]').first(); // 获取查询地址表单
  var $rows = $('#rows'); // 获取查询结果表格 tbody 元素
  var $empty = $('#empty'); // 获取未找到记录提示元素
  var $info = $('#info'); // 获取分页信息元素
  var page = 1; // 当前页码
  var pageSize = 20; // 每页记录数
  var total = 0; // 总记录数
  var maxPage = 1; // 最大页码  
  // 渲染查询结果表格
  function render(list) {
    if (!list || list.length === 0) {
      $rows.html('');
      $empty.show();
      return;
    }
    $empty.hide();
    // 渲染查询结果表格行
    var html = $.map(list, function(a, i) {
      var id = a.id;
      var link = 'updateAddress?id=' + id;
      // 为偶数行添加不同的背景颜色
      var rowClass = (i % 2 === 0) ? 'row-even' : 'row-odd';
      return '<tr class="' + rowClass + '">' +
        '<td>' + (a.id||'') + '</td>' +
        '<td>' + (a.contact||'') + '</td>' +
        '<td>' + (a.addressDesc||'') + '</td>' +
        '<td>' + (a.postCode||'') + '</td>' +
        '<td>' + (a.tel||'') + '</td>' +
        '<td>' + (a.userId||'') + '</td>' +
        '<td>' + (a.creationDate||'') + '</td>' +
        '<td>' +
          '<a href="'+link+'" class="btn btn-secondary" style="width:auto;padding:6px 12px;margin-right:6px;">改</a>' +
          '<button data-id="'+id+'" class="del btn btn-danger" style="width:auto;padding:6px 12px;">删</button>' +
        '</td>' +
      '</tr>';
    }).join('');
    $rows.html(html);
  }
  // 加载查询结果
  function load(p, ps) {
    // ID值
    var idVal = $.trim($form.find('input[name="id"]').val() || '');
    // 联系人值
    var contactVal = $.trim($form.find('input[name="contact"]').val() || '');
    $.ajax({
      url: 'queryAddress',
      type: 'GET',
      dataType: 'json',
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      data: { id: idVal, contact: contactVal, page: p, pageSize: ps },
      // 成功回调函数
      success: function(data) {
        if (!data.success && data.redirect) {
          alert(data.message || '');
          location.href = data.redirect;
          return;
        }
        page = data.page || p; // 更新当前页码
        pageSize = data.pageSize || ps; // 更新每页记录数
        total = data.total || 0; // 更新总记录数
        maxPage = data.maxPage || 1; // 更新最大页码
        render(data.list || []); // 渲染查询结果表格
        $info.text('共 ' + total + ' 条  共 ' + maxPage + ' 页  当前第 ' + page + ' 页  每页 ' + pageSize + ' 条'); // 更新分页信息
      },
      error: function() {
        alert('加载失败');
      }
    });
  }
  // 提交查询表单
  $form.on('submit', function(e) {
    e.preventDefault();
    page = 1;
    load(1, pageSize);
  });
  // 点击第一页按钮
  $('#first').on('click', function(e){ e.preventDefault(); page=1; load(1, pageSize); });
  // 点击上一页按钮  如果当前页码为1，则不执行跳转
  $('#prev').on('click', function(e){ e.preventDefault(); page = Math.max(1, page-1); load(page, pageSize); });
  // 点击下一页按钮  如果当前页码为最大页码，则不执行跳转 
  $('#next').on('click', function(e){ e.preventDefault(); page = Math.min(maxPage, page+1); load(page, pageSize); });
  // 点击最后一页按钮  如果当前页码为最大页码，则不执行跳转
  $('#last').on('click', function(e){ e.preventDefault(); page = maxPage; load(page, pageSize); });
  var $forms = $('form[action="queryAddress"]'); // 获取所有查询地址表单
  var $goForm = $forms.eq(1); // 获取跳转页码表单
  var $sizeForm = $forms.eq(2); // 获取设置每页记录数表单 
  // 提交跳转页码表单
  $goForm.on('submit', function(e){ e.preventDefault(); var p = parseInt($goForm.find('[name="page"]').val()||'1',10); page = Math.max(1, Math.min(maxPage, p)); load(page, pageSize); });
  // 提交设置每页记录数表单
  $sizeForm.on('submit', function(e){ e.preventDefault(); var ps = parseInt($sizeForm.find('[name="pageSize"]').val()||'20',10); pageSize = Math.max(1, ps); page = 1; load(page, pageSize); });
  // 点击删除按钮
  $rows.on('click', 'button.del', function() {
    var id = $(this).data('id');
    if (!confirm('确认删除该地址吗？')) return;
    // 发送删除地址请求
    $.ajax({
      url: 'deleteAddress',
      type: 'POST',
      dataType: 'json',
      headers: { 'X-Requested-With': 'XMLHttpRequest' },
      data: { id: id },
      success: function(data) {
        if (!data.success && data.redirect) {
          alert(data.message || '');
          location.href = data.redirect;
          return;
        }
        alert(data.message||'');
        load(page, pageSize);
      },
      error: function() {
        alert('请求失败');
      }
    });
  });
  load(page, pageSize);
});
</script>
</body>
</html>