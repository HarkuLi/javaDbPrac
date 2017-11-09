
var URLBase = URLBase || "/javaDbPrac";

///////////
// ready //
///////////

$(() => {
	var pageList = getPageList();
	var pageName = getPageName(pageList);	//match with navList
	
	setNavView(pageName);
});

///////////////
// functions //
///////////////

/**
 * return the page names on the navigation bar
 * @return {Array<String>}
 */
function getPageList(){
	var pageList = [];

	var pageLinkList = $("#navbar").find("li");
	for(let ele of pageLinkList){
		let eleId = $(ele).prop("id");
		
		let startIdx = eleId.indexOf("_") + 1;
		if(startIdx <= 0)	continue;
		
		let pageName = eleId.slice(startIdx);
		pageList.push(pageName);
	}

	return pageList;
}

/**
 * find the path name that is included in the page list and return
 * @param {Array<String>} pageList 
 * @return {String} page name or null
 */
function getPageName(pageList){
	var pathName = window.location.pathname;
	var pathList = pathName.split("/");

	for(let pageName of pageList){
		if(pathList.indexOf(pageName) >= 0){
			return pageName;
		}
	}

	return null;
}

/**
 * set the link of current page active, text, and hide buttons not required
 * @param {String} pageName
 * @return {Promise}
 */
function setNavView(pageName){
	
	return getUserInfo()
		.then(user => {
			if(user.name) {
	  		$("#nav_user_name").prop("class", "");
	  		$("#nav_sign_out").prop("class", "");
	  		
	  		var userStr = user.name + " (" + user.account + ")";
	  		$("#nav_user_name").children().text(userStr);
			}
			else {
				$("#nav_sign_up").prop("class", "");
	  		$("#nav_sign_in").prop("class", "");
			}
			
			$(`#nav_${pageName}`).prop("class", "active");
		})
		.catch(error => {
			alert(error);
		});
}

////////////////////
// ajax functions //
////////////////////

/**
 * 
 * @return {Promise} return the user correspond to the token
 */
function getUserInfo(){
	return new Promise((resolve, reject) => {
    $.post(`${URLBase}/user/get_by_token`, (data, status, xhr) => {
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


