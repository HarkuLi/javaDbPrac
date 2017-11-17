<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<title><spring:message code="notFound" /></title>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<h1><spring:message code="notFound" /></h1>
			<br>
			<p><spring:message code="notFound.description" /></p>
			<p class="text-success"><a href="/javaDbPrac/sign_in/page"><spring:message code="backToHome" /></a></p>
		</div>
	</div>	
</body>
</html>