<%--
  Created by IntelliJ IDEA.
  User: lyx
  Date: 2025/10/15
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*,java.util.Map" %>
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
    ArrayList<HashMap<String, String>> addressList =
            (ArrayList<HashMap<String, String>>) request.getAttribute("addressList");
    if (addressList != null && !addressList.isEmpty()) {
%>
<table>
    <tr>
        <th>ID</th><th>联系人</th><th>地址描述</th><th>邮编</th>
        <th>电话</th><th>用户ID</th><th>创建时间</th>
    </tr>
    <% for (Map<String,String> row : addressList) { %>
    <tr>
        <td><%=row.get("id")%></td>
        <td><%=row.get("contact")%></td>
        <td><%=row.get("addressDesc")%></td>
        <td><%=row.get("postCode")%></td>
        <td><%=row.get("tel")%></td>
        <td><%=row.get("userId")%></td>
        <td><%=row.get("creationDate")%></td>
    </tr>
    <% } %>
</table>
<% } else if (request.getParameter("keyword") != null) { %>
<p style="text-align:center;color:red;">未找到匹配的记录。</p>
<% } %>
</body>
</html>

