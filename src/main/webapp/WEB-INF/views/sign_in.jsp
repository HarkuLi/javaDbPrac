<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/sign_in.css">	
	<title>Sign in</title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- sign in form -->
	<div class="container">
		<div class="panel panel-success center-block">
			<form:form class="form-horizontal" modelAttribute="account_form" method="post" action="/javaDbPrac/sign_in/page" enctype="multipart/form-data">
				<div class="form-group">
					<form:label class="control-label col-md-3" path="account">Account:</form:label>
					<div class="col-md-9">
						<form:input class="form-control" path="account" />
					</div>
				</div>
				<div class="form-group">
					<form:label class="control-label col-md-3" path="password">Password:</form:label>
					<div class="col-md-9">
						<form:password class="form-control" path="password" />
					</div>
				</div>
				<p><form:errors cssClass="err_msg" path="*" /></p>
				<button type="submit" class="btn btn-success center-block">Sign in</button>
			</form:form>
		</div>
		<div class="panel panel-success center-block text-center">
			<span>No account? <a href="/javaDbPrac/sign_up/page">Create an account</a></span>
		</div>
	</div>
</body>
</html>