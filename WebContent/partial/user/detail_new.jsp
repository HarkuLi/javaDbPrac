<input type="hidden" class="data" name="id">
<img src="/javaDbPrac/user/photo" class="mid_img center-block">
<input type="file" class="data" name="photo"><br>
<input type="hidden" class="data" name="photoName">

<strong>state:</strong>
<label>
	<input type="radio" class="data" name="state" value="1" checked>enable
</label>
<label>
	<input type="radio" class="data" name="state" value="0">disable
</label>
<br>

<label>
account: <input type="text" class="data" name ="account">
</label>
<br>

<label>
password: <input type="password" class="data" name="password">
</label>
<br>

<label>
	check password: <input type="password" class="data" name="passwordCheck">
</label>
<br>

<label>
name: <input type="text" class="data" name="name">
</label>
<br>

<label>
age: <input type="text" class="data" name="age">
</label>
<br>

<label>
birth: <input type="date" class="data" name="birth">
</label>
<br>

<label>
occupation:
<select class="data occ_list" name="occupation"></select>
</label>
<br>

<strong>interests:</strong><br><br>
<div class="interest_box"></div>

<input type="submit" value="submit" id="user_create" class="btn btn-default btn-sm">


