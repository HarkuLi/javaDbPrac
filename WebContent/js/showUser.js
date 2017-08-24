/**
 * 
 */
var currentPage = 1;
var nameFilter = "";
var birthFilter_from = "";
var birthFilter_to = "";

///////////
// ready //
///////////
$(() => {
	selectPage(currentPage);
	
  ///////////////
  // listeners //
  ///////////////
  $("#last_page").on("click", () => {
    selectPage(currentPage-1);
  });
  
  $("#next_page").on("click", () => {
    selectPage(currentPage+1);
  });
  
  $("#filter_search").on("click", () => {
  	if(!isFilterChange()) return;
  	filterSearch();
  });
  
  $("#popup_form").on("click", "#user_create", function(event){
  	event.preventDefault();
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
  	newUser()
  		.then(data => {
  			if(data.errMsg) return alert(data.errMsg);
  			selectPage(currentPage);
  	  	closePopup();
  		});
  });
  
  $("#new_btn").on("click", () => {
  	//set pop up window
  	$(".mask").css("display", "flex");
  	$(".mask").prop("id", "new_block");
  	$("#popup_form").children(":submit").prop("id", "user_create");
  });
  
  $(".close_btn").on("click", function(){
  	closePopup();
  });
  
  $("#data_table").on("click", ".edit", function(){
  	var self = this;
  	$("body").css("cursor", "progress");
  	edit(self)
  		.then(() => {
  		$("body").css("cursor", "");
  	});
  });
  
  $("#data_table").on("click", ".delete", function(){
  	var self = this;
  	delRow(self)
  		.then(rst => {
  			if(!rst) return;
  			//reload the page
  			selectPage(currentPage);
  		});
  });
  
  $("#popup_form").on("click", "#user_modify", function(event){
  	event.preventDefault();
  	
  	var self = this;
  	save(self);
  });
});

///////////////
// functions //
///////////////

function closePopup(){
	clrFields($("#popup_form"));
	$(".mask").prop("id", "");
	$(".mask").css("display", "");
	$("#popup_form").children(":submit").prop("id", "");
}

/**
 * 
 * @return {Promise} return: Boolean
 */
function delRow(self){
	var check = confirm("[Warning] Delete the data?");
	if(!check) return Promise.resolve(false);
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return doDel(id)
		.then(() => {
			return true;
		});
}

/**
 * 
 * @param self {Object} copy of this
 * @return {Promise}
 */
function save(self){
	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
	
	var passedData = {};
	var inputList = $("#popup_form").children(".data");
	
	//record the input values
	for(let ele of inputList){
		let propName = $(ele).prop("name");
		let val = $(ele).prop("value");
		passedData[propName] = val;
	}
	
	//update the change
	return doUpdate(passedData)
  	.then(data => {
  		if(data.errMsg){
  			alert(data.errMsg);
  			return false;
  		}
  		return true;
  	})
  	.then(rst => {
  		if(!rst) return;
  		closePopup();
  	});
}

/**
 * 
 * @param self {Object} copy of this
 * @return {Promise}
 */
function edit(self){
	var data = {};
	
	//set pop up window
	$(".mask").prop("id", "edit_block");
	$("#popup_form").children(":submit").prop("id", "user_modify");
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getUser(id)
		.then(data => {
			//set pop up form
			var inputList = $("#popup_form").children(".data");
			for(let ele of inputList){
				let propName = $(ele).prop("name");
				$(ele).prop("value", data[propName]);
			}
			
			//show pop up window
			$(".mask").css("display", "flex");
		});
}

/**
 * @param form {Object} jquery element
 */
function clrFields(form){
	var inputList = $(form).children(".data");
	
	for(let ele of inputList){
		$(ele).prop("value", "");
	}
}

/**
 * @param form {Object} jquery element
 * @return {Boolean}
 */
function isFillAll(form){
	const exceptList = ["id"];
	
	var inputList = $(form).children(".data");
	
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) return false;
	}
	return true;
}

function filterSearch(){
	var dateFormatMsg = "wrong date format, correct format: YYYY-MM-DD.";
	var birthFrom = $("#birth_from_filter").prop("value");
	var birthTo = $("#birth_to_filter").prop("value");
	
	//check input format
	var reg = /^\d{4}-\d{2}-\d{2}$/;
	if(birthFrom.length && !reg.test(birthFrom))
		return alert(dateFormatMsg);
  if(birthTo.length && !reg.test(birthTo))
  	return alert(dateFormatMsg);
		
	//record new filters
	nameFilter = $("#name_filter").prop("value");
	birthFilter_from = birthFrom;
	birthFilter_to = birthTo;
	
	//go to page 1
	currentPage = 1;
	selectPage(1);
}

/**
 * 
 * @return {Boolean}
 */
function isFilterChange(){
	if($("#name_filter").prop("value") !== nameFilter) return true;
	if($("#birth_from_filter").prop("value") !== birthFilter_from) return true;
	if($("#birth_to_filter").prop("value") !== birthFilter_to) return true;
	return false;
}

/**
 * go to the page
 * can be used for reload by select current page
 * @param page {Number}
 */
function selectPage(page){
	if(page < 1) return;
  
	currentPage = page;
	$("body").css("cursor", "progress");
  
  getList(page)
  	.then(data => {
  		renderData(data.list);
  		pageNumDisp(data.totalPage);
  		$("body").css("cursor", "");
  	});
}

/**
 * change the values of existing row elements
 * hide and clean the values of the redundant rows
 * add new row elements if existing row elements are not enough
 * @param dataList {Array<Object>}
 */
function renderData(dataList){
	if(!dataList){
		$("#data_table").css("display", "none");
		return;
	}
	
	const sizeMap = {
		id: 3,
		name: 15,
		age: 3,
		birth: 10
	};
	
	$("#data_table").css("display", "");
	
	//deal with existing row elements
	var idx = 0;
	var rowList = $("#data_table").find(".data_rows");
	for(let row of rowList){
		let inputList = $(row).find("input");
		
		//number of row elements > data
	  //hide and clean values of the redundant rows
		if(!dataList[idx]){
			for(let ele of inputList){
				$(ele).prop("value", "");
			}
			$(row).css("display", "none");
			++idx;
			continue;
		}
		
		for(let ele of inputList){
			let prop = $(ele).prop("name");
			$(ele).prop("value", dataList[idx][prop]);
		}
		$(row).css("display", "");
		++idx;
	}
	
	//number of existing row elements < data
	//add and set new row elements
	for(; idx<dataList.length; ++idx){
		let data = dataList[idx];
		let dataRow = $("<tr></tr>");
		let rowEntry;
		let input;
		let btn;
		
		dataRow.prop("class", "data_rows");
		
		for(let prop in data){
			rowEntry = $("<td></td>");
			if(prop === "id")	rowEntry.prop("class", "id");
			input = $("<input>");
			input.prop("name", prop);
			input.prop("value", data[prop]);
			input.prop("disabled", true);
			input.prop("size", sizeMap[prop]);
			rowEntry.append(input);
			dataRow.append(rowEntry);
		}
		
		rowEntry = $("<td></td>");
		btn = $("<button></button>");
		btn.prop("class", "edit");
		btn.text("edit");
		rowEntry.append(btn);
		dataRow.append(rowEntry);
		
		rowEntry = $("<td></td>");
		btn = $("<button></button>");
		btn.prop("class", "delete");
		btn.text("delete");
		rowEntry.append(btn);
		dataRow.append(rowEntry);
		
		$("#data_table").append(dataRow);
	}
}

/**
 * 
 * @param totalPage {Number} total page of data
 */
function pageNumDisp(totalPage){
	if(currentPage > totalPage)	currentPage = totalPage;
	$(".paging_row > input").val(currentPage);
}

  ////////////////////
  // ajax functions //
  ////////////////////

/**
 * 
 * @param id {String}
 * @return {Promise} no return value
 */
function doDel(id){
	return new Promise((resolve, reject) => {
		$.post("del_user", {id}, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
	});
}

/**
 * 
 * @param passedData {Object} {id: String, name: String, age: String, birth: String}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doUpdate(passedData){
	return new Promise((resolve, reject) => {
		$.post("update_user", passedData, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
	});
}

/**
 * 
 * @return {Promise} if error, return: {errMsg: String}
 */
function newUser(){
	var passedData = {};
	var dataList = $("#popup_form").children(".data");
	
	for(let ele of dataList){
		let prop = $(ele).prop("name");
		let val = $(ele).prop("value");
		passedData[prop] = val;
	}
	
	return new Promise((resolve, reject) => {
		$.post("new_user", passedData, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
	});
}

/**
 * @return {Promise} return value: {list: Array<Object>, totalPage: Number}
 */
function getList(page){
  var passedData = {
    page
  };
  
  //filters
  if(nameFilter.length)
  	passedData.name = nameFilter;
  if(birthFilter_from.length)
  	passedData.birthFrom = birthFilter_from;
  if(birthFilter_to.length)
  	passedData.birthTo = birthFilter_to;
  
  return new Promise((resolve, reject) => {
    $.post("user_page", passedData, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
  });
}

/**
 * 
 * @param id {String}
 * @return {Objcet} user data of the id
 */
function getUser(id){
	return new Promise((resolve, reject) => {
    $.post("get_user", {id}, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
  });
}











