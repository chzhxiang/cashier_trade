$(function(){ 
    var xTop;
    $(".boxcon").scroll(function() {
          xTop = $(".boxcon").scrollTop();
          xTop = xTop/130;
          if(xTop>0.7) {
            $(".home-header").addClass('headerdown');
          }else{
            $(".home-header").removeClass('headerdown');
          }
          if(xTop>1) {
            xTop = 1;
          }
          $(".header-bg").css("opacity",xTop);
    });
})

var homeSwiper = new Swiper('.swiper-container', {
    pagination: '.swiper-pagination',
    autoplay: 5000,//可选选项，自动滑动
    paginationClickable: true,
    autoplayDisableOnInteraction:false,//设置为false时，用户滑动幻灯片后不会停止自动播放
    loop: true
});

function openPage(){
    $.ajax({
        url : tradeBasePath+'/mobileapp/toBuild',
        type: 'POST',
        error: function(request) {
            $Y.tips('<div>操作失败</div>');  
         },
        success: function (data) {
           createPage(data,"正在建设中");
        }
    });
}

function addFamily(){
	$.ajax({
        url : tradeBasePath+'/mobileapp/toPatient',
        type: 'POST',
        error: function(request) {
        	$Y.tips('<div>操作失败</div>');  
         },
        success: function (data) {
           createPage(data,"添加家人档案");
        }
    });
}