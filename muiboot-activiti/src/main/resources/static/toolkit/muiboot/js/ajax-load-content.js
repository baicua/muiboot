/**
 * 主区域ajax修改
 */
;(function($ , undefined) {
    "use strict";
    var root_url = $MB.getRootPath();
    function MbAjax(contentArea, options,$url) {
        var $contentArea = $(contentArea);
        this.loading = function(parms) {
            ajaxload(parms,true);
        };
        this.popstate = function(parms) {
            ajaxload(parms,false);
        };
        function ajaxload(url,isLoad){
            var $navigation=$("#navigation");
            var $menu,pushl;
            if(!url)return;
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
            updateBreadcrumbs($menu);
            $.ajax({
                url: url,
                beforeSend:function (r) {
                    if(!$MB.getLoading(1)){
                        $MB.setLoading(layer.load(3,{shade: [0.01,'#fff']}));
                    }
                },
                success: function (r) {
                    try {
                        var $r = $("<code></code>").html(r);//包装数据
                       // var scripts = $r.find("div.ajax-content script");
                        //$r.find("script").remove();
                        var content=$r.find("div.ajax-content");
                        if(content.length>0){
                            $contentArea.empty().html(content.html());
                        }else {
                            $contentArea.empty().html($r.html());
                        }
/*                        if(!!scripts&&scripts.length>0){
                            scripts.each(function(index,script){
                                if($MB.isMobile()&&script.attributes[1]){
                                    $MB.getScript(script.attributes[1].value,true);
                                }else {
                                    $MB.getScript(script.src,true);
                                }
                            });
                        }*/
                        content=null;
                        $r=null;
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
                        $contentArea.load("/error/500.html");
                    }else if(XMLHttpRequest.status===503){
                        $contentArea.load("/error/error.html");
                    }else {
                        $contentArea.load("/error/error.html");
                    }
                },
                complete:function (r) {
                    var loading = $MB.getLoading(0);
                    if (loading){
                        layer.close(loading);
                        $MB.setLoading("");
                    }
                }
            });
        };
        function updateBreadcrumbs($menu){
            var hash=$menu.attr("href");
            var $breadcrumb = $(".breadcrumb");
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
                var breadcrumnArray=new Array();
                breadcrumnArray.push('<a href="/sys"> 首页</a>');
                for (var i = 0; i < breadcrumbMenu.name.length; i++) {
                    breadcrumnArray.push('<span lay-separator="">/</span>');
                    breadcrumnArray.push('<a href="javascript:;" menu-id="');
                    breadcrumnArray.push(breadcrumbMenu.id[i]);
                    breadcrumnArray.push('" >');
                    breadcrumnArray.push(breadcrumbMenu.name[i]);
                    breadcrumnArray.push('</a>');
                }
                breadcrumnArray.push('<span lay-separator="">/</span>');
                breadcrumnArray.push('<a href="');
                breadcrumnArray.push(!hash?"":hash);
                breadcrumnArray.push('" menu-id="');
                breadcrumnArray.push(id);
                breadcrumnArray.push('" menu-url="');
                breadcrumnArray.push(!hash?"":hash);
                breadcrumnArray.push('"><cite>');
                breadcrumnArray.push($menuName);
                breadcrumnArray.push('</cite></a>');
                $breadcrumb.empty().append(breadcrumnArray.join(""));
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
                hash=breadcrumnArray=$breadcrumb=$parent=$parent=null;
            }
        }
    }//ajaxload

    $.fn.ajaxLoad = $.fn.ajax_load = function (option,$url) {
        var method_call;
        var $this = $(this);
        var data = $this.data('ajax_load');
        if (!data) $this.data('ajax_load', (data = new MbAjax(this, option,$url)));
        method_call = data[option];
        return (method_call === undefined) ? null : method_call($url);
    }
})(jQuery);