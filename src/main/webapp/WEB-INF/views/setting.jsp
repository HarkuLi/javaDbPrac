<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/setting.css">
	<title><spring:message code="setting" /></title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- sign up form -->
	<div class="container">
		<div class="panel panel-success center-block">
			<form class="form-horizontal" id="setting_form" enctype="multipart/form-data">
				<div class="form-group">
					<label class="control-label col-md-3"><spring:message code="language" />:</label>
					<div class="col-md-9">
						<select class="form-control" name="language">
							<option value="zh_TW"><spring:message code="chinese_taiwan" /></option>
							<option value="en_US"><spring:message code="english_america" /></option>
							<option value="ja_JP"><spring:message code="japanese" /></option>
						</select>
					</div>
				</div>
				<button type="submit" id="save_setting" class="btn btn-default btn-sm center-block">
					<spring:message code="save" />
				</button>
			</form>
		</div>
	</div>
	
	<script src="/javaDbPrac/js/setting.js"></script>
</body>
</html>