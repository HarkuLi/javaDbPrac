<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" class="mid_img center_block">
<input type="file" class="data" name="photo"><br>
<input type="hidden" class="data" name="photoName">
state:
<label>
	<input type="radio" class="data" name="state" value="1" checked>enable
</label>
<label>
	<input type="radio" class="data" name="state" value="0">disable<br>
</label>
<label>
account: <input type="text" class="data" name ="account"><br>
</label>
<label>
password: <input type="password" class="data" name="password"><br>
</label>
<label>
	check password: <input type="password" class="data" name="passwordCheck"><br>
</label>
<label>
name: <input type="text" class="data" name="name"><br>
</label>
<label>
age: <input type="text" class="data" name="age"><br>
</label>
<label>
birth: <input type="date" class="data" name="birth"><br>
</label>
<label>
occupation:
<select class="data occ_list" name="occupation"></select><br>
</label>
interests:
<div class="interest_box"></div>

<input type="submit" value="submit" id="user_create">