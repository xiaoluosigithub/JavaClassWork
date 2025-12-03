<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/10/15
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.Address" %>
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
    <input type="text" name="keyword" value="${param.keyword}" placeholder="输入联系人或地址关键字">
    <input type="hidden" name="page" value="1"/>
    <input type="hidden" name="pageSize" value="${pageSize==null?5:pageSize}"/>
    <button type="submit">查询</button>
    <a href="index.jsp" style="margin-left:20px;">返回首页</a>
</form>

<%
    List<Address> addressList = (List<Address>) request.getAttribute("addressList");
    if (addressList != null && !addressList.isEmpty()) {
%>
<table>
    <tr>
        <th>ID</th><th>联系人</th><th>地址描述</th><th>邮编</th>
        <th>电话</th><th>用户ID</th><th>创建时间</th><th>操作</th>
    </tr>
    <% for (Address addr : addressList) { %>
    <tr>
        <td><%=addr.getId()%></td>
        <td><%=addr.getContact()%></td>
        <td><%=addr.getAddressDesc()%></td>
        <td><%=addr.getPostCode()%></td>
        <td><%=addr.getTel()%></td>
        <td><%=addr.getUserId()%></td>
        <td><%=addr.getCreationDate()%></td>
        <td>
            <a href="updateAddress?id=<%=addr.getId()%>&keyword=${param.keyword}&page=${page}&pageSize=${pageSize}" style="padding:6px 10px;background:#28a745;color:#fff;border-radius:4px;text-decoration:none;margin-right:6px;">改</a>
            <form action="deleteAddress" method="post" style="display:inline;" onsubmit="return confirm('确认删除该地址吗？');">
                <input type="hidden" name="id" value="<%=addr.getId()%>"/>
                <input type="hidden" name="keyword" value="${param.keyword}"/>
                <input type="hidden" name="page" value="${page}"/>
                <input type="hidden" name="pageSize" value="${pageSize}"/>
                <button type="submit" style="padding:6px 10px;background:#dc3545;color:#fff;border:none;border-radius:4px;cursor:pointer;">删</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<% } else if (request.getParameter("keyword") != null) { %>
<p style="text-align:center;color:red;">未找到匹配的记录。</p>
<% } %>

<div style="width:90%; margin:10px auto; text-align:center;">

    <a href="queryAddress?keyword=${param.keyword}&page=1&pageSize=${pageSize}">首页</a>
    <a href="queryAddress?keyword=${param.keyword}&page=${page-1}&pageSize=${pageSize}">上一页</a>
    <a href="queryAddress?keyword=${param.keyword}&page=${page+1}&pageSize=${pageSize}">下一页</a>
    <a href="queryAddress?keyword=${param.keyword}&page=${maxPage}&pageSize=${pageSize}">尾页</a>
    <div style="margin-top:8px;">
        共 ${total} 条 &nbsp; 共 ${maxPage} 页 &nbsp; 当前第 ${page} 页 &nbsp; 每页 ${pageSize} 条
    </div>

    <form method="get" action="queryAddress" style="display:inline-block;margin-top:8px;">
        <input type="hidden" name="keyword" value="${param.keyword}"/>
        <input type="number" name="page" value="${page}" min="1" style="width:80px;"/>
        <input type="hidden" name="pageSize" value="${pageSize}"/>
        <button type="submit">GO</button>
    </form>

    <form method="get" action="queryAddress" style="display:inline-block;margin-top:8px;">
        <input type="hidden" name="keyword" value="${param.keyword}"/>
        <input type="number" name="pageSize" value="${pageSize}" min="1" style="width:80px;"/>
        <input type="hidden" name="page" value="${page}"/>
        <button type="submit">设置每页</button>
    </form>
</div>
</body>
</html>
