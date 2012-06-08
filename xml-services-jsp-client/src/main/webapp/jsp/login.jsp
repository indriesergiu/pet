<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Login - XML Services JSP Client</title>
</head>
<body>

<%--Include the app header--%>
<%@ include file="app_header.jsp" %>

<form method="post" action="process_login.jsp">

    <table title="Login">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username"/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login"></td>
        </tr>
    </table>

</form>
</body>
</html>