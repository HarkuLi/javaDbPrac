
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
		let pageName = eleId.slice(eleId.indexOf("_")+1);
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
 * @return {Promise} return true if success
 */
function setNavView(pageName){
	if(pageName){
  	$(`#nav_${pageName}`).prop("class", "active");
  	
  	if(pageName === "sign_in"){
  		$("#nav_sign_out").prop("class", "hidden");
  		$("#nav_sign_up").prop("class", "");
  		return Promise.resolve(true);
  	}
  	if(pageName === "sign_up"){
  		$("#nav_sign_out").prop("class", "hidden");
  		$("#nav_sign_in").prop("class", "");
  		return Promise.resolve(true);
  	}
	}
	
	return getUserInfo()
		.then(user => {
			if(!user.name) return false;
			var userStr = user.name + " (" + user.account + ")";
			$("#nav_user_name").children().text(userStr);
			return true;
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
    $.post(`${URLBase}/user/get_by_token`, (data, status) => {
      if(status !== "success") return reject("post status: " + status);
      resolve(data);
    });
  });
}


