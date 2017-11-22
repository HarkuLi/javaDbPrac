<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<title><spring:message code="welcome" /></title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- welcome message -->
	<div class="container">
		<div class="jumbotron text-center">
			<h1><spring:message code="welcome" /></h1>
			<br>
			<p><spring:message code="site.description" /></p>
		</div>
	</div>
</body>
</html>