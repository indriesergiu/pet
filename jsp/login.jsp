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
<jsp:useBean id="loginData" class="com.main.htmlclient.form.LoginData" scope="session"/>
<jsp:setProperty name="loginData" property="*"/>
<html>
<head><title>Login</title></head>
<body>

<%--TODO add a banner--%>

<%--Call the login service--%>
<% loginData.login(); %>

<%--Display the call outcome--%>
<c:choose>
    <c:when test="${loginData.success}">
        <%--LOGIN SUCCESSFUL--%>

        <%--add the received cookies to the response--%>
        <%
            for (Cookie cookie : loginData.getCookies()) {
                response.addCookie(cookie);
            }
        %>
        <%--forward to the view page--%>
        <jsp:forward page="view.jsp"/>

        <%--Cannot use for each and response.addCookie--%>

        <%--<c:forEach var="cookie" items="${loginData.cookies}">--%>
        <%--<% response.addCookie(); %>--%>
        <%--</c:forEach>--%>
    </c:when>
    <c:otherwise>
        <%--LOGIN FAILED--%>
        <p>Authentication failed. Server sent error code ${loginData.responseStatusCode} and message: ${loginData.responseMessage}</p>
    </c:otherwise>
</c:choose>
</body>
</html>