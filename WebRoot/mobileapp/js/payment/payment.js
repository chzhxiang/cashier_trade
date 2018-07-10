$(document).on("click",".archives-th .archives-list",function(){
	if(!$(this).hasClass('active')) {
		 $(this).addClass('active').siblings().removeClass('active');
	}
})