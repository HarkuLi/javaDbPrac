<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="account" />:</label>
	<div class="col-md-7">
		<input type="text" class="data form-control" name="account" disabled>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="newPassword" />:</label>
	<div class="col-md-7">
		<input type="password" class="data form-control" name="password">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="checkPassword" />:</label>
	<div class="col-md-7">
		<input type="password" class="data form-control" name="passwordCheck">
	</div>
</div>
<button type="submit" class="btn btn-default btn-sm center-block"><spring:message code="submit" /></button>