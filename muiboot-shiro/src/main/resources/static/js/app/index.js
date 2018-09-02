    $(document).ready(function() {
        //1.如果是移动端，添加触屏关闭菜单事件
        if($MB.isMobile()||$MB.isXsScreen()){
            $(".layui-layout.layui-layout-admin").addClass("shrink");
            $("body").on("touchend",".layui-body",function (e) {
                $(".layui-layout.layui-layout-admin").addClass("shrink");
            });
        }
        //2.初始化字典,菜单插件
        layui.extend({
            dicutils: $MB.getRootPath()+'/toolkit/muiboot/js/dicutils',
            menu: $MB.getRootPath()+'/toolkit/muiboot/js/ajax-load-menu',
        });
        //3.hash监听事件
        if(!$MB.hasHistoryApi()){
            window.onhashchange=function (e) {
                var hash =window.location.hash;
                if(!hash)return;
                $("#main-content").ajax_load("loading",hash);
            }
        }else {
            $("body").on("click","a[menu-id]",function () {
                var $this = $(this);
                var id =$this.attr("menu-id");
                var $thisa=$("#navigation").find("a[menu-id='"+id+"']");
                var url =$thisa.attr("menu-url");
                if(!!url){
                    $("#main-content").ajax_load("loading",url);
                }
            });
        }
        //4.菜单栏打开关闭点击监听
        $(document).on("click",'.layadmin-flexible',function () {
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
        //5.加载菜单
        layui.use(['menu'], function(args){
            layui.menu.loadmenu(userName);
        });
	});
/*
        $(document).ready(function(){
	    var str = "",has=$MB.hasHistoryApi();
	    var forTree = function(o) {
	        if(!o||o.length===0)return "";
            str+='<ul class="layui-nav layui-nav-tree"  lay-filter="menu" id="sys-menu-tree">';
	        for (var i = 0; i < o.length; i++) {
	            var href,urlstr = '<li class="layui-nav-item">';
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
                        href=has?"javascript:;":"#"+o[i]["href"];
                        urlstr+='<a href="'+href+'" menu-url=' + o[i]["href"]+' menu-id="'+o[i]["id"]+'"><i class="'+icon+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
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
                var urlstr = '<dd>',href;
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
                        href=has?"javascript:;":"#"+o[i]["href"];
                        urlstr +='<a href="'+href+'" menu-url=' + o[i]["href"]+' menu-id="'+o[i]["id"]+'"><i class="'+o[i]["icon"]+'"></i><span >&nbsp;&nbsp;'+ o[i]["name"] +'</span></a>';
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
        };
        $MB.layerPost({url:ctx  + "session/getUserMenu",data:{ "userName": userName }},function (r) {
            if (r.code == 0) {
                var data = r.msg;
                $("#navigation").remove("ul");
                $("#navigation").append(forTree(data.children));
                bindNav();
                var forward = $("#main-content").attr("forward");
                if(!!forward){
                    if($MB.hasHistoryApi()){
                        $main_content.ajax_load("loading",forward);
                    }else {
                        window.location.hash=window.location.hash+"#"+forward;
                    }
                }
            } else {
                layer.msg('获取菜单失败！', {icon: 0});
            }
        });
	});*/
