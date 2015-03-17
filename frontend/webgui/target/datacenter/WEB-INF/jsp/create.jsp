<%-- 
    Document   : form
    Author     : ralf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Add new contact</title>
    </head>
    <body>
        <h1>Add new contact</h1>
        <form:form modelAttribute="contact">
            <form:errors path="*">
                <div><spring:message code="error.global"/></div>
            </form:errors>

            <div>First name: <form:input path="firstname"/></div>
            <form:errors path="firstname">
                <div><form:errors path="firstname" htmlEscape="false" /></div>
            </form:errors>

            <div>Last name: <form:input path="lastname"/></div>
            <form:errors path="lastname">
                <div><form:errors path="lastname" htmlEscape="false" /></div>
            </form:errors>

            <div>Email: <form:input path="email"/></div>
            <form:errors path="email">
                <div><form:errors path="email" htmlEscape="false" /></div>
            </form:errors>

            <div><input type="submit" values="save"/></div>
            </form:form>
        <a href="list.do">&lt;&lt; back</a>
    </body>
</html>
