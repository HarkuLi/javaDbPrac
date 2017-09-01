<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user_photo" id="current_photo" class="mid_img center_block">
<input type="file" class="data" name="photo" id="photo"><br>
<input type="hidden" class="data" name="photoName">
state:
<label>
	<input type="radio" class="data" name="state" value="1" checked>enable
</label>
<label>
	<input type="radio" class="data" name="state" value="0">disable<br>
</label>
name: <input type="text" class="data" name="name"><br>
age: <input type="text" class="data" name="age"><br>
birth: <input type="date" class="data" name="birth"><br>
occupation:
<select id="occ_list" class="data" name="occupation"></select><br>
interests:
<div id="interest_box">
</div>
<input type="submit" value="submit">