<%-- 
    Document   : list
    Author     : ralf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Contacts Page</title>
    </head>
    <body>
        <h1>Contacts</h1>
        <table border="1">
            <thead>
                <tr>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="contact" items="${contactList}" varStatus="status">
                    <tr>
                        <td><c:out value="${contact.firstname}"></c:out></td>
                        <td><c:out value="${contact.lastname}"></c:out></td>
                        <td><c:out value="${contact.email}"></c:out></td>
                        <td><a href="detail.html?id=${contact.id}">view</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <a href="create">add</a>
    </body>
</html>
