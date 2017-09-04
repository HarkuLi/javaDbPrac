/**
 * 
 */
var currentPage = 1;
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
    selectPage(currentPage-1);
  });
  
  $("#next_page").on("click", () => {
    selectPage(currentPage+1);
  });
  
  $("#filter_search").on("click", () => {
  	if(!isFilterChange()) return;
  	filterSearch();
  });
  
  $("#new_btn").on("click", () => {
  	//set pop up window
  	$(".mask").css("display", "flex");
  	$(".mask").prop("id", "new_block");
  	$("#popup_form").children(":submit").prop("id", "int_create");
  });
  
  $(".close_btn").on("click", function(){
  	closePopup();
  });
  
  $("#popup_form").on("click", "#int_create", function(event){
  	event.preventDefault();
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
  	newInterest();
  });
  
  $("#data_table").on("click", ".edit", function(){
  	var self = this;
  	$("body").css("cursor", "progress");
  	edit(self)
  		.then(() => {
    		$("body").css("cursor", "");
    	});
  });
  
  $("#popup_form").on("click", "#int_modify", function(event){
  	event.preventDefault();
  	
  	if(!isFillAll($("#popup_form"))) return alert("You have some fields not filled.");
  	
  	var self = this;
  	save(self);
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
});

///////////////
// functions //
///////////////

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
	var formData = new FormData($("#popup_form")[0]);
	var passedData = {};
	
	for(let entry of formData.entries()){
		passedData[entry[0]] = entry[1];
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
	$("#popup_form").children(":submit").prop("id", "int_modify");
	
	var dataRow = $(self).parent().parent();
	var id = $(dataRow).find(".id").children().prop("value");
	
	return getInterest(id)
		.then(data => {
			const checkTypeList = ["state"];
			
			//set pop up form
				//set text type
			var inputList = $("#popup_form").children(".data");
			for(let ele of inputList){
				let propName = $(ele).prop("name");
				if(checkTypeList.indexOf(propName) !== -1) continue;
				$(ele).prop("value", data[propName]);
			}
				//set checked type
			for(let name of checkTypeList){
				let val = data[name] ? "1" : "0";
				let checkList = $("#popup_form").children("[name='"+ name +"']");
				for(let ele of checkList){
					if($(ele).prop("value") === val){
						$(ele).prop("checked", true);
						break;
					}
				}
			}
			
			//show pop up window
			$(".mask").css("display", "flex");
		});
}

function newInterest(){
	var formData = new FormData($("#popup_form")[0]);
	var passedData = {};
	
	for(let entry of formData.entries()){
		passedData[entry[0]] = entry[1];
	}
	
	return doCreate(passedData)
		.then(data => {
			if(data.errMsg) return alert(data.errMsg);
			selectPage(currentPage);
	  	closePopup();
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
 * @return {Boolean}
 */
function isFillAll(form){
	const exceptList = ["id", "state"];
	
	var inputList = $(form).children(".data");
	
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) return false;
	}
	
	//check state field
	if(!checkedVal($("#popup_form").children("[name='state']"))) return false;
	
	return true;
}

/**
 * @param form {Object} jquery element
 */
function clrFields(form){
	var inputList = $(form).children(".data");
	
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
	$("#popup_form").children(":submit").prop("id", "");
}

function filterSearch(){
	//record new filters
	for(let name in filter){
		filter[name] = $(".filter").find(`[name='${name}']`).prop("value");
	}
	
	//go to page 1
	currentPage = 1;
	selectPage(1);
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
		$.post("interest/del", {id}, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
	});
}

/**
 * 
 * @param passedData {Object} {id: String, name: String, state: String}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doUpdate(passedData){
	return new Promise((resolve, reject) => {
		$.post("interest/update", passedData, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
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
    $.post("interest/get_one", {id}, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
  });
}

/**
 * 
 * @param passedData {Object} {name: String, state: String}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.post("interest/new", passedData, (data, status) => {
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
  for(let name in filter){
  	if(filter[name].length)
  		passedData[name] = filter[name];
  }
  
  return new Promise((resolve, reject) => {
    $.post("interest/get_page", passedData, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
  });
}











