<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/global.css">
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/user.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<title>user</title>
</head>
<body>
	<!-- breadcrumbs -->
	<jsp:include page="partial/breadcrumbs.jsp"></jsp:include>

	<!-- filter -->
	<form class="filter">
		<p>Filter:</p>
		<strong>Name: </strong><input type="text" name="name"><br>
		<strong>Birth: </strong>
		From <input type="date" name="birthFrom">
		To <input type="date" name="birthTo"><br>
		<strong>Occupation: </strong>
		<select class="occ_list" name="occ"></select><br>
		<strong>State: </strong>
		<select name="state">
			<option value="">--</option>
			<option value="1">enable</option>
			<option value="0">disable</option>
		</select><br>
		<strong>interests: </strong>
		<span class="interest_filter_des"></span>
		<i class="material-icons icon_btn" id="interest_expand">expand_more</i><br>
		<div class="interest_box"></div>
		<button id="filter_search">Go</button>
	</form>
	<br>
	
	<!-- paging -->
	<div class="paging_row">
		<button type="button" id="last_page">◀</button>
           <input class="txt_center" type="text" name="page" value="1" size="3" disabled>
		<button type="button" id=next_page>▶</button>
	</div>
	
	<!-- new users -->
	<button id="new_btn">new</button><br>
	<br>
	
	<!-- pop up window -->
	<div class="mask">
		<div class="popup_window">
			<!-- close button -->
			<div class="txt_right">
				<i class="material-icons close_btn icon_btn">clear</i>
			</div>
			<!-- pop up content -->
			<div class="container">
				<form id="popup_form_new" class="center_block" enctype="multipart/form-data">
					<jsp:include page="partial/user/detail_new.jsp"></jsp:include>
				</form>
				<form id="popup_form_edit" class="center_block" enctype="multipart/form-data">
					<jsp:include page="partial/user/detail_edit.jsp"></jsp:include>
				</form>
				<form id="popup_form_chpw" class="center_block">
					<jsp:include page="partial/user/change_password.jsp"></jsp:include>
				</form>
			</div>
		</div>
	</div>
	
	<!-- show table -->
	<table id="data_table">
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
	
	<script src="/javaDbPrac/js/showUser.js"></script>
</body>
</html>