<div class="form-group">
	<label class="control-label col-md-2">Name:</label>
	<div class="col-md-10">
		<input type="text" name="name" class="form-control">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2">Birth:</label>
	<label class="control-label col-md-1">From</label>
	<div class="col-md-4">
		<input type="date" name="birthFrom" class="form-control">
	</div>
	<label class="control-label col-md-1">To</label>
	<div class="col-md-4">
		<input type="date" name="birthTo" class="form-control">
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2">Occupation:</label>
	<div class="col-md-10">
		<select class="form-control occ_list" name="occ"></select>
	</div>	
</div>
<div class="form-group">
	<label class="control-label col-md-2">State:</label>
	<div class="col-md-10">
		<select name="state" class="form-control">
			<option value="">--</option>
			<option value="1">enable</option>
			<option value="0">disable</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-2">Interests:</label>
	<div class="col-md-10">
		<span class="glyphicon glyphicon-chevron-down icon_btn" id="interest_expand"></span>
		<span class="interest_filter_des"></span>
	</div>
	<br><br>
	<div class="interest_box center-block"></div>
</div>
<button id="filter_search" class="btn btn-default btn-sm center-block">Go</button>

	
	
	