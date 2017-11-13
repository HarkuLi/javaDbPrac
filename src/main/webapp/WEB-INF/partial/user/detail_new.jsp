<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/default.png" class="mid_img center-block">
<br>
<input type="file" class="data center-block" name="photo"><br>
<input type="hidden" class="data" name="photoName">

<div class="form-group">
	<label><spring:message code="state.text" /><span class="text-danger"> *</span>:</label>
	<div class="row radio">
		<div class="col-md-6">
			<label><input type="radio" class="data" name="state" value="1"> <spring:message code="enable.text" /></label>
		</div>
		<div class="col-md-6">
			<label><input type="radio" class="data" name="state" value="0"> <spring:message code="disable.text" /></label>
		</div>
	</div>
</div>

<div class="form-group">
	<label><spring:message code="account.text" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="account">
</div>

<div class="form-group">
	<label><spring:message code="password.text" /><span class="text-danger"> *</span>:</label>
	<input type="password" class="data form-control" name="password">
</div>

<div class="form-group">
	<label><spring:message code="checkPassword.text" /><span class="text-danger"> *</span>:</label>
	<input type="password" class="data form-control" name="passwordCheck">
</div>

<div class="form-group">
	<label><spring:message code="name.text" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="name">
</div>

<div class="form-group">
	<label><spring:message code="age.text" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control" name="age">
</div>

<div class="form-group">
	<label><spring:message code="birth.text" /><span class="text-danger"> *</span>:</label>
	<input type="text" class="data form-control date" name="birth">
</div>

<div class="form-group">
	<label><spring:message code="occupation.text" /><span class="text-danger"> *</span>:</label>
	<select class="data occ_list form-control" name="occupation"></select>
</div>

<div class="form-group">
	<label><spring:message code="interest.text" />:</label>
	<br><br>
	<div class="col-md-offset-1 interest_box"></div>
</div>

<button type="submit" id="user_create" class="btn btn-default btn-sm center-block"><spring:message code="submit.text" /></button>
<div class="blank_block"></div>

<script src="/javaDbPrac/js/datepickerLoader.js"></script>

