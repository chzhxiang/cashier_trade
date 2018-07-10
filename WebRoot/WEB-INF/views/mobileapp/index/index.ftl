<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no" />
<title>index</title>
<link href="${tradeBasePath}/mobileapp/css/style.css?v=${css_version}" type="text/css" rel="stylesheet" />
<script src="${tradeBasePath}/mobileapp/js/jquery-1.9.1.min.js?v=${js_version}"></script>
<script src="${tradeBasePath}/mobileapp/js/fastclick.js?v=${js_version}"></script>
<script src="${tradeBasePath}/mobileapp/js/common.js?v=${js_version}"></script>
<script>
var tradeBasePath="${tradeBasePath}";
if ('addEventListener' in document) {  
    document.addEventListener('DOMContentLoaded', function() {  
        FastClick.attach(document.body);  
    }, false);  
}  
</script>
</head>
<body>
<div class="container">
    <div class="box trans">
        <div class="header home-header">
            <div class="top-search home-search">
                <input type="search" class="search-input home-search-input" name="" placeholder="输入医院/医生搜索" value="">
                <i class="icon icon-search"></i>
                <i class="icon icon-code"></i>
            </div>
            <div class="header-bg"></div>
        </div>
        <div class="boxcon">
            <div class="banner swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide"><img src="${tradeBasePath}/mobileapp/images/banner01.jpg"></div>
                    <div class="swiper-slide"><img src="${tradeBasePath}/mobileapp/images/banner01.jpg"></div>
                    <div class="swiper-slide"><img src="${tradeBasePath}/mobileapp/images/banner01.jpg"></div>
                </div> 
                <div class="swiper-pagination"></div>
            </div>
            <div class="features row mt8">
                <div class="col rightline push" onclick="openPage();">
                    <i class="icon icon-register"></i>
                    <h5>预约挂号</h5>
                    <p>三甲公立医院</p>
                </div>
                <div class="col push">
                    <i class="icon icon-payment"></i>
                    <h5>缴费取药<span></span></h5>
                    <p>线上缴费便捷取药</p>
                </div>
            </div>
            <h2 class="h2-tit mt8">家庭档案</h2>
            <div class="archives row">
                <div class="archives-list col push">
                    <p class="pic"><img src="${tradeBasePath}/mobileapp/images/women.png"></p>
                    <p class="name">*晓明</p>
                </div>
                <div class="archives-list col push">
                    <p class="pic"><img src="${tradeBasePath}/mobileapp/images/children.png"></p>
                    <p class="name">*明</p>
                </div>
                <div class="archives-list col push">
                    <p class="pic"><img src="${tradeBasePath}/mobileapp/images/grandma.png"></p>
                    <p class="name">*桂香</p>
                </div>
                <div class="archives-list col push">
                    <p class="pic"><img src="${tradeBasePath}/mobileapp/images/add.png"  onclick="addFamily()"></p>
                    <p class="name">添加更多</p>
                </div>
            </div>
            <h2 class="h2-tit mt8">最近就诊 <a class="view-more">查看<i class="icon icon-goto"></i></a></h2>
            <div class="hospital-list">
                <div class="item bottomline touch">
                    <img src="${tradeBasePath}/mobileapp/images/hospital1.png" class="pic">
                    <h5 class="name">广州市妇女儿童医疗中心</h5>
                    <p class="info"><span class="rightline">*晓明</span><span>耳科</span></p>
                    <a class="link-more">查看详情</a>
                </div>
                <div class="item bottomline touch">
                    <img src="${tradeBasePath}/mobileapp/images/hospital1.png" class="pic">
                    <h5 class="name">广州市妇女儿童医疗中心</h5>
                    <p class="info"><span>*晓明</span><span>耳科</span></p>
                    <a class="link-more">查看详情</a>
                </div>
                <div class="item bottomline touch">
                    <img src="${tradeBasePath}/mobileapp/images/hospital1.png" class="pic">
                    <h5 class="name">广州市妇女儿童医疗中心</h5>
                    <p class="info"><span>*晓明</span><span>耳科</span></p>
                    <a class="link-more">查看详情</a>
                </div>
            </div>
            <div class="item-more mb8 topline touch">
                <a class="view-item-more">查看更多<i class="icon icon-goto"></i></a>
            </div>
        </div>
        <div class="floor">
            <div class="row">
                <div class="col active push"><i class="icon icon-home"></i><p>首页</p></div>
                <div class="col push"><i class="icon icon-information"></i><p>消息</p></div>
                <div class="col push"><i class="icon icon-personal"></i><p>我的</p></div>
            </div>
        </div>
        <script src="${tradeBasePath}/mobileapp/js/swiper/swiper.min.js?v=${js_version}"></script>
        <script src="${tradeBasePath}/mobileapp/js/homePage/index.js?v=${js_version}"></script>
    </div>
</div>
</body>
</html>
