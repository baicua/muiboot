;$(document).ready(function() {
    "use strict";
    var $maincontent=$("#main-content");
    //1.如果是移动端，添加触屏关闭菜单事件
    if($MB.isMobile()||$MB.isXsScreen()){
        $(".layui-layout.layui-layout-admin").addClass("shrink");
        $("body").on("touchend",".layui-body",function (e) {
            $(".layui-layout.layui-layout-admin").addClass("shrink");
        });
    }
    //2.初始化字典,菜单插件
    layui.extend({
        dict: '/toolkit/muiboot/js/ajax-load-dict',
        menu: '/toolkit/muiboot/js/ajax-load-menu'
    });
    //3.hash监听事件
    if(!$MB.hasHistoryApi()){
        window.onhashchange=function (e) {
            var hash =window.location.hash;
            if(!hash)return;
            $maincontent.ajax_load("loading",hash);
        }
    }else {
        $("body").on("click","a[menu-id]",function () {
            var $this = $(this);
            var id =$this.attr("menu-id");
            var $thisa=$("#navigation").find("a[menu-id='"+id+"']");
            var url =$thisa.attr("menu-url");
            if(!!url){
                $maincontent.ajax_load("loading",url);
            }
        });
        $(window).on("popstate",function(){
            var href=window.location.href;
            var url=href&&href.substring(href.indexOf("sys")+4);
            $maincontent.ajax_load("popstate",url);
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
