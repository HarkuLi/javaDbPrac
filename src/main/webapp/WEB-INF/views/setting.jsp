<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/setting.css">
	<title>Setting</title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- sign up form -->
	<div class="container">
		<div class="panel panel-success center-block">
			<form class="form-horizontal" id="setting_form" enctype="multipart/form-data">
				<div class="form-group">
					<label class="control-label col-md-3">Language:</label>
					<div class="col-md-9">
						<select class="form-control" name="language">
							<option value="zh_TW">Chinese(Taiwan)</option>
							<option value="en_US">English(America)</option>
							<option value="ja_JP">Japan</option>
						</select>
					</div>
				</div>
				<button type="submit" id="save_setting" class="btn btn-default btn-sm center-block">submit</button>
			</form>
		</div>
	</div>
	
	<script src="/javaDbPrac/js/setting.js"></script>
</body>
</html>