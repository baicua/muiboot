	var $breadcrumb = $(".breadcrumb");
	var $main_content = $("#main-content");
	var loadspan="";
    $(document).ready(function() {
        loadspan=layer.load(2,{
            shade: [0.5,'#fff'] //0.1透明度的白色背景
        });
	}), $(window).on("load", function() {
	    var str = "";
	    var forTree = function(o) {
	        if(!o||o.length===0)return "";
            str+='<ul class="layui-nav layui-nav-tree"  lay-filter="test">';
	        for (var i = 0; i < o.length; i++) {
	            var urlstr = '<li class="layui-nav-item">';
	            try {
	                if (!o[i]["href"] && !!o[i]["icon"]) {
	                	urlstr+='<a href="javascript:;" menu-id="'+o[i]["id"]+'"><i class="'+o[i]["icon"]+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
	                } else if (!o[i]["href"]) {
                        urlstr+='<a href="javascript:;" menu-id="'+o[i]["id"]+'"><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
                    } else {
                        var icon="";
                        if(o[i]["icon"]){
                            icon=o[i]["icon"];
                        }
                        urlstr+='<a href="javascript:;" menu-url=' + o[i]["href"]+' menu-id="'+o[i]["id"]+'"><i class="'+icon+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
	                }
	                //str += urlstr;
	                if (!!o[i]["children"]&&o[i]["children"].length != 0) {
                        urlstr+='<dl class="layui-nav-child">';
                        urlstr+=forChildrenTree(o[i]["children"]);
                        urlstr+='</dl>';
	                }
	            } catch (e) {
	                console.log(e);
	            }
                urlstr += '</li>';
                str+=urlstr;
            }
            str+="</ul>";
	        return str;
	    };
        var forChildrenTree = function(o) {
            if(!o||o.length===0)return "";
            var restr=""
            for (var i = 0; i < o.length; i++) {
                var urlstr = '<dd>';
                try {
                    if (!o[i]["href"] && !!o[i]["icon"]) {
                        urlstr +='<a href="javascript:;"  menu-id="'+o[i]["id"]+'"><i class="'+o[i]["icon"]+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
                    } else if (!o[i]["href"]) {
                        urlstr +='<a href="javascript:;" menu-id="'+o[i]["id"]+'"><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
                    } else {
                        var icon="";
                        if(o[i]["icon"]){
                            icon=o[i]["icon"];
                        }
                        urlstr +='<a href="javascript:;" menu-url=' + o[i]["href"]+' menu-id="'+o[i]["id"]+'"><i class="'+o[i]["icon"]+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
                    }
                    if (!!o[i]["children"]&&o[i]["children"].length != 0) {
                        urlstr+='<dl class="layui-nav-child">';
                        urlstr+=forChildrenTree(o[i]["children"]);
                        urlstr+='</dl>';
                    }
                    urlstr += '</dd>';
                } catch (e) {
                    console.log(e);
                }
                restr+=urlstr;
            }
            return restr;
        };
        var bindNav=function () {
            layui.use('element',function () {
                var element = layui.element;
//初始化动态元素，一些动态生成的元素如果不设置初始化，将不会有默认的动态效果
                element.init();
//导航条点击监听
                element.on('nav(bigData)',function (elem) {
                    console.log(elem);
                });
//tab切换监听
                element.on('tab(test)',function (data) {
                    console.log(data);
                });
//手风琴折叠面板开启关闭监听
                element.on('collapse(collapseFilter)',function (data) {
                });
//触发的事件
                var active = {
                    setPercent: function () {
//设置50%进度
                        element.progress('demo', '50%')
                    }
                };
//点击事件的监听
                $('.site-demo-active').on('click', function(){
                    var othis = $(this);
                    var type = $(this).data('type');
                    active[type] ? active[type].call(this, othis) : '';
                });
            });
            $('.layadmin-flexible').click(function() {
                if ($(".layui-layout-admin").hasClass("shrink")) {
                    $(".layui-layout-admin").removeClass("shrink");
                    $('.layui-side-scroll li>a,.layui-side-scroll dd>a').unbind("mouseenter").unbind("mouseleave");
                } else {
                    $(".layui-layout-admin").addClass("shrink");
                    $('.shrink .layui-side-scroll li>a,.shrink .layui-side-scroll dd>a').hover(function (r) {
                        layer.tips($(this).text(), $(this));
                    },function (r) {
                        layer.close(layer.index);
                    });
                }
            });
        };
        $.post(ctx + "menu/getUserMenu", { "userName": userName }, function(r) {
            if (r.code == 0) {
                var data = r.msg;
                $("#navigation").html(forTree(data.children));
            } else {
                layer.alert('获取菜单失败！', {icon: 0});
            }
        })
		.error(function() { layer.alert('获取菜单失败！', {icon: 0});})
		.complete(function() {
			layer.close(loadspan);
            bindNav();
		});
	}),$(document).ready(function() {//菜单点击绑定
        $("body").on("click","a[menu-id]",function () {
			var $this = $(this);
			var id =$this.attr("menu-id");
			var url =$this.attr("menu-url");
            var $thisa=$("#navigation").find("a[menu-id='"+id+"']");
			if(!!id){
                var text_arr = new Array();
                $thisa.parents("dl").prev().each(function() {
                    text_arr.unshift($(this).text());
                });
                var breadcrumnHtml = "";
                breadcrumnHtml += '<a href=""><i class="layui-icon layui-icon-home" style="font-size: 14px;color: #000;"></i> 首页</a>';
                for (var i = 0; i < text_arr.length; i++) {
                    breadcrumnHtml += '<span lay-separator="">/</span>';
                    breadcrumnHtml += '<a href="">'+text_arr[i]+'</a>';
                }
                breadcrumnHtml += '<span lay-separator="">/</span>';
                breadcrumnHtml+= '<a href=""><cite>'+$thisa.text()+'</cite></a>';
                $breadcrumb.html("").append(breadcrumnHtml);
			}
			if(!!url){
                loadMain(url);
			}
        });
    });

	function loadMain(url) {
        $main_content.ajax_load("loading",url);
	};