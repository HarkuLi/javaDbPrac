<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" id="current_photo" class="mid_img center-block">
<br>
<input type="file" class="data center-block" name="photo"><br>
<input type="hidden" class="data" name="photoName">

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="state.text" />:</label>
	<div class="radio col-md-offset-1 col-md-4">
		<label><input type="radio" class="data" name="state" value="1"> <spring:message code="enable.text" /></label>
	</div>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="0"> <spring:message code="disable.text" /></label>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="account.text" />:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="account" disabled>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="password.text" />:</label>
	<label class="control-label col-md-offset-1 col-md-2">
		<a href="" id="change_password"><spring:message code="change.text" /></a>
	</label>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="name.text" />:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="name">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="age.text" />:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="age">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="birth.text" />:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control date" name="birth">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="occupation.text" />:</label>
	<div class="col-md-offset-2 col-md-8">
		<select class="data occ_list form-control" name="occupation"></select>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2"><spring:message code="interest.text" />:</label>
	<br><br>
	<div class="col-md-offset-2 interest_box"></div>
</div>

<button type="submit" id="user_modify" class="btn btn-default btn-sm center-block"><spring:message code="submit.text" /></button>
<div class="blank_block"></div>


