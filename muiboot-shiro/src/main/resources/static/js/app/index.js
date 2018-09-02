	var $breadcrumb = $(".breadcrumb");
	var $main_content = $("#main-content");
    $(document).ready(function() {
        if($MB.isMobile()||$MB.isXsScreen()){
            $(".layui-layout.layui-layout-admin").addClass("shrink");
            $("body").on("touchend",".layui-body",function (e) {
                $(".layui-layout.layui-layout-admin").addClass("shrink");
            });
        }
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
        $MB.layerPost({url:ctx  + "session/getUserMenu",data:{ "userName": userName }},function (r) {
            if (r.code == 0) {
                var data = r.msg;
                $("#navigation").remove("ul");
                $("#navigation").append(forTree(data.children));
                bindNav();
            } else {
                layer.msg('获取菜单失败！', {icon: 0});
            }
        });
	}),$(document).ready(function() {//菜单点击绑定
        $("body").on("click","a[menu-id]",function () {
			var $this = $(this);
			var id =$this.attr("menu-id");
            var $thisa=$("#navigation").find("a[menu-id='"+id+"']");
            var url =$thisa.attr("menu-url");
            if(!!id){
                var breadcrumbMenu=new Object();
                breadcrumbMenu.id=  new Array();
                breadcrumbMenu.name=  new Array();
                $thisa.parents("dl").prev().each(function() {
                    breadcrumbMenu.id.unshift($(this).attr("menu-id"));
                    breadcrumbMenu.name.unshift($(this).text());
                });
                var breadcrumnHtml = "";
                breadcrumnHtml += '<a href=""><i class="layui-icon layui-icon-home" style="font-size: 14px;color: #000;"></i> 首页</a>';
                for (var i = 0; i < breadcrumbMenu.name.length; i++) {
                    breadcrumnHtml += '<span lay-separator="">/</span>';
                    breadcrumnHtml += '<a href="javascript:;" menu-id="'+breadcrumbMenu.id[i]+'" >'+breadcrumbMenu.name[i]+'</a>';
                }
                breadcrumnHtml += '<span lay-separator="">/</span>';
                breadcrumnHtml+= '<a href="javascript:;" menu-id="'+id+'" menu-url="'+(!url?"":url)+'"><cite>'+$thisa.text()+'</cite></a>';
                $breadcrumb.html("").append(breadcrumnHtml);
			}
			if(!!url){
                var $parent=$thisa.parents("li");
			    if(!$parent.hasClass("layui-nav-itemed")){
                    $parent.addClass("layui-nav-itemed");
                    $thisa.parents("dd").addClass("layui-nav-itemed layui-this");
                }
                loadMain(url);
			}
        });
    }),$(document).ready(function(){
        layui.extend({
            dicutils: './toolkit/muiboot/js/dicutils' // {/}的意思即代表采用自有路径，即不跟随 base 路径
        });
    });

	function loadMain(url) {
        $main_content.ajax_load("loading",url);
	};