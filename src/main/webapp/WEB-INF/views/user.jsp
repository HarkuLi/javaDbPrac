<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/user.css">	
	<title><spring:message code="user.text" /></title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>
	
	<!-- filter -->
	<div class="container filter">
		<form class="form-horizontal">
			<fieldset>
				<legend><spring:message code="filter.text" /></legend>
				<jsp:include page="/WEB-INF/partial/user/filter.jsp"></jsp:include>
			</fieldset>
		</form>
		<br>
	</div>
	
	<div class="container text-center">
		<!-- paging -->
		<div class="paging_row">
			<button type="button" id="last_page" class="btn btn-default btn-sm">◀</button>
		    <input class="text-center" type="text" name="page" value="1" size="3" disabled>
			<button type="button" id="next_page" class="btn btn-default btn-sm">▶</button>
		</div>
		
		<!-- new users -->
		<button type="button" id="new_btn" class="btn btn-primary btn-sm pull-left"><spring:message code="new.text" /></button><br>
	</div>
	<br>
	
	<!-- pop up window -->
	<div class="mask">
		<div class="popup_window">
			<!-- close button -->
			<div class="text-right">
				<i class="material-icons close_btn icon_btn">clear</i>
			</div>
			<!-- pop up content -->
			<div class="popup_content">
				<form id="popup_form_new" class="center-block" enctype="multipart/form-data"></form>
				<form id="popup_form_edit" class="center-block" enctype="multipart/form-data"></form>
				<form id="popup_form_chpw" class="form-horizontal center-block" enctype="multipart/form-data"></form>
			</div>
		</div>
	</div>
	
	<!-- show table -->
	<div class="container">
		<table id="data_table" class="table table-hover">
			<tr>
				<th><spring:message code="photo.text" /></th>
				<th><spring:message code="account.text" /></th>
				<th><spring:message code="name.text" /></th>
				<th><spring:message code="age.text" /></th>
				<th><spring:message code="birth.text" /></th>
				<th><spring:message code="occupation.text" /></th>
				<th><spring:message code="interest.text" /></th>
				<th><spring:message code="state.text" /></th>
				<th></th>
				<th></th>
			</tr>
		</table>
	</div>
	
	<script src="/javaDbPrac/js/showUser.js"></script>
</body>
</html>