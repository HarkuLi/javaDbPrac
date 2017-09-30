/**
 * 
 */
var URLBase = URLBase || "/javaDbPrac";
const acceptPhotoType = ["image/jpeg", "image/png", "image/gif"];
const checkTypeList = ["state", "interest[]"];
var processing = false;

var occMap = {other : "other"};	//{occId: name}
var interestMap = {}; //{interestId: name}

initialization();

///////////
// ready //
///////////
$(() => {
  ///////////////
  // listeners //
  ///////////////
	
	$("#user_create").on("click", function(event){
  	event.preventDefault();
  	
  	if(processing) return;
  	if(!isFillAll($("#sign_up_form"))) return alert("You have some fields not filled.");
  	
  	processing = true;
  	newUser()
  		.then(rst => {
  			if(rst){
					alert("Create account successfully. Now, use your new account to sign in.");
					window.location.replace(`${URLBase}/sign_in/page`);
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
 * @return {Promise}
 */
function initialization(){	
	$("#sign_up_form").find("[name='state']").parentsUntil("form").remove();
	
	var promiseList = [];
	promiseList.push(renderOccList());
	promiseList.push(renderInterestList());
	
	return Promise.all(promiseList);
}

/**
 * @param form {Object} jquery element
 * @return {Boolean}
 */
function isFillAll(form){
	var exceptList = ["id", "photo", "photoName"];
	exceptList = exceptList.concat(checkTypeList);
	
	var inputList = $(form).find("[name]");
	
	for(let ele of inputList){
		if(exceptList.indexOf($(ele).prop("name")) !== -1) continue;
		if(!$(ele).prop("value").length) return false;
	}
	
	return true;
}

/**
 * 
 * @return {Promise}
 */
function newUser(){
	var passedData = new FormData($("#sign_up_form")[0]);
	var photo = $("#sign_up_form").find("[name='photo']").prop("files")[0];
	
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
			if(data.errMsg){
				alert(data.errMsg);
				return false;
			}
			return true;
		});
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
		});
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
		$.get(`${URLBase}/public/get_occ_list`, (data, status) => {
			if(status !== "success") return reject("get status: " + status);
			resolve(data.list);
		});
	});
}

/**
 * 
 * @return {Promise} return an list of interests
 */
function getInterestList(){
	return new Promise((resolve, reject) => {
		$.get(`${URLBase}/public/get_interest_list`, (data, status) => {
			if(status !== "success") return reject("get status: " + status);
			resolve(data.list);
		});
	});
}

/**
 * 
 * @param passedData {Object}
 * 			{
 * 				state: String,
 * 				account: String,
 * 				password: String,
 * 				passwordCheck: String,
 * 				name: String,
 * 				age: String,
 * 				birth: String,
 * 				photo: Object,
 * 				photoType: String,
 * 				occupation: String,
 * 				interest[]: Array<String>
 * 			}
 * @return {Promise} if error, return: {errMsg: String}
 */
function doCreate(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `${URLBase}/sign_up/action`,
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
