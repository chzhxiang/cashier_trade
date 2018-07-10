//登录
function login(){
	var isValidate = true;
	
	var account = $("#account").val();
	var password = $("#password").val();
	
	if(account == null || account == ""){
		isValidate = false;
	}
	if(password == null || password == ""){
		isValidate = false;
	}
	
	if(isValidate){
		$("#loginForm").submit();
	}
}