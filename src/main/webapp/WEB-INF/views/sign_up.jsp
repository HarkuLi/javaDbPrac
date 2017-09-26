<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/sign_up.css">	
	<title>Sign up</title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- sign up form -->
	<div class="container">
		<div class="panel panel-success center-block">
			<form class="form-horizontal" id="sign_up_form" enctype="multipart/form-data">
				<jsp:include page="/WEB-INF/partial/user/detail_new.jsp"></jsp:include>
			</form>
		</div>
	</div>
	
	<script src="/javaDbPrac/js/sign_up.js"></script>
</body>
</html>