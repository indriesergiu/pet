<%--
  Created by IntelliJ IDEA.
  User: sergiu.indrie
  Date: 4/5/12
  Time: 5:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="searchBean" class="com.main.htmlclient.beans.SearchBean" scope="session"/>
<jsp:setProperty name="viewBean" property="cookieMap" value="${cookie}"/>
<jsp:setProperty name="viewBean" property="cookieMap" value="${param}"/>
<html>
<head><title>Search Results - XML Services JSP Client</title></head>
<body>

<%--Include the app header--%>
<%@ include file="app_header.jsp" %>

<%--The toolbar--%>
<a href="search_form.jsp">Search</a>
<a href="view.jsp">View</a>

<%--Call view service--%>
<% searchBean.search(); %>

<table>

    <tr>
        <td align="right">
            <%--Page number displayed--%>
            <label>Page ${viewBean.page}</label>
            <br>
        </td>
    </tr>

    <tr>
        <td>
            <%--Page content displayed in text area--%>
            <textarea rows="27" cols="100" readonly="readonly">
                <%= searchBean.getPageContent() %>
            </textarea>
            <br>
        </td>
    </tr>

    <tr>
        <td align="center">
            <%--Page navigation links--%>
            <div>
                <%--Previous page link--%>
                <a href=<%= "view.jsp?page=" + searchBean.getPreviousPage() %>>${viewBean.previousPage}</a>

                <%--Placeholder between page links--%>
                &nbsp;&nbsp;&nbsp;&nbsp;

                <%--Next page link--%>
                <a href=<%= "view.jsp?page=" + searchBean.getNextPage() %>>${viewBean.nextPage}</a>
            </div>
        </td>
    </tr>
</table>

</body>
</html>