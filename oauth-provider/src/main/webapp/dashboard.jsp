<%-- 
    Document   : dashboard
    Created on : 29-Apr-2015, 20:08:43
    Author     : Josue
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns:c="http://java.sun.com/jsp/jstl/core">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Dashboard...</h1>
        <h3>Hello <c:out value="${user.name}" /></h3>
        <form action="${pageContext.request.contextPath}/logout">
            <input type="submit" value="Logout">
        </form>

        <h1>Your authorized applications</h1>
        <table>
            <tbody>
                <tr>
                    <th>Application name</th>
                    <th>Revoke</th>
                </tr>
            <c:forEach items="${tokenz}" var="token">
                <tr>
                    <td><c:out value="${token.application.name}" /></td>
                <td>
                    <form method="POST" action="${pageContext.request.contextPath}/dashboard">
                        <input type="hidden" name="tokenId" value="${token.id}" />
                        <input type="submit" value="Revoke">
                    </form>
                </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
