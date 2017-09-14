
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
 * set the link of current page active, and hide buttons not required
 * @param {String} pageName 
 */
function setNavView(pageName){
	if(!pageName) return;

	if(pageName === "sign_in"){
		$("#nav_sign_out").prop("class", "hidden");
	}
	
	$(`#nav_${pageName}`).prop("class", "active");
}