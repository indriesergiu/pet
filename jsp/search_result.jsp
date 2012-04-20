<%--
  Created by IntelliJ IDEA.
  User: sergiu.indrie
  Date: 4/5/12
  Time: 5:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="searchBean" class="com.main.htmlclient.beans.SearchBean" scope="session"/>
<jsp:setProperty name="searchBean" property="cookieMap" value="${cookie}"/>
<jsp:setProperty name="searchBean" property="parameters" value="${param}"/>
<jsp:setProperty name="searchBean" property="*"/>
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

<%--Authentication check--%>
<c:if test="${searchBean.unauthenticated}">
    <jsp:forward page="login.jsp"/>
</c:if>

<table>

    <tr>
        <td align="right">
            <%--Page number displayed--%>
            <label>Page ${searchBean.page}</label>
            <br>
        </td>
    </tr>

    <tr>
        <td>
            <%--Page content displayed in text area--%>
            <textarea rows="27" cols="100" readonly="readonly"><%= searchBean.getPageContent() %></textarea>
            <br>
        </td>
    </tr>

    <tr>
        <td align="center">
            <%--Page navigation links--%>
            <table id="groupButtonsOnSameLine">
                <tr>
                    <td>
                        <%--Previous page link--%>
                        <c:if test="${searchBean.previousPage != ''}">
                            <form action="search_result.jsp" method="post">
                                <input name="page" type="hidden" value="${searchBean.previousPage}">
                                <c:forEach var="searchRule" items="${searchBean.searchParameters}">
                                    <input name="${searchRule.name}" type="hidden" value="${searchRule.value}">
                                </c:forEach>
                                <input type="submit" value="${searchBean.previousPage}">
                            </form>
                        </c:if>
                    </td>

                    <td>
                        <%--Placeholder between page links--%>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>

                    <td>
                        <%--Next page link--%>
                        <form action="search_result.jsp" method="post">
                            <input name="page" type="hidden" value="${searchBean.nextPage}">
                            <c:forEach var="searchRule" items="${searchBean.searchParameters}">
                                <input name="${searchRule.name}" type="hidden" value="${searchRule.value}">
                            </c:forEach>
                            <input type="submit" value="${searchBean.nextPage}">
                        </form>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<%--add headers from response data--%>
<%
    for (String headerKey : searchBean.getHeaderMap().keySet()) {
        response.addHeader(headerKey, searchBean.getHeaderMap().get(headerKey));
    }
%>

</body>
</html>