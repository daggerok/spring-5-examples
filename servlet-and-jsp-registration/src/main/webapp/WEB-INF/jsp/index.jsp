<!DOCTYPE html>
<html lang="en">

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<body>
<h1>${message}</h1>

<spring:url value="/jsp" htmlEscape="true" var="springUrl" />
<c:url value="/jsp" var="url"/>

<p><a href="${springUrl}">Spring says hola</a></p>
<p><a href="${url}">JSTL says hello</a></p>
<p><a href="/jsp">А я говорю тебе: Превед!</a></p>

</body>
</html>
