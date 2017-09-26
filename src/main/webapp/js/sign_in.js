
///////////
// ready //
///////////

$(() => {
	///////////////
  // listeners //
  ///////////////
	
	$("#sign_in_form").on("click", "[type='submit']", (event) => {
		event.preventDefault();
		signIn();
	});
});

///////////////
// functions //
///////////////

function signIn(){
	var passedData = new FormData($("#sign_in_form")[0]);
	
	return signInAction(passedData)
		.then(data => {
			if(data.result){
				window.location.replace("user");
				return;
			}
			$("#sign_in_form").children(".err_msg").text("Incorrect account or password.");
		});
}

  ////////////////////
  // ajax functions //
  ////////////////////

/**
 * @param passedData {Object} formData
 * @return {Promise} {token: String (not required)}
 */
function signInAction(passedData){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "sign_in/action",
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