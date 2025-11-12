<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/10/15
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dao.Address" %>
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
<form action="queryAddress" method="get">
    <input type="text" name="keyword" placeholder="输入联系人或地址关键字">
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
        <th>电话</th><th>用户ID</th><th>创建时间</th>
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
    </tr>
    <% } %>
</table>
<% } else if (request.getParameter("keyword") != null) { %>
<p style="text-align:center;color:red;">未找到匹配的记录。</p>
<% } %>
</body>
</html>
