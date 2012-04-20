<%--
  Created by IntelliJ IDEA.
  User: sergiu.indrie
  Date: 4/3/12
  Time: 1:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<html>
<head><title>Server Error</title></head>
<body>
<p>Error status code: ${pageContext.errorData.statusCode} </p>

<p>Exception: ${pageContext.errorData.throwable}</p>

<p>Cause: ${pageContext.errorData.throwable.cause}</p>

<%-- TODO Consider using resource bundles for messages with <fmt:message key="ServerError"/>--%>

<%--<jsp:text>--%>
<%--what are these?--%>
<%--<label--%>
<%--</jsp:text>--%>
</body>
</html>