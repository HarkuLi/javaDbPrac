<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="name" />:</label>
	<div class="col-md-10">
		<input type="text" class="data form-control" name="name">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="state" />:</label>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="1"> <spring:message code="enable" /></label>
	</div>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="0"> <spring:message code="disable" /></label>
	</div>
</div>
<button type="submit" class="btn btn-default btn-sm"><spring:message code="submit" /></button>