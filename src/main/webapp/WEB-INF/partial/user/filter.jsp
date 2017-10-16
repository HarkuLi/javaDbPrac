<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="name.text" />:</label>
	<div class="col-md-10">
		<input type="text" name="name" class="form-control">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="birth.text" />:</label>
	<label class="control-label col-md-1"><spring:message code="from.text" /></label>
	<div class="col-md-4">
		<input type="text" name="birthFrom" class="form-control date">
	</div>
	<label class="control-label col-md-1"><spring:message code="to.text" /></label>
	<div class="col-md-4">
		<input type="text" name="birthTo" class="form-control date">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="occupation.text" />:</label>
	<div class="col-md-10">
		<select class="form-control occ_list" name="occ"></select>
	</div>	
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="state.text" />:</label>
	<div class="col-md-10">
		<select name="state" class="form-control">
			<option value="">--</option>
			<option value="1"><spring:message code="enable.text" /></option>
			<option value="0"><spring:message code="disable.text" /></option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="interest.text" />:</label>
	<div class="col-md-10">
		<span class="glyphicon glyphicon-chevron-down icon_btn" id="interest_expand"></span>
		<span class="interest_filter_des"></span>
	</div>
	<br><br>
	<div class="interest_box center-block"></div>
</div>
<button id="filter_search" class="btn btn-default btn-sm center-block"><spring:message code="filterApply.text" /></button>

	
	
	