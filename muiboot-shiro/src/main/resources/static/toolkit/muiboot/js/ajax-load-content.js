;(function($) {
    "use strict";
    var ajax_loaded_page = {urls:{}};//ajax加载的页面
    var root_url = $MB.getRootPath();
    var $contentArea;
    var $breadcrumb = $(".breadcrumb");
    var $navigation=$("#navigation");
    var cache={
        sessionCache:{},
         useCache: function(cacheName,url,millisecond){
                if(this.sessionCache[url])return true;
                this.sessionCache[url]=true;
                if(!window.localStorage)return false;
                var storage=window.localStorage;
                var data=$.extend({},JSON.parse(storage.getItem(cacheName)));
                var oldmillisecond=data[url];
                if(!oldmillisecond){
                    data[url]=millisecond;
                    storage.setItem(cacheName,JSON.stringify(data));
                    return false;
                }
                //计算出相差天数
                var days=Math.floor((millisecond-oldmillisecond)/(24*3600*1000));
                if(days<=7){
                    return true;
                }else {
                    data[url]=millisecond;
                    storage.setItem(cacheName,JSON.stringify(data));
                    return false;
                }
            }
    };
    $.fn.ajax_load = function(options, param) {
        // 如果是调用方法
        if (typeof options == 'string') {
            if(!$contentArea){
                $contentArea =this.ajax_load();
                return $contentArea.ajax_load(options,param);
            }
            return $.fn.ajax_load.methods[options]($contentArea, param);
        }
        if(!!$contentArea){
            return $contentArea;
        }
        // 如果是初始化组件
        options = $.extend({}, $.fn.ajax_load.defaults, options || {});
        $contentArea=$(this);
        // 加载数据
        ajax_loaded_page.buildBreadcrumn=function($menu){
            var hash=$menu.attr("href");
            if($menu.length>0&&hash){
                var $menuName=$.trim($menu.text());
                var id =$menu.attr("menu-id");
                var breadcrumbMenu=new Object();
                breadcrumbMenu.id=  new Array();
                breadcrumbMenu.name=  new Array();
                $menu.parents("dl").prev().each(function() {
                    breadcrumbMenu.id.unshift($(this).attr("menu-id"));
                    breadcrumbMenu.name.unshift($.trim($(this).text()));
                });
                var breadcrumnHtml = "";
                breadcrumnHtml += '<a href=""><i class="layui-icon layui-icon-home" style="font-size: 14px;color: #000;"></i> 首页</a>';
                for (var i = 0; i < breadcrumbMenu.name.length; i++) {
                    breadcrumnHtml += '<span lay-separator="">/</span>';
                    breadcrumnHtml += '<a href="javascript:;" menu-id="'+breadcrumbMenu.id[i]+'" >'+breadcrumbMenu.name[i]+'</a>';
                }
                breadcrumnHtml += '<span lay-separator="">/</span>';
                breadcrumnHtml+= '<a href="'+(!hash?"":hash)+'" menu-id="'+id+'" menu-url="'+(!hash?"":hash)+'"><cite>'+$menuName+'</cite></a>';
                $breadcrumb.html("").append(breadcrumnHtml);
                var $parent=$menu.parent("dd");
                var $parents=$parent.parent("dl").parent();
                $("#sys-menu-tree").find(".layui-nav-itemed").each(function () {
                    if(!$(this).is($parents)){
                        $(this).removeClass("layui-nav-itemed");
                    }
                });
                $("#sys-menu-tree").find(".layui-this").each(function () {
                    if(!$(this).is($parent)){
                        $(this).removeClass("layui-this");
                    }
                });
                if(!$parents.hasClass("layui-nav-itemed")){
                    $parents.addClass("layui-nav-itemed");
                }
                if(!$parent.hasClass("layui-this")){
                    $parent.addClass("layui-this");
                }
            }
        },
        ajax_loaded_page.push=function(url,isLoad){
            var l = this.urls.length;
            if(isLoad)ajax_loaded_page.urls[l]=url;
            ajax_loaded_page.$thisUrl=url;
            ajax_loaded_page.$thisIndex=l;
            $("#main-content").attr("forward",url);
            return l;
        },
        ajax_loaded_page.get=function(key){
            if(typeof key === 'string'){
                for (var k in ajax_loaded_page.urls){
                    if(ajax_loaded_page.urls[k]===key){
                        return k;
                    }
                }
                return undefined;
            }else {
                return ajax_loaded_page.urls[key];
            }
        },

        $contentArea.refresh = function(){
            $contentArea.ajaxload(ajax_loaded_page.$thisUrl,false);
        },
        $contentArea.ajaxload =function(url,isLoad){
            if(!url)return;
            //判断是否使用缓存
            //var useCache=cache.useCache("menuCache",url,new Date().getTime());
            var $menu,pushl;
            if($MB.hasHistoryApi()){
                $menu=$navigation.find("a[menu-url='"+url+"']");
                pushl=root_url+"sys/"+url;
                url=root_url + url;
                if(isLoad)
                history.pushState(null,$menu.text(),pushl);
            }else {
                $menu=$navigation.find("a[href='"+url+"']");
                url= root_url+url.replace(/^(\#\!)?\#/, '');
            }
            ajax_loaded_page.buildBreadcrumn($menu);
            $.ajax({
                url: url,
                beforeSend:function (r) {
                    if(!$MB.getLoading(1)){
                        $MB.setLoading(layer.load(0,{shade: [0.01,'#fff']}));
                    }
                },
                success: function (r) {
                    try {
                        var $r = $("<code></code>").html(r);//包装数据
                        //var $r=$(r);
                        var scripts = $r.find("div.ajax-content script");
                        $r.find("script").remove();
                        var content=$r.find("div.ajax-content");
                        if(content.length>0){
                            $contentArea.empty().html(content.html());
                        }else {
                            $contentArea.empty().html($r.html());
                        }
                        if(!!scripts&&scripts.length>0){
                            scripts.each(function(index,script){
                                if($MB.isMobile()&&script.attributes[1]){
                                    $MB.getScript(script.attributes[1].value,true);
                                }else {
                                    $MB.getScript(script.src,true);
                                }
                            })
                        }
                        //ajax_loaded_page.push(url,isLoad);
                    }catch (e) {
                        console.error("error:"+e.message+";url:"+url);
                        return true;
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    if(XMLHttpRequest.status===303){
                        layer.msg('当前未登录或登录超时，是否返回重新登录？', {
                            time: 0 //不自动关闭
                            ,btn: ['确定', '取消']
                            ,yes: function(index){
                                location.reload();
                            }
                        });
                    }else if(XMLHttpRequest.status===400){
                        $contentArea.load("/error/error.html");
                    }else if(XMLHttpRequest.status===404){
                        $contentArea.load("/error/error.html");
                    }else if(XMLHttpRequest.status===500){
                        $contentArea.load("/error/error.html");
                    }else if(XMLHttpRequest.status===503){
                        $contentArea.load("/error/error.html");
                    }else {
                        $contentArea.load("/error/error.html");
                    }
                },
                complete:function (r) {
                    if($MB.isMobile()){$(".layui-layout.layui-layout-admin").addClass("shrink")}
                    var loading = $MB.getLoading(0);
                    if (loading){
                        layer.close(loading);
                        $MB.setLoading("");
                    }
                }
            });
        };
        return $contentArea;
    };

    // 组件方法封装........
    $.fn.ajax_load.methods = {
        refresh : function($contentArea) {
            $contentArea.refresh();
        },
        loading : function($contentArea, parms) {
            $contentArea.ajaxload(parms,true);
        },
        popstate:function ($contentArea, parms) {
            $contentArea.ajaxload(parms,false);
        }
    };
    $.fn.ajax_load.defaults = {};
})(jQuery);