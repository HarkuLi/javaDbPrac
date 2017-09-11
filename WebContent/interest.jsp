<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="partial/head.jsp"></jsp:include>
	<title>interest</title>
</head>
<body>
	<!-- breadcrumbs -->
	<div class="container">
		<jsp:include page="partial/breadcrumbs.jsp"></jsp:include>
	</div>

	<!-- filter -->
	<div class="container filter">
		<fieldset>
			<legend>Filter</legend>
			<strong>Name: </strong><input type="text" name="name"><br>
			<strong>State: </strong>
			<select name="state">
				<option value="">--</option>
				<option value="1">enable</option>
				<option value="0">disable</option>
			</select><br>
			<button id="filter_search" class="btn btn-default btn-sm center-block">Go</button>
		</fieldset>
	</div>
	<br>
	
	<div class="container text-center">
		<!-- paging -->
		<div class="paging_row">
			<button type="button" id="last_page" class="btn btn-default btn-sm">◀</button>
	        <input class="text-center" type="text" name="page" value="1" size="3" disabled>
			<button type="button" id="next_page" class="btn btn-default btn-sm">▶</button>
		</div>
		
		<!-- new occupation -->
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
			<!-- form for new occupation -->
			<div class="text-center">
				<form id="popup_form">
					<jsp:include page="partial/interest/detail.jsp"></jsp:include>
				</form>
			</div>
		</div>
	</div>
	
	<!-- show table -->
	<div class="container">
		<table id="data_table" class="table table-hover">
			<tr>
				<th>name</th>
				<th>state</th>
				<th></th>
				<th></th>
			</tr>
		</table>
	</div>
	
	<script src="/javaDbPrac/js/interest.js"></script>
</body>
</html>