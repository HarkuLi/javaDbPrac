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
  	$("#popup_form").children("[type='submit']").prop("id", "occ_create");
  });
  
  $(".close_btn").on("click", function(){
  	closePopup();
  });
  
  $("#popup_form").on("click", "#occ_create", function(event){
  	event.preventDefault();
  	if(processing) return;
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
  	processing = true;
  	newOcc()
  		.then(() => {
  			processing = false;
  		});
  });
  
  $("#data_table").on("click", ".edit", function(){
  	if(processing) return;
  	
  	var self = this;
  	$("body").css("cursor", "progress");
  	
  	processing = true;
  	edit(self)
  		.then(() => {
    		$("body").css("cursor", "");
    		processing = false;
    	});
  });
  
  $("#popup_form").on("click", "#occ_modify", function(event){
  	event.preventDefault();
  	if(processing) return;
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
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

function closePopup(){
	clrFields($("#popup_form"));
	$(".mask").prop("id", "");
	$(".mask").css("display", "");
	$("#popup_form").children("[type='submit']").prop("id", "");
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
	$("#popup_form").children("[type='submit']").prop("id", "occ_modify");
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getOcc(id)
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
				let val = data[name] ? "1" : "0";
				let checkList = $("#popup_form").find("[name='"+ name +"']");
				for(let ele of checkList){
					if($(ele).prop("value") === val){
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

/**
 * @param form {Object} jquery element
 * @return {Boolean}
 */
function isFillAll(form){
	const exceptList = ["id", "state"];
	
	var inputList = $(form).find(".data");
	
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) return false;
	}
	
	//check state field
	if(!checkedVal($("#popup_form").find("[name='state']"))) return false;
	
	return true;
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
 * 
 * @return {Promise}
 */
function newOcc(){
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
 * @return {Promise}
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
			if(prop === "state"){
				dataList[idx][prop] ? $(ele).prop("value", "enable") : $(ele).prop("value", "disable");
				continue;
			}
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
			
			if(prop === "state"){
				data[prop] ? input.prop("value", "enable") : input.prop("value", "disable");
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
		$.post(`${URLBase}/occ/del`, {id}, (data, status, xhr) => {
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
		$.post(`${URLBase}/occ/update`, passedData, (data, status, xhr) => {
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
 * @param passedData {Object} {name: String, state: String}
 * @return {Promise}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.post(`${URLBase}/occ/new`, passedData, (data, status, xhr) => {
      if(status !== "success") {
				reject("post status: " + status);
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
    $.post(`${URLBase}/occ/get_page`, passedData, (data, status, xhr) => {
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

/**
 * 
 * @param id {String}
 * @return {Objcet} occupation data of the id
 */
function getOcc(id){
	return new Promise((resolve, reject) => {
    $.post(`${URLBase}/occ/get_one`, {id}, (data, status, xhr) => {
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











