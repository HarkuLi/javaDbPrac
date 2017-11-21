/**
 * 
 */
var URLBase = URLBase || "/javaDbPrac";
var currentPage = 1;
var processing = false;
var filter = {
	name: "",
	state: ""
};
var statusMap = {};

initialization();

///////////
// ready //
///////////
$(() => {
	selectPage(currentPage);
	
  ///////////////
  // listeners //
  ///////////////
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
  
  $("#filter_search").on("click", (event) => {
  	event.preventDefault();
  	
  	if(processing) return;
  	if(!isFilterChange()) return;
  	
  	processing = true;
  	filterSearch()
  		.then(() => {
  			processing = false;
  		});
  });
  
  $("#new_btn").on("click", () => {
  	//set pop up window
  	$(".mask").css("display", "flex");
  	$(".mask").prop("id", "new_block");
  	$("#popup_form").children("[type='submit']").prop("id", "int_create");
  });
  
  $(".close_btn").on("click", function(){
  	closePopup();
  });
  
  $("#popup_form").on("click", "#int_create", function(event){
  	event.preventDefault();
  	if(processing) return;
  	
  	var checkFill = checkFillAll($("#popup_form"));
  	if(!checkFill.isFilled)
  		return alert("You have some fields not filled:\n" + checkFill.blankList.toString());
  	
  	processing = true;
  	newInterest()
    	.then(() => {
  			processing = false;
  		});
  });
  
  $("#data_table").on("click", ".edit", function(){
  	if(processing) return;
  	
  	processing = true;
  	var self = this;
  	$("body").css("cursor", "progress");
  	edit(self)
  		.then(() => {
    		$("body").css("cursor", "");
    		processing = false;
    	});
  });
  
  $("#popup_form").on("click", "#int_modify", function(event){
  	event.preventDefault();
  	if(processing) return;
  	
  	var checkFill = checkFillAll($("#popup_form"));
  	if(!checkFill.isFilled)
  		return alert("You have some fields not filled:\n" + checkFill.blankList.toString());
  	
  	processing = true;
  	var self = this;
  	save(self)
  		.then(() => {
  			processing = false;
  		});
  });
  
  $("#data_table").on("click", ".delete", function(){
  	if(processing) return;
  	
  	processing = true;
  	var self = this;
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
});

///////////////
// functions //
///////////////

function initialization(){
	recordStatusOption();
}

function recordStatusOption(){
	for(let option of $(".filter").find("[name='state']").children()){
		statusMap[$(option).prop("value")] = $(option).text();
	}
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
	var formData = new FormData($("#popup_form")[0]);
	var passedData = {};
	
	for(let entry of formData.entries()){
		passedData[entry[0]] = entry[1];
	}
	
	//update the change
	return doUpdate(passedData)
  	.then(data => {
  		return selectPage(currentPage);
  	})
  	.then(rst => {
  		if(!rst) return;
  		closePopup();
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
	//set pop up window
	$(".mask").prop("id", "edit_block");
	$("#popup_form").children("[type='submit']").prop("id", "int_modify");
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getInterest(id)
		.then(data => {
			const checkTypeList = ["state"];
			
			//set pop up form
				//set text type
			var inputList = $("#popup_form").find(".data");
			for(let ele of inputList){
				let propName = $(ele).prop("name");
				if(checkTypeList.indexOf(propName) !== -1) continue;
				$(ele).prop("value", data[propName]);
			}
				//set checked type
			for(let name of checkTypeList){
				let checkList = $("#popup_form").find("[name='"+ name +"']");
				for(let ele of checkList){
					if($(ele).prop("value") === data[name]){
						$(ele).prop("checked", true);
						break;
					}
				}
			}
			
			//show pop up window
			$(".mask").css("display", "flex");
		})
		.catch(error => {
			console.log(error);
		});
}

/**
 * 
 * @return {Promise}
 */
function newInterest(){
	var formData = new FormData($("#popup_form")[0]);
	var passedData = {};
	
	for(let entry of formData.entries()){
		passedData[entry[0]] = entry[1];
	}
	
	return doCreate(passedData)
		.then(data => {
			return selectPage(currentPage);
		})
		.then(rst => {
			if(!rst) return;
			closePopup();
		})
		.catch(error => {
			alert(error);
		});
}

/**
 * 
 * @param checkEleList {Array<Object>} all checked type(radio/checkbox) elements with the same name
 * @return {String} return null if no one checked
 */
function checkedVal(checkEleList){
	for(let ele of checkEleList){
		if($(ele).prop("checked") === true) return $(ele).prop("value");
	}
	return null;
}

/**
 * @param form {Object} jquery element
 * @return {isFilled: Boolean, blankList: Array<String>}
 */
function checkFillAll(form){
	const exceptList = ["id", "state"];
	
	var resultObj = {
			isFilled: true,
			blankList: []
	};
	
	//check state field
	if(!checkedVal($(form).find("[name='state']"))) {
		resultObj.blankList.push("state");
	}
	
	//check other fields
	var inputList = $(form).find(".data");
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) resultObj.blankList.push($(ele).prop("name"));
	}
	
	if(resultObj.blankList.length) resultObj.isFilled = false;
	
	return resultObj;
}

/**
 * @param form {Object} jquery element
 */
function clrFields(form){
	var inputList = $(form).find(".data");
	
	for(let ele of inputList){
		if($(ele).prop("name") === "state"){
			$(ele).prop("checked", false);
			continue;
		}
		$(ele).prop("value", "");
	}
}

function closePopup(){
	clrFields($("#popup_form"));
	$(".mask").prop("id", "");
	$(".mask").css("display", "");
	$("#popup_form").children("[type='submit']").prop("id", "");
}

/**
 * 
 * @return {Promise}
 */
function filterSearch(){
	//record new filters
	for(let name in filter){
		filter[name] = $(".filter").find(`[name='${name}']`).prop("value");
	}
	
	//go to page 1
	currentPage = 1;
	return selectPage(1);
}

/**
 * 
 * @return {Boolean}
 */
function isFilterChange(){
	for(let name in filter){
		let newVal = $(".filter").find(`[name='${name}']`).prop("value");
		if(newVal !== filter[name]) return true;
	}
	
	return false;
}

/**
 * go to the page
 * can be used for reload by select current page
 * @param page {Number}
 * @return {Promise} return true if switch to the page
 */
function selectPage(page){
	if(page < 1) return Promise.resolve(false);
  
	currentPage = page;
	$("body").css("cursor", "progress");
  
  return getList(page)
  	.then(data => {
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
		name: 15,
		state: 7
	};
	
	$("#data_table").css("display", "");
	
	//deal with existing row elements
	var idx = 0;
	var rowList = $("#data_table").find(".data_rows");
	for(let row of rowList){
		let data = dataList[idx];
		let inputList = $(row).find("input");
		
		//number of row elements > data
	  //hide and clean values of the redundant rows
		if(!data){
			for(let ele of inputList){
				$(ele).prop("value", "");
			}
			$(row).css("display", "none");
			++idx;
			continue;
		}
		
		for(let ele of inputList){
			let prop = $(ele).prop("name");
			if(prop === "state"){
				$(ele).prop("value", statusMap[data[prop]]);
				continue;
			}
			$(ele).prop("value", data[prop]);
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
			
			if(prop === "state"){
				input.prop("value", statusMap[data[prop]]);
			}
			else{
				input.prop("value", data[prop]);
			}
			
			input.prop("disabled", true);
			input.prop("size", sizeMap[prop]);
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
 * @return {Promise} id
 */
function doDel(id){
	return new Promise((resolve, reject) => {
		$.post(`${URLBase}/interest/del`, {id}, (data, status, xhr) => {
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
 * @param passedData {Object} {id: String, name: String, state: String}
 * @return {Promise}
 */
function doUpdate(passedData){
	return new Promise((resolve, reject) => {
		$.post(`${URLBase}/interest/update`, passedData, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to update: " + data.errMsg);
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
 * @return {Objcet} interest detail of the id
 */
function getInterest(id){
	return new Promise((resolve, reject) => {
    $.post(`${URLBase}/interest/get_one`, {id}, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get");
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
 * @param passedData {Object} {name: String, state: String}
 * @return {Promise}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.post(`${URLBase}/interest/new`, passedData, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 201) {
				reject("failed to create");
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
 * @return {Promise} return value: {list: Array<Object>, totalPage: Number}
 */
function getList(page){
  var passedData = {
    page
  };
  
  //filters
  for(let name in filter){
  	if(filter[name].length)
  		passedData[name] = filter[name];
  }
  
  return new Promise((resolve, reject) => {
    $.post(`${URLBase}/interest/get_page`, passedData, (data, status, xhr) => {
			if(status !== "success") {
				reject("post status: " + status);
				return;
			}
			if(xhr.status !== 200) {
				reject("failed to get list");
				return;
			}
      resolve(data);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
		});
  });
}











