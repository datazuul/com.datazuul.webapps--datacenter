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
        <title>Contact: <c:out value="${contact.firstname}"/> <c:out value="${contact.lastname}"/></title>
    </head>
    <body>
        <h1>Contact: <c:out value="${contact.firstname}"/> <c:out value="${contact.lastname}"/></h1>
        <table>
            <tbody>
                <tr>
                    <th>Email: </th><td><c:out value="${contact.email}"/></td>
                </tr>
            </tbody>
        </table>
        <a href="list.do">&lt;&lt; back</a>
    </body>
</html>
