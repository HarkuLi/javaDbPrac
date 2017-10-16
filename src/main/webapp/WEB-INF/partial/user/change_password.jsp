<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="account.text" />:</label>
	<div class="col-md-7">
		<input type="text" class="data form-control" name="account" disabled>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="newPassword.text" />:</label>
	<div class="col-md-7">
		<input type="password" class="data form-control" name="password">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-5"><spring:message code="checkPassword.text" />:</label>
	<div class="col-md-7">
		<input type="password" class="data form-control" name="passwordCheck">
	</div>
</div>
<button type="submit" class="btn btn-default btn-sm center-block"><spring:message code="submit.text" /></button>