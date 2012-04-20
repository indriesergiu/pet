<%--
  TODO doc me
  User: sergiu.indrie
  Date: 4/2/12
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="viewBean" class="com.main.htmlclient.beans.ViewBean" scope="session"/>
<jsp:setProperty name="viewBean" property="*"/>
<jsp:setProperty name="viewBean" property="cookieMap" value="${cookie}"/>
<html>
<head><title>View - XML Services JSP Client</title></head>
<body>

<%--Include the app header--%>
<%@ include file="app_header.jsp" %>

<%--Call view service--%>
<% viewBean.view(); %>

<%--The toolbar--%>
<a href="search_form.jsp">Search</a>
<%--Option to update page content--%>
<a href=<%= "update.jsp?page=" + viewBean.getPage() + "&pageContent=" + viewBean.getUrlEncodedPageContent() %>>Update page</a>

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
                <%= viewBean.getPageContent() %>
            </textarea>
            <br>
        </td>
    </tr>

    <tr>
        <td align="center">
            <%--Page navigation links--%>
            <div>
                <%--Previous page link--%>
                <a href=<%= "view.jsp?page=" + viewBean.getPreviousPage() %>>${viewBean.previousPage}</a>

                <%--Placeholder between page links--%>
                &nbsp;&nbsp;&nbsp;&nbsp;

                <%--Next page link--%>
                <a href=<%= "view.jsp?page=" + viewBean.getNextPage() %>>${viewBean.nextPage}</a>
            </div>
        </td>
    </tr>
</table>

<%--TODO add headers from response data--%>

</body>
</html>