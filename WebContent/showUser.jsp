<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import = "java.util.ArrayList, model.user.UsersModel" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="/javaDbPrac/css/showUser.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<title>show table</title>
</head>
<body>
	<c:set var = "tableList" value = "${requestScope.tableList}" />
	<c:choose>
		<c:when test = "${tableList == null}">
			Not found.
		</c:when>
		<c:otherwise>
		
			<!-- filter -->
			<div id="filter">
				<p>Filter:</p>
				<strong>Name: </strong><input type="text" id="name_filter"><br>
				<strong>Birth: </strong>From <input type="date" id="birth_from_filter"> To <input type="date" id="birth_to_filter"><br>
				<button id="filter_search">Go</button>
			</div>
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
						<i class="material-icons close_btn">clear</i>
					</div>
					<!-- form for new user -->
					<div class="txt_center">
						<form id="popup_form">
							<jsp:include page="partial/user/detail.jsp"></jsp:include>
						</form>
					</div>
				</div>
			</div>
			
			<!-- show table -->
			<table id="data_table">
				<tr>
					<th>name</th>
					<th>age</th>
					<th>birth</th>
					<th></th>
					<th></th>
				</tr>
			<c:forEach var = "ele" items = "${tableList}">
				<tr class="data_rows">
					<td class="id"><input value="${ele.getId()}" name="id" size="3" disabled></td>
					<td><input value="${ele.getName()}" name="name" disabled></td>
					<td><input value="${ele.getAge()}" name="age" size="3" disabled></td>
					<td><input value="${ele.getBirth()}" name="birth" size="10" disabled></td>
					<td><button class="edit">edit</button></td>
					<td><button class="delete">delete</button></td>
				</tr>
			</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
	
	<script src="/javaDbPrac/js/showUser.js"></script>
</body>
</html>