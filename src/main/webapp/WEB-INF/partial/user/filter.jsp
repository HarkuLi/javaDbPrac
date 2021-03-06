<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="name" />:</label>
	<div class="col-md-10">
		<input type="text" name="name" class="form-control">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="birth" />:</label>
	<label class="control-label col-md-1"><spring:message code="from" /></label>
	<div class="col-md-4">
		<input type="text" name="birthFrom" class="form-control date">
	</div>
	<label class="control-label col-md-1"><spring:message code="to" /></label>
	<div class="col-md-4">
		<input type="text" name="birthTo" class="form-control date">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="occupation" />:</label>
	<div class="col-md-10">
		<select class="form-control occ_list" name="occ"></select>
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
<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="interest" />:</label>
	<div class="col-md-10">
		<span class="glyphicon glyphicon-chevron-down icon_btn" id="interest_expand"></span>
		<span class="interest_filter_des"></span>
	</div>
	<br><br>
	<div class="interest_box center-block"></div>
</div>
<button id="filter_search" class="btn btn-default btn-sm center-block"><spring:message code="filterApply" /></button>

	
	
	