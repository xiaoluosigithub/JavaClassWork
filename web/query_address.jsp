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
    <style>
        body { font-family: Arial; background: #f5f6fa; }
        form { text-align: center; margin-top: 40px; }
        input { padding: 10px; width: 250px; }
        button { padding: 10px 20px; background: #0078d7; color: white; border: none; border-radius: 5px; cursor: pointer; }
        table { width: 90%; margin: 30px auto; border-collapse: collapse; background: white; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
        th { background: #0078d7; color: white; }
    </style>
</head>
<body>
<%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %>


<form action="queryAddress" method="get">
    <input type="number" name="id" value="${param.id}" placeholder="输入ID">
    <input type="text" name="contact" value="${param.contact}" placeholder="输入联系人（支持模糊）">
    <input type="hidden" name="page" value="1"/>
    <input type="hidden" name="pageSize" value="20"/>
    <button type="submit">查询</button>
    <a href="index.jsp" style="margin-left:20px;">返回首页</a>
</form>

<table>
    <tr>
        <th>ID</th><th>联系人</th><th>地址描述</th><th>邮编</th>
        <th>电话</th><th>用户ID</th><th>创建时间</th><th>操作</th>
    </tr>
    <tbody id="rows"></tbody>
</table>
<div id="empty" style="text-align:center;color:red;display:none;">未找到匹配的记录。</div>

<div style="width:90%; margin:10px auto; text-align:center;">

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
<script>
document.addEventListener('DOMContentLoaded', function() {
  const form = document.querySelector('form[action="queryAddress"]');
  const rows = document.getElementById('rows');
  const empty = document.getElementById('empty');
  const info = document.getElementById('info');
  let page = 1;
  let pageSize = 20;
  let total = 0;
  let maxPage = 1;
  function render(list) {
    rows.innerHTML = '';
    if (!list || list.length === 0) {
      empty.style.display = 'block';
      return;
    }
    empty.style.display = 'none';
    const html = list.map(a => {
      const id = a.id;
      const link = 'updateAddress?id=' + id;
      return '<tr>' +
        '<td>' + (a.id||'') + '</td>' +
        '<td>' + (a.contact||'') + '</td>' +
        '<td>' + (a.addressDesc||'') + '</td>' +
        '<td>' + (a.postCode||'') + '</td>' +
        '<td>' + (a.tel||'') + '</td>' +
        '<td>' + (a.userId||'') + '</td>' +
        '<td>' + (a.creationDate||'') + '</td>' +
        '<td>' +
          '<a href="'+link+'" style="padding:6px 10px;background:#28a745;color:#fff;border-radius:4px;text-decoration:none;margin-right:6px;">改</a>' +
          '<button data-id="'+id+'" class="del" style="padding:6px 10px;background:#dc3545;color:#fff;border:none;border-radius:4px;cursor:pointer;">删</button>' +
        '</td>' +
      '</tr>';
    }).join('');
    rows.innerHTML = html;
    rows.querySelectorAll('button.del').forEach(btn => {
      btn.addEventListener('click', async function() {
        const id = this.getAttribute('data-id');
        if (!confirm('确认删除该地址吗？')) return;
        const body = new URLSearchParams({ id });
        try {
          const res = await fetch('deleteAddress', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
              'X-Requested-With': 'XMLHttpRequest'
            },
            body: body.toString()
          });
          const data = await res.json();
          if (!data.success && data.redirect) {
            alert(data.message || '');
            location.href = data.redirect;
            return;
          }
          alert(data.message||'');
          load();
        } catch (e) {
          alert('请求失败');
        }
      });
    });
  }
  async function load(p=page, ps=pageSize) {
    const idInput = form.querySelector('input[name="id"]');
    const contactInput = form.querySelector('input[name="contact"]');
    const idVal = idInput ? idInput.value.trim() : '';
    const contactVal = contactInput ? contactInput.value.trim() : '';
    const params = new URLSearchParams({
      id: idVal,
      contact: contactVal,
      page: p,
      pageSize: ps
    });
    try {
      const res = await fetch('queryAddress?' + params.toString(), {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
      });
      const data = await res.json();
      if (!data.success && data.redirect) {
        alert(data.message || '');
        location.href = data.redirect;
        return;
      }
      page = data.page || p;
      pageSize = data.pageSize || ps;
      total = data.total || 0;
      maxPage = data.maxPage || 1;
      render(data.list || []);
      info.textContent = '共 ' + total + ' 条  共 ' + maxPage + ' 页  当前第 ' + page + ' 页  每页 ' + pageSize + ' 条';
    } catch (e) {
      alert('加载失败');
    }
  }
  form.addEventListener('submit', function(e) {
    e.preventDefault();
    page = 1;
    load(1, pageSize);
  });
  document.getElementById('first').addEventListener('click', function(e){ e.preventDefault(); page=1; load(1, pageSize); });
  document.getElementById('prev').addEventListener('click', function(e){ e.preventDefault(); page = Math.max(1, page-1); load(page, pageSize); });
  document.getElementById('next').addEventListener('click', function(e){ e.preventDefault(); page = Math.min(maxPage, page+1); load(page, pageSize); });
  document.getElementById('last').addEventListener('click', function(e){ e.preventDefault(); page = maxPage; load(page, pageSize); });
  const goForm = document.querySelectorAll('form[action="queryAddress"]')[1];
  const sizeForm = document.querySelectorAll('form[action="queryAddress"]')[2];
  goForm.addEventListener('submit', function(e){ e.preventDefault(); const p = parseInt(goForm.page.value||'1',10); page = Math.max(1, Math.min(maxPage, p)); load(page, pageSize); });
  sizeForm.addEventListener('submit', function(e){ e.preventDefault(); const ps = parseInt(sizeForm.pageSize.value||'20',10); pageSize = Math.max(1, ps); page = 1; load(page, pageSize); });
  load();
});
</script>
</body>
</html>
