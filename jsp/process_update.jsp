<%--
  TODO doc me
  User: sergiu.indrie
  Date: 4/2/12
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page errorPage="error.jsp" %>--%>
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

<% updateBean.update(); %>

<%--Display the call outcome--%>
<c:choose>
    <c:when test="${updateBean.success}">
        <%--UPDATE SUCCESSFUL--%>

        <%--forward to the view page--%>
        <jsp:forward page="view.jsp"/>
    </c:when>
    <c:otherwise>
        <%--UPDATE FAILED--%>
        <p>Update failed. Server sent error code ${updateBean.responseStatusCode} and message: ${updateBean.responseMessage}</p>
    </c:otherwise>
</c:choose>
</body>
</html>