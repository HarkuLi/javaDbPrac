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
			.then(() => {
				location.reload();
  		})
  		.catch(error => {
  			alert(error);
  		});
	})
});

///////////////
// functions //
///////////////

function initialization(){
	selectCurrentLanguage()
		.then(() => {
			return loadi18n();
		});
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
  	})
  	.catch(error => {
  		console.log(error);
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
			success: (data, status, xhr) => {
        if(status !== "success") {
        	reject("post status: " + status);
        	return;
        }
        if(xhr.status !== 200) {
        	reject("failed to set language: " + data.errMsg);
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
function getCurrentLanguage(){
	return new Promise((resolve, reject) => {
		$.get(`${URLBase}/public/get_current_language`, (data, status, xhr) => {
			if(status !== "success") {
      	reject("post status: " + status);
      	return;
      }
      if(xhr.status !== 200) {
      	reject("failed to get language: " + data.errMsg);
				return;
      }
			resolve(data);
		})
		.fail(res => {
			reject(res.responseJSON.errMsg);
		});
	});
}

function loadi18n(){
	return new Promise((resolve, reject) => {
		jQuery.i18n.properties({
	    name:'messages', 
	    path:'resources/', 
	    mode:'map',
	    language: 'zh_TW',
	    async: true,
	    callback: function() {
	    	resolve();
	    }
		});
	});
}




