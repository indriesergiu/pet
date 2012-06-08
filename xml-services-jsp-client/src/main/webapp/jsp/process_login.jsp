<%--
  Calls the login service on the XML Services server and displays the result.

  User: sergiu.indrie
  Date: 4/2/12
  Time: 3:18 PM
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="error.jsp" %>
<jsp:useBean id="loginBean" class="com.xmlservices.jspclient.htmlclient.beans.LoginBean" scope="session"/>
<jsp:setProperty name="loginBean" property="*"/>
<html>
<head><title>Login</title></head>
<body>

<%--Call the login service--%>
<% loginBean.login(); %>

<%--Display the call outcome--%>
<c:choose>
    <c:when test="${loginBean.success}">
        <%--LOGIN SUCCESSFUL--%>

        <%--add the received cookies to the response--%>
        <%
            for (Cookie cookie : loginBean.getCookies()) {
                response.addCookie(cookie);
            }
        %>
        <%--redirect (in order to obtain the cookies in the request) to the view page--%>
        <c:redirect url="view.jsp"/>

    </c:when>
    <c:otherwise>
        <%--LOGIN FAILED--%>
        <p>Authentication failed. Server sent error code ${loginBean.responseStatusCode} and message: ${loginBean.responseMessage}</p>
    </c:otherwise>
</c:choose>
</body>
</html>