(function($) {
    "use strict";
    var ajax_loaded_page = {urls:{}};//ajax加载的页面
    var root_url = document.location.pathname.substring(0, document.location.pathname.substr(1).indexOf('/') + 1);
    var $contentArea;
    var cache={
        sessionCache:{},
         useCache: function(url,millisecond){
                if(this.sessionCache[url])return true;
                this.sessionCache[url]=true;
                if(!window.localStorage)return false;
                var storage=window.localStorage;
                var data=$.extend({},JSON.parse(storage.getItem("menuCache")));
                var oldmillisecond=data[url];
                if(!oldmillisecond){
                    data[url]=millisecond;
                    storage.setItem("menuCache",JSON.stringify(data));
                    return false;
                }
                //计算出相差天数
                var days=Math.floor((millisecond-oldmillisecond)/(24*3600*1000));
                if(days<=7){
                    return true;
                }else {
                    data[url]=millisecond;
                    storage.setItem("menuCache",JSON.stringify(data));
                    return false;
                }
            }
    }
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

        ajax_loaded_page.push=function(url,isLoad){
            var l = this.urls.length;
            if(isLoad)ajax_loaded_page.urls[l]=url;
            ajax_loaded_page.$thisUrl=url;
            ajax_loaded_page.$thisIndex=l;
            $main_content.attr("ajax-href",url);
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
            //判断是否使用缓存
            var useCache=cache.useCache(url,new Date().getTime());
            $.ajax({
                url: root_url + url,
                cache: useCache,
                beforeSend:function (r) {
                    layer.load(2,{shade: [0.5,'#fff']});
                },
                success: function (r) {
                    if (r.indexOf('账户登录') != -1) {
                        location.href = location;
                        return;
                    }
                    try {
                        var $r = $("<code></code>").html(r);//包装数据
                        //var $r=$(r);
                        var scripts = $r.find("div.ajax-content script");
                        $r.find("script").remove();
                        var content=$r.find("div.ajax-content");
                        if(content.length>0){
                            $main_content.empty().html(content.html());
                        }else {
                            $main_content.empty().html($r.html());
                        }
                        if(!!scripts&&scripts.length>0)
                            $main_content.append(scripts);
                        ajax_loaded_page.push(url,isLoad);
                    }catch (e) {
                        console.error("error:"+e.message+";url:"+url);
                        return true;
                    }
                },
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    if(XMLHttpRequest.status==400){
                        $main_content.load("/error/error.html");
                    }else if(XMLHttpRequest.status==404){
                        $main_content.load("/error/error.html");
                    }else if(XMLHttpRequest.status==500){
                        $main_content.load("/error/error.html");
                    }else if(XMLHttpRequest.status==503){
                        $main_content.load("/error/error.html");
                    }else {
                        $main_content.load("/error/error.html");
                    }
                },
                complete:function (r) {
                    layer.closeAll();
                }
            });
        },
        $contentArea.forward ==function(){
            var  $thisIndex=ajax_loaded_page.$thisIndex;
            if(!$thisIndex&&$thisIndex<ajax_loaded_page.urls.length){
                $contentArea.refresh(ajax_loaded_page.get($thisIndex+1),false);
            }else {
                layer.alert('当前已经是最后一页！', {icon: 0});
            }
        },
        $contentArea.back ==function(){
            var  $thisIndex=ajax_loaded_page.$thisIndex;
            if(!$thisIndex||$thisIndex<=1){
                layer.alert('当前已经是第一页！', {icon: 0});
            }else {
                $contentArea.refresh(ajax_loaded_page.get($thisIndex-1),false);
            }
        }
        return $contentArea;
    };

    // 组件方法封装........
    $.fn.ajax_load.methods = {
        refresh : function($contentArea) {
            $contentArea.refresh();
        },
        loading : function($contentArea, parms) {
            if(parms){
                $contentArea.ajaxload(parms,true);
            }else{
                $contentArea.refresh();
            }
        }
    };
    $.fn.ajax_load.defaults = {};
})(jQuery);