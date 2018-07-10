//判断ios和android
var IS = {};
IS.iOS = false; // 是否是IOS
IS.android = false; // 是否是安卓
var ua = navigator.userAgent.toLowerCase();
if (/iphone|ipad|ipod/.test(ua)) {
    IS.iOS = true;
} else if (/android/.test(ua)) {
    IS.android = true;
}
var iOSDelayTime = 1000; // iOS延迟执行时间设置 毫秒


function dealWithStr(str) {
    if (isNull(str)) {
        return "";
    } else {
        return str;
    }
}

function isNull(str) {
    var flag = false;
    if (str === window.undefined || str == null || str == "" || str.length == 0 || str == "null" || str == 'undefined') {
        flag = true;
    }
    return flag;
}

/**
 * 计算年龄
 * @param strBirthday
 * @returns
 */
function getAge(strBirthday) {       
	var returnAge;
	if(String(strBirthday).indexOf("-1")){
		var turnDate=new Date(strBirthday);
		strBirthday=turnDate.getFullYear()+"-"+(turnDate.getMonth() + 1)+"-"+turnDate.getDate();
	}
	var strBirthdayArr=strBirthday.split("-");
	var birthYear = strBirthdayArr[0];
	var birthMonth = strBirthdayArr[1];
	var birthDay = strBirthdayArr[2];
	
	var d = new Date();
	var nowYear = d.getFullYear();
	var nowMonth = d.getMonth() + 1;
	var nowDay = d.getDate();
	
	if(nowYear == birthYear) {
		returnAge = 0;//同年 则为0岁
	}else {
		var ageDiff = nowYear - birthYear ; //年之差
		if(ageDiff > 0) {
			if(nowMonth == birthMonth){
				var dayDiff = nowDay - birthDay;//日之差
				if(dayDiff < 0) {
					returnAge = ageDiff - 1;
				}else {
					returnAge = ageDiff ;
				}
			}else{
				var monthDiff = nowMonth - birthMonth;//月之差
				if(monthDiff < 0) {
					returnAge = ageDiff - 1;
				} else {
					returnAge = ageDiff ;
				}
			}
		} else {
			returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天
		}
	}
	return returnAge;//返回周岁年龄
}
var regBox = {
	regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,// 邮箱
	regName : /^[a-zA-Z0-9_-]{3,16}$/,// 用户名
	regMobile : /^0?1[3|4|5|7|8][0-9]\d{8}$/,// 手机
	regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
	regRealName : /^[\u4e00-\u9fa5a-zA-Z0-9]{2,8}$/,
	regCode:/^[a-zA-Z:]{2,50}$/,
	regIdCard:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
	regIdNumber:/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/
}


var SystemUtil = {
    /**
     * 公共跳转
     * 
     * @param goUrl
     *            跳转地址url
     * @param formId
     *            表单id,如果是公共的表单id paramsForm可不传
     * @param falg
     *            是否需要显示app的菜单栏
     */
    goUrl : function(goUrl, formId, falg) {
        var formDom;
        if (!isNull(formId)) {
            formDom = $("#" + formId);
            if ($(formDom).length > 0) {
                $("#" + formId).attr("action", goUrl);
                $("#" + formId).submit();
            } else {
                window.location = goUrl;
            }
        } else {
            if ($("#paramsForm").length > 0) {
                $("#paramsForm").attr("action", goUrl);
                $("#paramsForm").submit();
            } else {
                window.location = goUrl;
            }
        }
        // 首页，消息，我的 页面时,不调用底部隐藏
        if (goUrl.indexOf('/mobileapp/toHomePage') > 0 || goUrl.indexOf('/mobileapp/msgcenter/listView') > 0 || goUrl.indexOf('/mobileapp/appUser/toPerson') > 0) {
            // 首页，消息，我的 页面不调用
            showOrHiddenTab(true);

            if (goUrl.indexOf('/mobileapp/toHomePage') > 0) {
                // 首页
                SetCheckTab(1);
            }

            if (goUrl.indexOf('/mobileapp/msgcenter/listView') > 0) {
                // 消息
                SetCheckTab(2);
            }

            if (goUrl.indexOf(bcUrl) > 0) {
                // 商城
                SetCheckTab(3);
            }
            if (goUrl.indexOf('/mobileapp/appUser/toPerson') > 0) {
                // 我的
                SetCheckTab(4);
            }
        } else {
            showOrHiddenTab(falg);
        }
    },
    /**
     * 直接跳转到首页、我的或者消息，不加载底部栏目，不设置底部选择tab
     */
    goFristUrl : function(goUrl, formId) {
        var formDom;
        if (!isNull(formId)) {
            formDom = $("#" + formId);
            if ($(formDom).length > 0) {
                $("#" + formId).attr("action", goUrl);
                $("#" + formId).submit();
            } else {
                window.location = goUrl;
            }
        } else {
            if ($("#paramsForm").length > 0) {
                $("#paramsForm").attr("action", goUrl);
                $("#paramsForm").submit();
            } else {
                window.location = goUrl;
            }
        }
    },
    /**
     * 跳转到首页
     */
    goHomeIndex : function() {
        var url='/mobileapp/toHomePage';
        //医程通钱包主页 add shitou
        if($("#appCode").val()=="upWallet")
        {
            url='/mobileapp/toThirdHomePage';
        }
        var homeIndexUrl = appPath + url;
        var curUrl = $("#curUrl");
        if (curUrl.length > 0) {
            $(curUrl).val(appPath + url);
        }
        SystemUtil.goUrl(homeIndexUrl, "paramsForm", true);
    }
}

/**
 * 跳转到首页
 */
function goHomeIndex() {
    var url='/mobileapp/toHomePage';
    //医程通钱包主页 add shitou
    if($("#appCode").val()=="upWallet")
    {
        url='/mobileapp/toThirdHomePage';
    }
    var homeIndexUrl = appPath + url;
    SystemUtil.goFristUrl(homeIndexUrl, "paramsForm");
}

/**
 * 跳转到消息
 */
function goMessageIndex() {
    var messageIndexUrl = appPath + '/mobileapp/msgcenter/listView';
    SystemUtil.goFristUrl(messageIndexUrl, "paramsForm");
}

/**
 * 跳转到我的
 */
function goMyIndex() {
    var myIndexUrl = appPath + '/mobileapp/appUser/toPerson';
    SystemUtil.goFristUrl(myIndexUrl, "paramsForm");
}





/* 2016-5-14 add */
(function() {
    window.$Y = function() {
    };
    $Y.init = function() {
        $Y.hover();
    }

    /**
     * 加载loading 例子 ：$Y.loading.show(); $Y.loading.show('加载中'); $Y.loading.hide();
     * 
     */
    $Y.loading = {
        setHTML : function(str) {
            var htmltext = '<div class="am-loading am-loading-show" id="loading_warp">' + '<div class="am-loading-mark"></div><div class="am-loading-text">' + '<span class="am-icon-loading"></span><b id="am-loading_txt" style="font-weight: normal">' + str + '</b></div></div>';
            $('body').append(htmltext);
        },
        show : function(str) {
            var str = str || '加载中...';
            if ($('#loading_warp').length > 0) {
                $('#am-loading_txt').html(str);
                $('#loading_warp').show();
            } else {
                this.setHTML(str);
            }
        },
        hide : function() {
            $('#loading_warp').hide();
            $('#am-loading_txt').html('加载中');
        }
    }

    /* dialog 弹窗 */

    /**
     * 例子1： 没有按钮，没有标题只有内容的弹窗
     * 
     * var myBox = new $Y.confirm({ ok:{ title:"确定", click:function(){ //参数可为空，没有默认方法,不会自动关闭窗体，可用 myBox.close()来关闭 alert('这是确定') } }, cancel:{ title:"取消", click:function(){ //参数可为空, 当为空时默认方法关闭窗体 alert('这是取消') } } ,callback:function(){ //窗体显示后的回调 } ) // ok 和 cancel 可以单独存在（只有一个按钮），或同时存(两个按钮)
     * 
     */

    $Y.confirm = function(opts) {
        if (typeof opts != "object") {
            return false;
        }
        var d = new Date().getTime();
        this.id = 'dialog' + d;

        var me = $Y.confirm;
        var op = {};
        op.btnSize = 0;
        op.id = 'dialog' + d;
        me.prototype.init = function() {
            var that = this;
            op.title = opts.title || null;
            op.toolBtn = opts.toolBtn || null;
            op.style = opts.style || '';

            op.ok = opts.ok || {};

            if (op.ok.title) {
                op.okTitle = opts.ok.title || '提示';
                op.okEvent = opts.ok.click || this.close;
                op.btnSize++
            }

            op.cancel = opts.cancel || {};
            if (op.cancel.title) {
                op.cancelTitle = opts.cancel.title || '取消';
                op.cancelEvent = opts.cancel.click || this.close;
                op.btnSize++
            }

            op.content = opts.content || "";
            op.callback = opts.callback || $.noop();
            op.load = opts.load || '';
            op.vertical = opts.vertical || '';
            op.closebut = opts.closebut || '';

            var html = "";
            html += '<div  id="' + op.id + '" class="modal">';

            if (op.title) {
                if (op.title == "null") {
                    op.title = "";
                }
                html += '<h2 class="modal-h2 optTit">' + op.title + '<!-- <a class="icon-close-click"><i class="icon icon-close middle"></i></a>--></h2>';
                setTimeout(function() {
                    $(".modal-con").css("padding-top", "70px")
                }, 100)
            }

            html += '<div class="dialogOptCon modal-con middle">';

            html += '<div class="modal-middle optTic">' + op.content + '</div></div>';
            if (op.btnSize) {
                html += '<div class="button row optBtn">';
                var btnBlock = op.btnSize < 2 ? ' btn2-block' : '';
                if (op.cancelTitle) {
                    html += '<div class="but-save col but-cancel btn2 btn2-cancel modal-but-save-reset' + btnBlock + '">' + op.cancelTitle + '</div>';
                }
                if (op.okTitle) {
                    html += '<div class="but-save col btn2 btn2-ok modal-but-save-reset' + btnBlock + '">' + op.okTitle + '</div>';
                }

                setTimeout(function() {
                    $(".modal-con").css("padding-bottom", "80px")
                }, 100)

            }
            html += ' </div>';
            html += ' </div>';
            html += '<div class="backdrop"></div>';
            $("body,html").addClass("ovfHiden");
            $("body").append(html);
            $(".backdrop").fadeIn(300);
            
            $("#" + that.id).fadeIn(600, function() {
                $("#" + that.id).show();
            });
            //$("#" + that.id).addClass("modalDisplay");
            if (!op.vertical) {
                $("#" + that.id).find(".modal-con").removeClass("middle");
            } else {
                $("#" + that.id).find(".modal-con").addClass("middle");
            }
            if (op.closebut) {
                $("#" + that.id).find(".modal-h2").append('<a class="icon-close-click"><i class="icon icon-close middle"></i></a>')
            } else {
                // $("#modal .modal-con").addClass("middle");
            }

            setTimeout(function() {
                that.bind();
                console.log(op.style)
                that.css(op.style)
            }, 250)
        };
        me.prototype.bind = function() {
            var that = this;
            var okClick = function() {
                if (typeof op.okEvent == "function") {
                    op.okEvent(that);
                }
            };
            var cancelClick = function() {

                if (typeof op.cancelEvent == "function") {
                    op.cancelEvent(that);
                    ;
                }

            };

            var closeClick = function() {
                $("#" + that.id).fadeOut(300);
                $("#" + that.id).next(".backdrop").fadeOut(700, function() {
                    $("#" + that.id).next(".backdrop").remove();
                    $("#" + that.id).remove();
                });
                $("body,html").removeClass("ovfHiden");
            };

            // $('.icon-close-click').on('click',function(that){ cancelClick(that) })
            $("#" + that.id).find('.btn2-ok').on('click', function(that) {
                okClick(that)
            })
            $("#" + that.id).find('.btn2-cancel').on('click', function(that) {
                cancelClick(that)
            })
            $("#" + that.id).find('.icon-close-click').on('click', function(that) {
                closeClick(that)
            })

            if (op.load) {
                that.css({
                    transition : 'all ease 0.5s',
                    '-webkit-transition' : 'all ease 0.5s',
                    '-moz-transition' : 'all ease 0.5s'
                });
                that.content('<div class="am-loading-text" style="text-align:center;"><span class="am-icon-loading"></span><b id="am-loading_txt" style="font-weight: normal">加载中...</b></div>')
                $.ajax({
                    url : op.load,
                    dataType : 'html',
                    type : 'post',
                    timeout : 1000 * 30,
                    success : function(res) {
                        if (res) {
                            that.content(res);
                            that.setCenter()
                        }
                    },
                    error : function(err) {
                        that.content(err);
                        that.setCenter()
                    }
                })
            }
        }

        me.prototype.close = function() {
            var that = this;

            if ($('.modal-con').size() < 2) {
                // $('#dialogOpt').remove();
                $("#" + that.id).fadeOut(300);
                $("#" + that.id).next(".backdrop").fadeOut(700, function() {
                    $("#" + that.id).next(".backdrop").remove();
                    $("#" + that.id).remove();
                });
                $("body,html").removeClass("ovfHiden");

            } else {

                $("#" + that.id).remove();
            }
        }
        me.prototype.content = function(str) {
            var that = this;
            $("#" + that.id).find('.optTic').html(str)

        }
        me.prototype.css = function(style) {
            var that = this;
            if (typeof style == "object") {
                $("#" + that.id).css(style)
            }
        }

        me.prototype.setCenter = function() {
            var that = this;
            var _h = $("#" + that.id).height();
            var _top = (window.innerHeight - _h) / 2;
            if (_top <= 10) {
                _top = 30;
            }
            if (_h >= window.innerHeight - 10) {
                var _bottom = 30;
                $("#" + that.id).css({
                    "top" : _top + 'px',
                    "bottom" : _bottom + 'px'
                })
                $("#" + that.id).find('.optTic').css({
                    height : '100%',
                    overflow : 'auto'
                });
            } else {
                $("#" + that.id).css({
                    "top" : _top + 'px',
                    'overflow' : 'auto'
                })
            }

        }

        // 初始化
        this.init();
    }

    $Y.tips = function(str, url, time) {
        var t = null;
        var time = time || 1500;
        if (str) {
            var html = '<div id="tips" style="display:none;">' + '<div class="tips_content">' + '<div class="tips_bg"></div>' + '<div class="tips_content_txt"><p class="p1-icon"><i class="icon icon-caveat"></i></p>' + str + '</div></div>' + '</div>'
            if ($('#tips').size() > 0) {
                $('#tips .tips_content_txt').html(str);
            } else {
                $('body').append(html)
                $("#tips").fadeIn(300);
            }
            setTimeout(function() {
                $("#tips").fadeOut(300, function() {
                    $("#tips").remove();
                });
            }, time)
        }
        if (url) {
            setTimeout(function() {
                window.location = url
            }, time)
        }

    }
    
    /**
     * 名称：增加点击交互效果
     */

    $Y.hover = function() {

        var me = $Y.hover;
        var O = null, t = null;
        me.init = function() {
            $('.touch,.push').attr({
                'ontouchstart' : '$Y.hover.TouchOn(this)',
                'ontouchend' : '$Y.hover.TouchOut(this)'
            })
        }
        me.TouchOn = function(obj) {
            O = $(obj);
            $(obj).addClass('hover');

        }
        me.TouchOut = function(obj) {
            clearTimeout(t);
            if (!O)
                return;

            t = setTimeout(function() {
                $(obj).removeClass('hover');
            }, 50);
            O = null
        }
        me.TouchMove = function(e) {
            var y = e.touches[0].pageY;
            if (!O)
                return;
            O.removeClass('hover');
            O = null;
            clearTimeout(t);
        }
        $('body').attr({
            'ontouchmove' : '$Y.hover.TouchMove(event)'
        });


        $Y.hover.init();
    }

}(window.jQuery))
$(function() {
    $Y.init();
})

function createPage(data,tit){
   var num,obj;
   obj = $(".container > .box");
   obj.addClass('out');
   var htm  = '<div class="box trans begin">';
       htm += '<div class="header"><i class="icon icon-back" onclick="backPrevPage(this)"></i>';
       htm += '<span class="h1-tit">'+ tit +'</span></div>';
       htm += '<div class="boxMain"><div class="boxcon pt70 pb0"><p class="loadingCon"><i class="icon icon-loading"></i></p></div></div>';
       htm += '</div>';
   $(".container").append(htm);
   setTimeout(function(){
       num = obj.length;
       if(num > 1) { 
           $(".container > .box:first").remove();
       }
       $(".container > .box:last").removeClass('begin').find('.boxMain').html(data);
       setTimeout(function(){
           //$(".container > .box.out").html('');
       },300)
   },10)
}

//滑动返回上一页
function backPage(data){
   var backUrl = $("#backUrl").val();
   $(".container > .box:last").addClass('begin');
   $(".container > .box.out").removeClass('out');
   setTimeout(function(){
         $(".container > .box.begin").remove();
    },300)
}

//点击返回一页
function backPrevPage(e) {
    var backUrl = $("#backUrl").val();
    var parentClass = $(e).closest('.box');
    parentClass.addClass('begin');
    $(".container > .box.out").removeClass('out');
    setTimeout(function(){
         parentClass.remove();
    },300)

}

$(function() {
    var startPos = 0,
        endPos = 0,
        parent = null,
        offset = 0;
    document.body.addEventListener('touchstart', function(e) {
        var touch = e.touches[0];
        startPos = touch.pageX;
        var len = $(".container .box").length;
        if(len < 2) {  
            return;
        }

    }, false);
    document.body.addEventListener('touchmove', function(e) {
        var touch = e.touches[0];
        var len = $(".container .box").length;
        if(len < 2) {  
            return;
        }
        endPos = touch.pageX;
        console.log(startPos);
        if(startPos < 20) {
            offset = endPos - startPos;
            if(offset > 0) {
                event.preventDefault();
                $('.container .box:last').css("transform","translate3d("+offset+"px,0,0)");
            }
        }
    }, false);
    document.body.addEventListener('touchend', function(e) {
        var len = $(".container .box").length;
        if(len < 2) {  
            return;
        }
       if(startPos < 20) {
           if(offset > 80) {
              backPage();
           }else if( offset > 0 ) {
              $('.container .box:last').css("transform","translate3d(0,0,0)");
           }
       }
    }, false);

})



