<%--
  TODO doc me
  User: sergiu.indrie
  Date: 4/2/12
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="updateBean" class="com.main.htmlclient.beans.UpdateBean" scope="session"/>
<jsp:setProperty name="updateBean" property="*"/>
<jsp:setProperty name="updateBean" property="cookieMap" value="${cookie}"/>
<html>
<head><title>Update - XML Services JSP Client</title></head>
<body>

<%--Include the app header--%>
<%@ include file="app_header.jsp" %>

<%--The toolbar--%>
<a href="view.jsp">View</a>
<a href="search_form.jsp">Search</a>

<form action="process_update.jsp" method="post">

    <input type="hidden" name="page" value="${updateBean.page}">
    <table>

        <tr>
            <td align="right">
                <%--Page number displayed--%>
                <label>Page ${updateBean.page}</label>
                <br>
            </td>
        </tr>

        <tr>
            <td>
                <%--Page content displayed in text area--%>
                <textarea name="pageContent" rows="27" cols="100">
                    <%= updateBean.getPageContent() %>
                </textarea>
                <br>
            </td>
        </tr>

        <tr>
            <td align="right"><input type="submit" value="Update"></td>
        </tr>

    </table>

</form>

</body>
</html>