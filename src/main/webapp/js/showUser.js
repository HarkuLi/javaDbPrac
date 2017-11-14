/**
 * 
 */
var URLBase = URLBase || "/javaDbPrac";
const acceptPhotoType = ["image/jpeg", "image/png", "image/gif"];
const checkTypeList = ["state", "interest[]"];
var currentPage = 1;
var processing = false;

var occMap = {other : "other"};	//{occId: name}
var interestMap = {}; //{interestId: name}
var filterForm = new FormData($(".filter").find("form")[0]);
//store jquery elements of opened forms
var openedFormList = [];
var editingUser;

initialization();

///////////
// ready //
///////////
$(() => {
  ///////////////
  // listeners //
  ///////////////
	
	//prevent the scroll of inputs in the table while dragging
	$("#data_table").on("mousedown", "input", (event) => {
		event.preventDefault();
	});
	
  $("#last_page").on("click", () => {
  	if(processing) return;
  	
  	processing = true;
    selectPage(currentPage-1)
    	.then(() => {
    		processing = false;
    	});
  });
  
  $("#next_page").on("click", () => {
  	if(processing) return;
  	
  	processing = true;
    selectPage(currentPage+1)
      .then(() => {
    		processing = false;
    	});
  });
  
  $(".filter").on("click", "#interest_expand", () => {
  	var iconClass = $("#interest_expand").prop("class");
  	if(iconClass.indexOf("chevron-down") >= 0) interestFilterMore();
  	else interestFilterLess();
  });
  
  $("#filter_search").on("click", (event) => {
  	event.preventDefault();
  	
  	//if(!isFilterChange()) return;
  	if(processing) return;
  	
  	processing = true;
  	filterSearch()
    	.then(() => {
    		processing = false;
    	});
  });
  
  $("#popup_form_new").on("click", "#user_create", function(event){
  	event.preventDefault();
  	
  	if(processing) return;
  	
  	var checkFill = checkFillAll($("#popup_form_new"));
  	if(!checkFill.isFilled)
  		return alert("You have some fields not filled:\n" + checkFill.blankList.toString());
  	
  	processing = true;
  	newUser()
    	.then(() => {
    		processing = false;
    	});
  });
  
  $("#new_btn").on("click", () => {
		if(processing) return;
		processing = true;
		
		loadNewUserForm()
  		.then(() => {
  			switchForm($("#popup_form_new"));
			})
			.catch(error => {
				alert(error);
			})
			.then(() => {
				processing = false;
			});
  });
  
  $(".close_btn").on("click", function(){
  	closeForm();
  });
  
  $("#data_table").on("click", ".edit", function(){
  	if(processing) return;
  	
  	var self = this;
  	processing = true;
		$("body").css("cursor", "progress");
		
		loadEditUserForm()
			.then(() => {
				return edit(self);
			})
  		.then(() => {
  			//show edit form
  			switchForm($("#popup_form_edit"));
			})
			.catch(error => {
				alert(error);
			})
			.then(() => {
				$("body").css("cursor", "");
    		processing = false;
			});
  });
  
  $("#data_table").on("click", ".delete", function(){
  	if(processing) return;
  	
  	var self = this;
  	processing = true;
  	delRow(self)
  		.then(rst => {
  			if(!rst) return;
  			//reload the page
  			return selectPage(currentPage);
  		})
  		.then(() => {
  			processing = false;
  		});
  });
  
  $("#popup_form_edit").on("click", "#user_modify", function(event){
  	event.preventDefault();
  	
  	if(processing) return;
  	
  	var checkFill = checkFillAll($("#popup_form_edit"));
  	if(!checkFill.isFilled)
  		return alert("You have some fields not filled:\n" + checkFill.blankList.toString());
  	
  	var self = this;
  	processing = true;
  	save(self)
    	.then(() => {
    		processing = false;
    	});
  });
  
  $("#popup_form_edit").on("click", "#change_password", (event) => {
		event.preventDefault();
		if(processing) return;
		processing = true;
		
		loadChangePasswordForm()
			.then(() => {
				editPw();
				switchForm($("#popup_form_chpw"));
			})
			.catch(error => {
				alert(error);
			})
			.then(() => {
				processing = false;
			});
  });
  
  $("#popup_form_chpw").on("click", "[type='submit']", () => {
  	event.preventDefault();
  	
  	if(processing) return;
  	
  	var checkFill = checkFillAll($("#popup_form_chpw"));
  	if(!checkFill.isFilled)
  		return alert("You have some fields not filled:\n" + checkFill.blankList.toString());
  	
  	processing = true;
  	changePw()
  		.then(rst => {
  			if(rst){
    			alert("Change password successfully");
    			closeForm();
  			}
  			processing = false;
  		});
  });
});

///////////////
// functions //
///////////////

/**
 * 
 * @return {Promise} return true if success
 */
function changePw(){
	//enable the element to pass
	$("#popup_form_chpw").find("[name='account']").prop("disabled", false);
	
	var passedData = new FormData($("#popup_form_chpw")[0]);
	return updatePassword(passedData)
		.then(() => {
  		return getUser(editingUser.id);
		})
  	.then(data => {
			//record the editing user
			editingUser = data;
			
			//update the account shown in the edit form of the user
			$("#popup_form_edit").find("[name='account']").prop("value", data.account);
  		
			return true;
		})
		.catch(error => {
			alert(error);
			return false;
		});
}

function editPw(){
	var inputList = $("#popup_form_chpw").find("[name]");
	
	//set the form
		//determine whether account input is disabled
	var accountEle = $("#popup_form_chpw").find("[name='account']");
	if(!editingUser.account) accountEle.prop("disabled", false);
	else accountEle.prop("disabled", true);
		//set values
	for(let ele of inputList){
		let prop = $(ele).prop("name");
		$(ele).prop("value", editingUser[prop]);
	}
}

/**
 * close the current form and show the last form
 * close the pop up window when there is no form in the opened form list
 */
function closeForm(){
	//close the current form
	var currentForm = openedFormList.pop();
	$(currentForm).css("display", "");
	clrFields($(currentForm));
	
	//close the pop up window if there is no form in the opened form list
	if(!openedFormList.length){
		closePopup();
		return;
	}
	
	//show the last form
	var lastForm = openedFormList[openedFormList.length-1];
	$(lastForm).css("display", "block");
}

/**
 * hide the original form and show the designated form
 * @param form {Object} jquery form element
 */
function switchForm(form){
	//show the pop up window
	if(!openedFormList.length) $(".mask").css("display", "flex");
	
	//hide the original form
	var currentForm = openedFormList[openedFormList.length-1];
	if(currentForm) $(currentForm).css("display", "");
	
	//show the designated form
	openedFormList.push(form);
	$(form).css("display", "block");
}

function interestFilterLess(){
	//render the interest filter description 
	var tmpFilterForm = new FormData($(".filter").find("form")[0]);
	var interestList = tmpFilterForm.getAll("interest[]");
	var interestDes = "";
	for(let i=0; i<interestList.length; ++i){
		interestDes += interestMap[interestList[i]];
		if(i != interestList.length-1)	interestDes += ", ";
	}
	$(".interest_filter_des").text(interestDes);
	$(".interest_filter_des").css("display", "");
	
	//wrap the interest filter block
	$("#interest_expand").prop("class", "glyphicon glyphicon-chevron-down icon_btn");
	$(".filter").find(".interest_box").css("display", "");
}

function interestFilterMore(){
	//hide the interest filter description 
	$(".interest_filter_des").css("display", "none");
	$(".interest_filter_des").text("");
	
	//expand the interest filter block
	$("#interest_expand").prop("class", "glyphicon glyphicon-chevron-up icon_btn");
	$(".filter").find(".interest_box").css("display", "block");
}

/**
 * 
 * @return {Promise}
 */
function initialization(){	
	var promiseList = [];
	promiseList.push(renderOccList());
	promiseList.push(renderInterestList());
	
	return Promise.all(promiseList)
		.then(() => {
			//should initialized after renderOccList() and renderInterestList()
			//because it uses occMap and interestMap
			return selectPage(currentPage);
		});
}

function closePopup(){
	$("#current_photo").prop("src", URLBase + "/user/photo");
	$(".popup_window").find("form").css("display", "");
	$(".mask").css("display", "");
	editingUser = null;
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
		})
		.catch((error) => {
			console.log(error);
			return false;
		});
}

/**
 * 
 * @param self {Object} copy of this
 * @return {Promise}
 */
function save(self){
	var passedData = new FormData($("#popup_form_edit")[0]);
	var photo = $("#popup_form_edit").children("[name='photo']").prop("files")[0];
	
	if(photo){
		if(acceptPhotoType.indexOf(photo.type)===-1)
			return alert("Unaccepted file type.");
		let type = photo.type.split("/")[1];
		passedData.append("photoType", type);
	}
	
	//update the change
	return doUpdate(passedData)
  	.then(() => {
  		return selectPage(currentPage);
  	})
  	.then(() => {
  		closeForm();
		})
		.catch(error => {
			alert(error);
		});
}

/**
 * 
 * @param self {Object} copy of this
 * @return {Promise}
 */
function edit(self){
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getUser(id)
		.then(data => {
			//record the editing user
			editingUser = data;
			
			//set current photo
			var photoURL = URLBase + "/user/photo?n=" + (data.photoName || "");
			$("#current_photo").prop("src", photoURL);
			
			//set pop up form
				//set non-checked type data
			var txtData = $("#popup_form_edit").find(".data");
			for(let ele of txtData){
				let propName = $(ele).prop("name");
				if(checkTypeList.indexOf(propName) !== -1) continue;
				$(ele).prop("value", data[propName]);
			}
				//set state
			var state = data.state ? "1" : "0";
			let stateList = $("#popup_form_edit").find("[name='state']");
			for(let ele of stateList){
				if($(ele).prop("value") === state){
					$(ele).prop("checked", true);
					break;
				}
			}
				//set interest
			if(data.interest){
				var interestData = $("#popup_form_edit").find(".interest_box").find("input");
				for(let ele of interestData){
					let interestId = $(ele).prop("value");
					if(data.interest.indexOf(interestId) >= 0){
						$(ele).prop("checked", true);
					}
				}
			}
		});
}

/**
 * @param formList {Object} jquery element
 */
function clrFields(formList){
	for(let form of formList){
		
		let inputList = $(form).find("[name]");
		for(let ele of inputList){
			let propName = $(ele).prop("name");
			if(checkTypeList.indexOf(propName) !== -1) continue;
			$(ele).prop("value", "");
		}
		
		$(form)[0].reset();
	}
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
 * @return {isFilled: Boolean, blankList: Array<String>}
 */
function checkFillAll(form){
	const exceptList = ["id", "photo", "photoName", "interest[]", "state"];
	
	var resultObj = {
			isFilled: true,
			blankList: []
	};
	
	//check state field
	if($(form).find("[name='state']").length){
		if(!isChecked($(form).find("[name='state']"))) resultObj.blankList.push("state");
	}
	
	//check other fields
	var inputList = $(form).find("[name]");
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) resultObj.blankList.push($(ele).prop("name"));
	}
	
	if(resultObj.blankList.length) resultObj.isFilled = false;
	
	return resultObj;
}

/**
 * 
 * @return {Promise}
 */
function newUser(){
	var passedData = new FormData($("#popup_form_new")[0]);
	var photo = $("#popup_form_new").find("[name='photo']").prop("files")[0];
	
	if(photo){
		if(acceptPhotoType.indexOf(photo.type)===-1){
			alert("Unaccepted file type.");
			return Promise.resolve(false);
		}
		let type = photo.type.split("/")[1];
		passedData.append("photoType", type);
	}
	
	return doCreate(passedData)
		.then(data => {
			return selectPage(currentPage);
		})
		.then(rst => {
			if(!rst) return;
			closeForm();
		})
		.catch(error => {
			alert(error);
		});
}

/**
 * 
 * @return {Promise}
 */
function filterSearch(){
	var dateFormatMsg = "wrong date format, correct format: YYYY-MM-DD.";
	var birthFrom = $(".filter").find("[name='birthFrom']").prop("value");
	var birthTo = $(".filter").find("[name='birthTo']").prop("value");
	
	//check input format
	var reg = /^\d{4}-\d{2}-\d{2}$/;
	if(birthFrom.length && !reg.test(birthFrom))
		return alert(dateFormatMsg);
  if(birthTo.length && !reg.test(birthTo))
  	return alert(dateFormatMsg);
		
	//record new filters
  filterForm = new FormData($(".filter").children("form")[0]);
	
	//go to page 1
	currentPage = 1;
	return selectPage(1);
}

/**
 * 
 * @return {Boolean}
 */
function isFilterChange(){
	var filterList = $(".filter").find("[name]");
	
	for(let ele of filterList){
		let filterName = $(ele).prop("name");
		if($(ele).prop("value") !== filter[filterName]) return true;
	}

	return false;
}

/**
 * 
 * @param data
 * @return {object}
 */
function handlePageData(data){
	if(!data.list) return data;
	
	//deep copy
	var rstData = JSON.parse(JSON.stringify(data));
	
	//replace occupation id as name
	for(let user of rstData.list){
		if(!user.occupation)	user.occupation = "--";
		else user.occupation = occMap[user.occupation];
	}
	
	//replace interest list as string
	for(let user of rstData.list){
		if(!user.interest){
			user.interest = "--";
			continue;
		}
		
		var interestStr = "";
		for(let i=0; i<user.interest.length; ++i){
			interestStr += interestMap[user.interest[i]];
			if(i != user.interest.length-1)	interestStr += ", ";
		}
		user.interest = interestStr;
	}
	
	return rstData;
}

/**
 * go to the page
 * can be used for reload by select current page
 * @param page {Number}
 * @return {Promise} return true if switching to the page successfully
 */
function selectPage(page){
	if(page < 1) return Promise.resolve(false);
  
	currentPage = page;
	$("body").css("cursor", "progress");
  
  return getList(page)
  	.then(data => {
  		data = handlePageData(data);
  		renderData(data.list);
  		pageNumDisp(data.totalPage);
  		$("body").css("cursor", "");
  		return true;
		})
		.catch(error => {
			console.log(error);
			return false;
		});
}

/**
 * 
 * @param totalPage {Number} total page of data
 */
function pageNumDisp(totalPage){
	if(currentPage > totalPage)	currentPage = totalPage;
	$(".paging_row > input").val(currentPage);
}

  //////////////////////
  // render functions //
  //////////////////////

/**
 * 
 * @return {Promise}
 */
function renderOccList(){
	var defaultOcc = {
		"--" : "",
		"other" : "other"
	};
	
	return getOccList()
		.then(list => {
			$(".occ_list").empty();
			
			for(let prop in defaultOcc){
				let option = $("<option></option>");
				option.prop("value", defaultOcc[prop]);
				option.append(prop);
				$(".occ_list").append(option);
			}
			
			for(let ele of list){
				option = $("<option></option>");
				option.prop("value", ele.id);
				option.append(ele.name);
				$(".occ_list").append(option);
				
				//record occupations
				occMap[ele.id] = ele.name;
			}
		})
		.catch(error => {
			console.log(error);
		});
}

/**
 * 
 * @return {Promise}
 */
function renderInterestList(){
	return getInterestList()
		.then(list => {
			$(".interest_box").empty();
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
				
				//record interests
				interestMap[ele.id] = ele.name;
			}
			$(".interest_box").append(ul);
		})
		.catch(error => {
			console.log(error);
		});
}

/**
 * change the values of existing row elements
 * hide and clean the values of the redundant rows
 * add new row elements if existing row elements are not enough
 * @param dataList {Array<Object>}
 */
function renderData(dataList){
	if(!dataList || !dataList.length){
		$("#data_table").css("display", "none");
		return;
	}
	
	const sizeMap = {
		id: 3,
		account: 10,
		name: 15,
		age: 3,
		birth: 10,
		photoName: 1,
		occupation: 10,
		interest: 10,
		state: 4
	};
	
	const hideList = ["id", "photoName"];
	const propList = ["id", "photoName", "photo", "account",
										"name", "age", "birth", "occupation",
										"interest", "state"];
	
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
				$(ele).prop("title", "");
			}
			$(row).css("display", "none");
			++idx;
			continue;
		}
		
		//set input entries
		for(let ele of inputList){
			let prop = $(ele).prop("name");
			if(prop === "state"){
				dataList[idx][prop] ? $(ele).prop("value", "enable") : $(ele).prop("value", "disable");
				continue;
			}
			$(ele).prop("value", dataList[idx][prop] || "--");
			if(prop === "interest") $(ele).prop("title", dataList[idx][prop]);
		}
		
	  //set photo entry
		let photo = $(row).find("img");
		let photoURL = URLBase + "/user/photo?n=" + (dataList[idx].photoName || "");
		$(photo).prop("src", photoURL);
		
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
				let photoURL = URLBase + "/user/photo?n=" + (data.photoName || "");
				img.prop("src", photoURL);
				img.prop("height", "40")
				img.prop("width", "40")
				rowEntry.append(img);
				dataRow.append(rowEntry);
				continue;
			}
			
			//text entry
			if(hideList.indexOf(prop) >= 0) rowEntry.prop("class", "hidden");
			if(prop === "id")	rowEntry.prop("class", "id");
			input = $("<input>");
			input.prop("name", prop);
			if(prop === "state"){
				data[prop] ? input.prop("value", "enable") : input.prop("value", "disable");
			}
			else{
				input.prop("value", data[prop] || "--");
			}
			input.prop("disabled", true);
			input.prop("size", sizeMap[prop]);
			if(prop === "interest") input.prop("title", data[prop]);
			rowEntry.append(input);
			dataRow.append(rowEntry);
		}
		
		rowEntry = $("<td></td>");
		btn = $("<button></button>");
		btn.prop("class", "btn btn-info btn-sm edit");
		btn.text("edit");
		rowEntry.append(btn);
		dataRow.append(rowEntry);
		
		rowEntry = $("<td></td>");
		btn = $("<button></button>");
		btn.prop("class", "btn btn-danger btn-sm delete");
		btn.text("delete");
		rowEntry.append(btn);
		dataRow.append(rowEntry);
		
		$("#data_table").append(dataRow);
	}
}

  ////////////////////
  // ajax functions //
  ////////////////////

/**
 * 
 * @return {Promise} return a list of occupations
 */
function getOccList(){
	return new Promise((resolve, reject) => {
		$.get(`${URLBase}/public/get_occ_list`, (data, status, xhr) => {
			if(status !== "success") {
				reject("get status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get occupation list: " + data.errMsg);
				return;
			}
			resolve(data.list);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
		});
	});
}

/**
 * 
 * @return {Promise} return an list of interests
 */
function getInterestList(){
	return new Promise((resolve, reject) => {
		$.get(`${URLBase}/public/get_interest_list`, (data, status, xhr) => {
			if(status !== "success") {
				reject("get status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get interest list: " + data.errMsg);
				return;
			}
			resolve(data.list);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
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
		$.post(`${URLBase}/user/del`, {id}, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to delete: " + data.errMsg);
				return;
			}
      resolve(data);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
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
 * @return {Promise}
 */
function doUpdate(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `${URLBase}/user/update`,
			type: "POST",
			data: passedData,
			processData: false,
			contentType: false,
			success: (data, status, xhr) => {
        if(status !== "success") {
					reject("post status: " + status);
					return;
				}
				if(xhr.status !== 200) {
					reject("failed to update: " + data.errMsg);
					return;
				}
        resolve(data);
			},
			error: (res) => {
				reject(res.responseJSON.errMsg);
			}
		})
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
 * @return {Promise}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `${URLBase}/user/new`,
			type: "POST",
			data: passedData,
			processData: false,
			contentType: false,
			success: (data, status, xhr) => {
        if(status !== "success") {
					reject("post status: " + status);
					return;
				}
				if(xhr.status !== 201) {
					reject("failed to create: " + data.errMsg);
					return;
				}
        resolve(data);
			},
			error: (res) => {
				reject(res.responseJSON.errMsg);
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
  for(let pair of filterForm.entries()){
  	let key = pair[0];
  	let val = filterForm.getAll(pair[0]);
  	
  	if(key === "interest[]"){
  		passedData[key] = val;
  	}
  	else{
  		if(!val[0].length) continue;
  		passedData[key] = val[0];
  	}	
  }
  
  return new Promise((resolve, reject) => {
    $.post(`${URLBase}/user/get_page`, passedData, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get list: " + data.errMsg);
				return;
			}
      resolve(data);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
		});
  });
}

/**
 * 
 * @param id {String}
 * @return {Promise} user data of the id
 */
function getUser(id){
	return new Promise((resolve, reject) => {
    $.post(`${URLBase}/user/get_one`, {id}, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get user: " + data.errMsg);
				return;
			}
      resolve(data);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
		});
  });
}

/**
 * 
 * @param passedData {Object}
 * 			{
 * 				id: String,
 * 				account: String,
 *				password: String,
 *				passwordCheck: String
 * 			}
 * @return {Promise}
 */
function updatePassword(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `${URLBase}/user/change_password`,
			type: "POST",
			data: passedData,
			processData: false,
			contentType: false,
			success: (data, status, xhr) => {
				if(status !== "success") {
					reject("post status: " + status);
					return;
				}
				if(xhr.status !== 200) {
					reject("failed to update password: " + data.errMsg);
					return;
				}
        resolve(data);
			},
			error: (res) => {
				reject(res.responseJSON.errMsg);
			}
		});
	});
}

/**
 * 
 * @return {Promise}
 */
function loadNewUserForm(){
	return new Promise((resolve, reject) => {
		//assume there are input elements in the form
		//therefore, we can check whether the form has been loaded by checking the number of input elements
		if($("#popup_form_new").find("input").length) {
			resolve(false);
			return;
		}

		$("#popup_form_new").load(`${URLBase}/user/new_form`, (res, status, xhr) => {
			if(status !== "success") {
  			reject("failed to load the content.");
  			return;
  		}
  		if(xhr.status !== 200) {
  			reject("failed to load the content: " + res.errMsg);
  			return;
  		}
  		resolve(true);
		});
	})
	.then(isLoad => {
		if(isLoad){
			//load the occupation and interest lists
			return initialization();
		}
	});
}

/**
 * 
 * @return {Promise}
 */
function loadEditUserForm(){
	return new Promise((resolve, reject) => {
		//assume there are input elements in the form
		//therefore, we can check whether the form has been loaded by checking the number of input elements
		if($("#popup_form_edit").find("input").length) {
			resolve(false);
			return;
		}

		$("#popup_form_edit").load(`${URLBase}/user/edit_form`, (res, status, xhr) => {
			if(status !== "success") {
  			reject("failed to load the content.");
  			return;
  		}
  		if(xhr.status !== 200) {
  			reject("failed to load the content: " + res.errMsg);
  			return;
  		}
  		resolve(true);
		});
	})
	.then(isLoad => {
		if(isLoad){
			//load the occupation and interest lists
			return initialization();
		}
	});
}

/**
 * 
 * @return {Promise}
 */
function loadChangePasswordForm(){
	return new Promise((resolve, reject) => {
		//assume there are input elements in the form
		//therefore, we can check whether the form has been loaded by checking the number of input elements
		if($("#popup_form_chpw").find("input").length) {
			resolve(false);
			return;
		}

		$("#popup_form_chpw").load(`${URLBase}/user/change_password_form`, (res, status, xhr) => {
			if(status !== "success") {
  			reject("failed to load the content.");
  			return;
  		}
  		if(xhr.status !== 200) {
  			reject("failed to load the content: " + res.errMsg);
  			return;
  		}
  		resolve(true);
		});
	});
}








