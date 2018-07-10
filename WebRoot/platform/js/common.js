// dom加载完成后执行的js
$(function() {
	// 全选
	$(document).on('click', 'input[type=checkbox][name=all]', function() {
		var ps = $(this).parents('.form-check');
		var p = $(this).parents('label');
		if (p.hasClass('check')) {
			p.removeClass('check');
			ps.find('.checkboxTwo').removeClass('check');
		} else {
			p.addClass('check')
			ps.find('.checkboxTwo').addClass('check');
		}
	});

	// 单选
	$(document).on('click', 'input[type=radio]', function() {
		var p = $(this).parents('label');
		var name = $(this).attr('name');
		$('form input[name=' + name + ']').parents('label').removeClass('check');
		p.addClass('check');
	});
	// 多选
	$(document).on('click', 'input[type=checkbox][name!=all]', function() {
		var ps = $(this).parents('.form-check');
		var p = $(this).parents('label');
		if (p.hasClass('check')) {
			p.removeClass('check')
		} else {
			p.addClass('check')
		}
		var size = ps.find('input[type=checkbox][name!=all]').size();
		var iCount = ps.find('.checkboxTwo.check').size();
		// console.log(iCount);
		if (iCount == size) {
			$('.checkboxTwoAll').addClass('check');
		} else {
			$('.checkboxTwoAll').removeClass('check');
		}
	});
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
	            var html = '<div id="tips" style="display:none;">' + '<div class="tips_content">' + '<div class="tips_bg"></div>' + '<div class="tips_content_txt">' + str + '</div></div>' + '</div>'
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
	// 侧边导航展缩
	$(document).on('click', '.sidebar-height > ul>li>a', function() {
		if ($(this).parents("li").hasClass("active")) {

		} else {
			$(".sidebar-height li").removeClass("active");
			$(".sidebar-height li ul").slideUp();
			$(this).parents("li").find("ul").slideDown();
			$(this).parents("li").addClass("active");

		}

	});

});

(function($) {
	/**
	 * 分页插件
	 * 
	 * @param type
	 *            请求类型,get或者post,默认post
	 * @param url
	 *            请求地址
	 * @param successCallback
	 *            请求成功后的回调函数,用于自己实现分页数据列表展示或其他处理
	 * @param errorCallback
	 *            请求失败后的回调函数,用于错误提示或其他处理
	 * @param dataType
	 *            数据类型,默认json
	 * @params params 请求参数,不需要携带分页参数,只需设定你的查询参数即可
	 */
	$.fn.pagination = function(options) {
		var settings = {
			type : "post",
			url : "",
			successCallback : {},
			errorCallback : function() {
				alert("系统内部错误,请联系管理员。");
			},
			dataType : "json",
			params : {}
		};
		var obj = $(this);
		settings = $.extend(settings, options || {}); // 合并默认和用户配置
		var pageInfo = {
			"pageNum" : $('input[name="pageNum"]').val(),
			"pageSize" : $('input[name="pageSize"]').val()
		};
		var params = $.extend({}, pageInfo, options.params);
		// 请求分页数据
		$.ajax({
			type : settings.type,
			url : settings.url,
			data : params,
			dataType : settings.dataType,
			success : function(data) {
				// 请求成功后调用成功回调函数
				settings.successCallback(data);
				// 设置分页信息
				pagingInfo(obj, data);
				// 解除事件绑定
				$(".pagingInfo").unbind("click");
				$(".goto.pagingInfoGoto").unbind("click");
				$(".prePagingInfo").unbind("click");
				$(".nextPagingInfo").unbind("click");
				$(".pagingInfo").unbind("click");
				// 为页码上的超链接绑定点击事件
				$(".pagingInfo").bind("click", function() {
					// 获取点击的页码值
					var pageNum = $(this).text();
					// 校正页码
					changePage(pageNum);
					// 调用分页插件以响应点击事件
					obj.pagination(options);

				});
				// 为跳转超链接绑定点击事件
				$(".goto.pagingInfoGoto").bind("click", function() {
					// 获取输入的页码值
					var pageNum = $('input[name="gotoInput"]').val();
					// 校正页码
					changePage(pageNum);
					// 调用分页插件以响应点击事件
					obj.pagination(options);
				});
				// 为上一页,下一页绑定点击事件
				$(".prePagingInfo").bind("click", function() {
					// 获取上一页页码值
					var pageNum = $('input[name="prePage"]').val();

					// 校正页码
					changePage(pageNum);
					// 调用分页插件以响应点击事件
					obj.pagination(options);

				});
				$(".nextPagingInfo").bind("click", function() {
					// 获取下一页页码值
					var pageNum = $('input[name="nextPage"]').val();
					// 校正页码
					changePage(pageNum);
					// 调用分页插件以响应点击事件
					obj.pagination(options);
				});
			},
			error : function(XmlHttpRequest, textStatus, errorThrown) {
				settings.errorCallback();
			}
		});
	};
	// 分页信息
	pagingInfo = function(obj, data) {
		var root = obj;
		root.empty();
		var ul = "<div class='pagination-recording'>共有 <span class='colorblue'>" + data.total + "</span> 条记录   <span class='qrnum'>" + data.pages + "</span>页</div>";
		ul += "<ul>";
		if (data.prePage == 0) {
			ul += "<li class='disabled'>上一页</li>";
		} else {
			ul += "<li><a class='prePagingInfo' href='javascript:void(0);'>上一页</a></li>";
		}

		if (data.pages != 0) {
			// 控制页码显示数量
			var pageNum = 6;// 显示多少个页码
			var pageStart, pageEnd;
			if (data.pageNum <= pageNum / 2 + 1) {
				pageStart = 1;
				pageEnd = pageNum;
				if (pageEnd > data.pages) {
					pageEnd = data.pages;
				}
			} else if (data.pageNum > pageNum / 2 + 1) {
				pageStart = data.pageNum - pageNum / 2;
				pageEnd = data.pageNum + pageNum / 2 - 1;
				if (pageEnd > data.pages) {
					pageEnd = data.pages;
				}
				// 向左补页码
				if ((pageEnd - pageStart) + 1 < pageNum) {
					pageStart = pageStart - (pageNum - (pageEnd - pageStart + 1));
					if (pageStart < 1) {
						pageStart = 1;
					}
				}
			}
			for (var i = pageStart; i <= pageEnd; i++) {
				if (i == data.pageNum) {
					ul += "<li class='active'><span>" + i + "</span></li>";
				} else {
					ul += "<li><a class='pagingInfo' href='javascript:void(0);' >" + i + "</a></li>";
				}
			}
		} else {
			ul += "<li class='active'><span>1</span></li>";
		}
		if (data.nextPage == 0) {
			ul += "<li class='disabled'>下一页</li>";
		} else {
			ul += "<li><a class='nextPagingInfo' href='javascript:void(0);'>下一页</a></li>";
		}

		ul += "<div class='pageGoto'>";
		ul += "<span>转到第</span> <input type='text' name='gotoInput' class='goto_input'/><span>页</span>";
		ul += "<a href='javascript:void(0);' class='goto pagingInfoGoto'>跳转</a>";
		ul += "</div>";
		ul += "<input type='hidden' name='prePage' value='" + data.prePage + "'/>";
		ul += "<input type='hidden' name='nextPage' value='" + data.nextPage + "'/>";
		ul += "<input type='hidden' name='pageNum' value='" + data.pageNum + "'/>";
		ul += "<input type='hidden' name='pageSize' value='" + data.pageSize + "'/>";
		ul += "<input type='hidden' name='pages' value='" + data.pages + "'/>";
		ul += "<input type='hidden' name='listSize' value='" + data.list.length + "'/>";
		ul += "</ul>";
		root.append(ul);
	};
	// 校正页码
	changePage = function(pageNum) {
		if (pageNum) {
			var pages = $('input[name="pages"]').val();
			var pageNumInput = $('input[name="pageNum"]');

			// 如果输入的页数是非数字，则还是跳到当前页
			if (isNaN(pageNum)) {
				pageNum = pageNumInput.val();
			}

			pages = Number(pages);
			pageNum = Number(pageNum);

			// 如果页数大于总页数，则跳至最后一页，如页数小于最小页数，则跳至第一页
			if (pageNum >= pages) {
				pageNum = pages;
			} else if (pageNum <= 1) {
				pageNum = 1;
			}
			pageNumInput.val(pageNum);
		}
	}

})(jQuery);

// 表单赋值
$.fn.formEdit = function(data) {
	return this.each(function() {
		var elementDom;
		var elementDomName;
		if (data == null) {
			this.reset();
			return;
		}
		for (var i = 0; i < this.length; i++) {
			elementDom = this.elements[i];
			elementDomName = elementDom.name;
			if (data[elementDomName] == undefined) {
				continue;
			}
			elementDom.value = data[elementDomName];
		}
	});
};

function isNull(str) {
	var flag = false;
	if (str === window.undefined || str == null || str == "" || str.length == 0 || str == "null") {
		flag = true;
	}
	return flag;
}



(function($) {
	$.dateFormat = function(format, obj) {
		if (isNull(obj)) {
			obj = new Date();
		}
		var date = {
			"M+" : obj.getMonth() + 1,
			"d+" : obj.getDate(),
			"H+" : obj.getHours(),
			"m+" : obj.getMinutes(),
			"s+" : obj.getSeconds(),
			"q+" : Math.floor((obj.getMonth() + 3) / 3),
			"S+" : obj.getMilliseconds()
		};
		if (/(y+)/i.test(format)) {
			format = format.replace(RegExp.$1, (obj.getFullYear() + '').substr(4 - RegExp.$1.length));
		}
		for ( var k in date) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
			}
		}
		return format;
	}
})(jQuery);

var regBox = {
	regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,// 邮箱
	regName : /^[a-zA-Z0-9_-]{3,16}$/,// 用户名
	regMobile : /^0?1[3|4|5|7|8][0-9]\d{8}$/,// 手机
	regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
	regRealName : /^[\u4e00-\u9fa5a-zA-Z0-9]{2,8}$/,
	regCode:/^[a-zA-Z:]{2,50}$/,
	regIdNumber:/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/
}
function selectAll() {
	var checklist = document.getElementsByName("check");
	if (document.getElementById("checkAll").checked) {
		for (var i = 0; i < checklist.length; i++) {
			checklist[i].checked = 1;
		}
	} else {
		for (var j = 0; j < checklist.length; j++) {
			checklist[j].checked = 0;
		}
	}
}

function openUrl(url) {
	if (typeof url == 'function') {
		url();
	} else {
		$.ajax({
			url : tradeBasePath + "/" + url,
			type : 'POST',
			error : function(request) {
				alert("操作失败");
			},
			success : function(data) {
				if (data != null && data.status == "OK") {
					var returnurl = tradeBasePath + "/" + data.message
					alert("操作成功");
					openUrl(returnurl);
				} else {
					$("#content").html(data);
				}
			}
		});
	}

}

function hasButtonAuth() {
	if($(".operation-button").find("a").length == 0) {
		$(".operation-button").css("display", "none");
	}
}

function openEnCodeUrl(url,urlEnCode){
	url = url + encodeURI(encodeURI(urlEnCode));	
	openUrl(url);
}

// function dateFormat(time, format) {
// var t = new Date(time);
// var tf = function(i) {
// return (i < 10 ? '0' : '') + i
// };
// return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
// switch (a) {
// case 'yyyy':
// return tf(t.getFullYear());
// break;
// case 'MM':
// return tf(t.getMonth() + 1);
// break;
// case 'mm':
// return tf(t.getMinutes());
// break;
// case 'dd':
// return tf(t.getDate());
// break;
// case 'HH':
// return tf(t.getHours());
// break;
// case 'ss':
// return tf(t.getSeconds());
// break;
// }
// });
// }

