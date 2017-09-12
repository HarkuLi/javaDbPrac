<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/sign_in.css">	
	<title>Sign in</title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="partial/navbar.jsp"></jsp:include>
	
	<!-- sign in form -->
	<div class="container first_block">
		<div class="panel panel-success center-block">
			<form class="form-horizontal">
				<div class="form-group">
					<label class="control-label col-md-3" for="account">Account:</label>
					<div class="col-md-9">
						<input type="text" class="form-control" name="account" id="account">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-md-3" for="password">Password:</label>
					<div class="col-md-9">
						<input type="password" class="form-control" name="password" id="password">
					</div>
				</div>
				<button type="submit" class="btn btn-success center-block">Sign in</button>
			</form>
		</div>
		<div class="panel panel-success center-block text-center">
			<span>No account? <a href="#">Create an account</a></span>
		</div>
	</div>
</body>
</html>