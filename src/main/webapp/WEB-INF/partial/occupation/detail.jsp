<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<c:forEach items="${statusOption}" var="option">
		<div class="radio col-md-5">
			<label>
				<input type="radio" class="data" name="state" value="${option.key}"> <spring:message code="${option.value}" />
			</label>
		</div>
	</c:forEach>
</div>
<button type="submit" class="btn btn-default btn-sm"><spring:message code="submit" /></button>