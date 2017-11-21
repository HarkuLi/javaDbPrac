<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" id="current_photo" class="mid_img center-block">
<br>
<input type="file" class="data center-block" name="photo"><br>
<input type="hidden" class="data" name="photoName">

<div class="form-group">
	<label><spring:message code="state" /><span class="text-danger"> *</span>:</label>
	<div class="row radio">
		<c:forEach items="${statusOption}" var="option">
			<div class="col-md-6">
				<label>
					<input type="radio" class="data" name="state" value="${option.key}"> <spring:message code="${option.value}" />
				</label>
			</div>
		</c:forEach>
	</div>
</div>

<div class="form-group">
	<label><spring:message code="account" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="account" disabled>
</div>

<div class="form-group">
	<div class="row">
		<label class="col-md-3"><spring:message code="password" />:</label>
		<label class="col-md-2">
			<a href="" id="change_password"><spring:message code="change" /></a>
		</label>
	</div>
</div>

<div class="form-group">
	<label><spring:message code="name" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="name">
</div>

<div class="form-group">
	<label><spring:message code="age" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="age">
</div>

<div class="form-group">
	<label><spring:message code="birth" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control date" name="birth">
</div>

<div class="form-group">
	<label><spring:message code="occupation" /><span class="text-danger"> *</span>:</label>
	<select class="data occ_list form-control" name="occupation"></select>
</div>

<div class="form-group">
	<label><spring:message code="interest" />:</label>
	<br><br>
	<div class="col-md-offset-1 interest_box"></div>
</div>

<button type="submit" id="user_modify" class="btn btn-default btn-sm center-block"><spring:message code="submit" /></button>
<div class="blank_block"></div>

<script src="/javaDbPrac/js/datepickerLoader.js"></script>


