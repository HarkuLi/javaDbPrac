<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/signUp.css">	
	<title><spring:message code="signUp" text="Sign Up" /></title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- sign up form -->
	<div class="container">
		<div class="panel panel-success center-block">
			<form class="form-horizontal" id="sign_up_form" enctype="multipart/form-data">
				<jsp:include page="/WEB-INF/partial/user/detailNew.jsp"></jsp:include>
			</form>
		</div>
	</div>
	
	<script src="/javaDbPrac/js/signUp.js"></script>
</body>
</html>