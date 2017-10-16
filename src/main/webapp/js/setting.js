/**
 * 
 */
var URLBase = URLBase || "/javaDbPrac";

initialization();

///////////
// ready //
///////////
$(() => {
  ///////////////
  // listeners //
  ///////////////
	
	$("#save_setting").on("click", (event) => {
		event.preventDefault();
		
		var passedData = new FormData($("#setting_form")[0]);
		
		setLanguage(passedData)
			.then((data) => {
				if(data.errMsg){
					alert(data.errMsg);
					return;
				}
				location.reload();
  		});
	})
});

///////////////
// functions //
///////////////

function initialization(){
	selectCurrentLanguage();
}

/**
 * 
 * @return {Promise}
 */
function selectCurrentLanguage(){
	return getCurrentLanguage()
  	.then((data) => {
  		var language = data.language;
  		$("option").filter(`[value=${language}]`).prop("selected", true);
  	});
}

  //////////////////////
  // render functions //
  //////////////////////


  ////////////////////
  // ajax functions //
  ////////////////////

/**
 * 
 * @return {Promise}
 */
function setLanguage(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: `${URLBase}/public/set_language`,
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
 * @return {Promise}
 */
function getCurrentLanguage(){
	return new Promise((resolve, reject) => {
		$.get(`${URLBase}/public/get_current_language`, (data, status) => {
			if(status !== "success") return reject("get status: " + status);
			resolve(data);
		});
	});
}






