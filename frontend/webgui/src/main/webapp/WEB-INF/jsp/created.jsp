<%-- 
    Document   : created
    Author     : ralf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Contact added</title>
    </head>
    <body>
        <h1>Contact added</h1>
        <div>First name: <c:out value="${contact.firstname}"></c:out></div>
        <div>Last name: <c:out value="${contact.lastname}"></c:out></div>
        <div>Email: <c:out value="${contact.email}"></c:out></div>
        <a href="list.do">&lt;&lt; back</a>
    </body>
</html>
