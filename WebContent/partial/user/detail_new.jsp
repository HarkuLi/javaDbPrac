<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" class="mid_img center-block">
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
		<input type="text" class="data form-control" name="account">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Password:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="password" class="data form-control" name="password">
	</div>
</div>

<div class="form-group">
	<label class="control-label col-md-2">Check Password:</label>
	<div class="col-md-offset-1 col-md-9">
		<input type="password" class="data form-control" name="passwordCheck">
	</div>
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
		<input type="date" class="data form-control" name="birth">
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

<button type="submit" id="user_create" class="btn btn-default btn-sm center-block">submit</button>
<div class="blank_block"></div>

