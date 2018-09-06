;layui.define(['jquery','element'], function (exports) {
    "use strict";
    var $ = layui.jquery;
    var element=layui.element;
    var url="/session/getUserMenu",has=$MB.hasHistoryApi();
    function loadmenu(usename) {
        $MB.layerGet({url:url,data:{ "userName": usename}},function (r) {
            if (r.code == 0) {
                var $navigation=$("#navigation");
                var $maincontent=$("#main-content");
                var data = r.msg;
                $navigation.remove("ul");
                $navigation.append(forTree(data.children));
                var forward = $maincontent.attr("forward");
                if(!!forward){
                    if(has){
                        $maincontent.ajax_load("loading",forward);
                    }else {
                        window.location.href=$MB.getRootPath()+"/sys"+"#"+forward;
                    }
                }
                element.init();
            } else {
                layer.msg('获取菜单失败！', {icon: 0});
            }
        });
    }
    function forTree(o) {
        var str="";
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
    function forChildrenTree(o) {
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
    var obj = {
        loadmenu: function (usemane) {
            loadmenu(usemane);
        }
    };
    exports('menu', obj);
});