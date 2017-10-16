<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="name.text" />:</label>
	<div class="col-md-10">
		<input type="text" class="data form-control" name="name">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="state.text" />:</label>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="1"> <spring:message code="enable.text" /></label>
	</div>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="0"> <spring:message code="disable.text" /></label>
	</div>
</div>
<button type="submit" class="btn btn-default btn-sm"><spring:message code="submit.text" /></button>