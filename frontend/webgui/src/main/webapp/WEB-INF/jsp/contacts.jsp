<%-- 
    Document   : contacts
    Created on : Jun 10, 2014, 6:34:56 PM
    Author     : ralf
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message var="pageTitle" code="contacts.home.pageTitle" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>${pageTitle}</title>
    </head>
    <body>
        <h1><spring:message code="contacts_of"/> ${name}!</h1>
    </body>
</html>
