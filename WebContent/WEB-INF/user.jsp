<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../partial/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/user.css">	
	<title>user</title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="../partial/navbar.jsp"></jsp:include>

	<!-- filter -->
	<div class="container filter first_block">
		<form class="form-horizontal">
			<fieldset>
				<legend>Filter</legend>
				<jsp:include page="../partial/user/filter.jsp"></jsp:include>
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
		<button type="button" id="new_btn" class="btn btn-primary btn-sm pull-left">new</button><br>
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
				<form id="popup_form_new" class="form-horizontal center-block" enctype="multipart/form-data">
					<jsp:include page="../partial/user/detail_new.jsp"></jsp:include>
				</form>
				<form id="popup_form_edit" class="form-horizontal center-block" enctype="multipart/form-data">
					<jsp:include page="../partial/user/detail_edit.jsp"></jsp:include>
				</form>
				<form id="popup_form_chpw" class="form-horizontal center-block" enctype="multipart/form-data">
					<jsp:include page="../partial/user/change_password.jsp"></jsp:include>
				</form>
			</div>
		</div>
	</div>
	
	<!-- show table -->
	<div class="container">
		<table id="data_table" class="table table-hover">
			<tr>
				<th>photo</th>
				<th>name</th>
				<th>age</th>
				<th>birth</th>
				<th>occupation</th>
				<th>interests</th>
				<th>state</th>
				<th></th>
				<th></th>
			</tr>
		</table>
	</div>
	
	<script src="/javaDbPrac/js/showUser.js"></script>
</body>
</html>