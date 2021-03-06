<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/partial/head.jsp"></jsp:include>
	<title><spring:message code="interest" /></title>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="/WEB-INF/partial/navbar.jsp"></jsp:include>

	<!-- filter -->
	<div class="container filter">
		<form class="form-horizontal">
			<fieldset>
				<legend><spring:message code="filter" /></legend>
				<div class="form-group">
					<label class="control-label col-md-2"><spring:message code="name" />:</label>
					<div class="col-md-10">
						<input type="text" name="name" class="form-control">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-md-2"><spring:message code="state" />:</label>
					<div class="col-md-10">
						<select name="state" class="form-control">
							<option value="">--</option>
							<c:forEach items="${statusOption}" var="option">
								<option value="${option.key}"><spring:message code="${option.value}" /></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<button id="filter_search" class="btn btn-default btn-sm center-block">
					<spring:message code="filterApply" />
				</button>
			</fieldset>
		</form>
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
		<button type="button" id="new_btn" class="btn btn-primary btn-sm pull-left">
			<spring:message code="new" />
		</button><br>
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
			<form id="popup_form" class="form-horizontal text-center">
				<jsp:include page="/WEB-INF/partial/interest/detail.jsp"></jsp:include>
			</form>
		</div>
	</div>
	
	<!-- show table -->
	<div class="container">
		<table id="data_table" class="table table-hover">
			<tr>
				<th><spring:message code="name" /></th>
				<th><spring:message code="state" /></th>
				<th></th>
				<th></th>
			</tr>
		</table>
	</div>
	
	<script src="/javaDbPrac/js/interest.js"></script>
</body>
</html>