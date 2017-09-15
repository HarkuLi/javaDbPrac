<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" id="current_photo" class="mid_img center-block">
<input type="file" class="data" name="photo"><br>
<input type="hidden" class="data" name="photoName">

<div class="form-group">
	<label class="control-label col-md-2">State:</label>
	<div class="radio col-md-offset-1 col-md-4">
		<label><input type="radio" class="data" name="state" value="1"> enable</label>
	</div>
	<div class="radio col-md-5">
		<label><input type="radio" class="data" name="state" value="0"> disable</label>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Account:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="account" disabled>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Password:</label>
	<label class="control-label col-md-offset-1 col-md-3"><a href="" id="change_password">change</a></label>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Name:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="name">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Age:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control" name="age">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Birth:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="text" class="data form-control date" name="birth">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Occupation:</label>
	<div class="col-md-offset-2 col-md-8">
		<select class="data occ_list form-control" name="occupation"></select>
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Interests:</label>
	<br><br>
	<div class="col-md-offset-1 interest_box"></div>
</div>

<button type="submit" id="user_modify" class="btn btn-default btn-sm center-block">submit</button>
<div class="blank_block"></div>


