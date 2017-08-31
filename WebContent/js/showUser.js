/**
 * 
 */
const route = "/javaDbPrac/";
var currentPage = 1;
var nameFilter = "";
var birthFilter_from = "";
var birthFilter_to = "";

///////////
// ready //
///////////
$(() => {
	//initialization
	selectPage(currentPage);
	renderInterestList();
	
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
  	
  	newUser();
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
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
  	var self = this;
  	save(self);
  });
});

///////////////
// functions //
///////////////

function renderInterestList(){
	getInterestList()
		.then(list => {
			$("#interest_box").empty();
			var ul = $("<ul></ul>");
			for(let ele of list){
				let li = $("<li></li>");
				let label = $("<label></label>");
				let input = $("<input></input>");
				input.prop("class", "data");
				input.prop("type", "checkbox");
				input.prop("name", "interest[]");
				input.prop("value", ele.id);
				label.append(input);
				label.append(ele.name);
				li.append(label);
				ul.append(li);
			}
			$("#interest_box").append(ul);
		});
}

function closePopup(){
	$("#current_photo").prop("src", "/javaDbPrac/user_photo");
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
	const acceptFileType = ["image/jpeg", "image/png", "image/gif"];
	var passedData = new FormData($("#popup_form")[0]);
	var photo = $("#photo").prop("files")[0];
	
	if(photo){
		if(acceptFileType.indexOf(photo.type)===-1)
			return alert("Unaccepted file type.");
		let type = photo.type.split("/")[1];
		passedData.append("photoType", type);
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
  		selectPage(currentPage);
  		closePopup();
  	});
}

/**
 * 
 * @param self {Object} copy of this
 * @return {Promise}
 */
function edit(self){
	//set pop up window
	$(".mask").prop("id", "edit_block");
	$("#popup_form").children(":submit").prop("id", "user_modify");
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getUser(id)
		.then(data => {
			//set current photo
			$("#current_photo").prop("src", route + "/user_photo?n=" + data.photoName);
			
			//set pop up form
				//set non-checked type data
			var txtData = $("#popup_form").children(".data");
			for(let ele of txtData){
				let propName = $(ele).prop("name");
				$(ele).prop("value", data[propName]);
			}
				//set interest
			var interestData = $("#interest_box").find("input");
			for(let ele of interestData){
				let interestId = $(ele).prop("value");
				if(data.interest.indexOf(interestId) >= 0){
					$(ele).prop("checked", true);
				}
			}
			
			//show pop up window
			$(".mask").css("display", "flex");
		});
}

/**
 * @param form {Object} jquery element
 */
function clrFields(form){
	var txtData = $(form).children(".data");
	txtData = $(txtData).filter("[type!='checkbox']");
	txtData = $(txtData).filter("[type!='radio']");
	
	$(txtData).prop("value", "");
	
	var checkData = $(form).find(":checkbox, :radio");
	$(checkData).prop("checked", false);
}

/**
 * 
 * @param checkEleList {Array<Object>} all checked type(radio/checkbox) elements with the same name
 * @return {Boolean} return false if no one checked
 */
function isChecked(checkEleList){
	for(let ele of checkEleList){
		if($(ele).prop("checked") === true) return true;
	}
	return false;
}

/**
 * @param form {Object} jquery element
 * @return {Boolean}
 */
function isFillAll(form){
	const exceptList = ["id", "photo", "photoName"];
	
	var inputList = $(form).children(".data");
	
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) return false;
	}
	
	//check whether interests selected
	if(!isChecked($("#interest_box").find("input"))) return false;
	
	return true;
}

function newUser(){
	const acceptFileType = ["image/jpeg", "image/png", "image/gif"];
	var passedData = new FormData($("#popup_form")[0]);
	var photo = $("#photo").prop("files")[0];
	
	if(photo){
		if(acceptFileType.indexOf(photo.type)===-1)
			return alert("Unaccepted file type.");
		let type = photo.type.split("/")[1];
		passedData.append("photoType", type);
	}
	
	return doCreate(passedData)
		.then(data => {
			if(data.errMsg) return alert(data.errMsg);
			selectPage(currentPage);
	  	closePopup();
		});
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
		birth: 10,
		photoName: 1
	};
	
	const hideList = ["id", "photoName"];
	const propList = ["id", "photoName", "photo", "name", "age", "birth"];
	
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
		
		//set input entries
		for(let ele of inputList){
			let prop = $(ele).prop("name");
			$(ele).prop("value", dataList[idx][prop]);
		}
		
	  //set photo entry
		let photo = $(row).find("img");
		$(photo).prop("src", route + "user_photo?n=" + dataList[idx].photoName);
		
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
		
		for(let prop of propList){
			rowEntry = $("<td></td>");
			
			//photo entry
			if(prop === "photo"){
				let img = $("<img>");
				img.prop("src", route + "user_photo?n=" + data.photoName);
				img.prop("height", "40")
				img.prop("width", "40")
				rowEntry.append(img);
				dataRow.append(rowEntry);
				continue;
			}
			
			//text entry
			if(hideList.indexOf(prop) >= 0) rowEntry.prop("class", "hidden_data");
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
 * @return {Promise} return array of interest
 */
function getInterestList(){
	return new Promise((resolve, reject) => {
		$.get("get_int_list", (data, status) => {
			if(status !== "success") return reject("get status: " + status);
			resolve(data.list);
		});
	});
}

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
 * @param passedData {Object} (formData)
 * 			{
 * 				id: String,
 * 				name: String,
 * 				age: String,
 * 				birth: String,
 * 				photo: Object,
 * 				photoType: String
 * 			}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doUpdate(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "update_user",
			type: "POST",
			data: passedData,
			processData: false,
			contentType: false,
			success: (data, status) => {
        if(status !== "success") return reject("post status: " + status);
        resolve(data);
      }
		});
	});
}

/**
 * 
 * @param passedData {Object}
 * 			{
 * 				name: String,
 * 				age: String,
 * 				birth: String,
 * 				photo: Object,
 * 				photoType: String
 * 			}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "new_user",
			type: "POST",
			data: passedData,
			processData: false,
			contentType: false,
			success: (data, status) => {
        if(status !== "success") return reject("post status: " + status);
        resolve(data);
      }
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











