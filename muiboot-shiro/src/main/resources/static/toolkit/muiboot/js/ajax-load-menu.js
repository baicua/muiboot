/**
 * 菜单工具
 */
;layui.define(['jquery','element',"mrouter"], function (exports) {
    "use strict";
    var $ = layui.jquery;
    var element=layui.element;
    var router=layui.mrouter;
    var url="/session/getUserMenu",has=$MB.hasHistoryApi();
    function loadmenu(usename) {
        var $menu=$("#sys-menu-tree");
        $MB.layerGet({url:url,data:{ "userName": usename},cache:true,noloading:true},function (r) {
            if (r.code == 0) {
                var data = r.msg;
                $menu.data("model",data.model);
                $menu.empty();
                if(!data||!data.children||data.children.length<1){//没有任何权限
                    element.tabAdd('mb-pane-top', {
                        title: '提示'
                        ,content: '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> <html> <head> <meta charset="utf-8"> <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> <meta http-equiv="X-UA-Compatible" content="edge"/> <title>MUIBOOT 权限管理系统</title> <link href="../toolkit/theme/css/theme.css" rel="stylesheet" type="text/css"/> </head> <body style="background:#edf6fa;"> <div class="error404"> <h2>非常遗憾，您暂时没有授予该系统任何权限！</h2> <p>请联系管理员，给您授予相应的业务权限。</p> <p>联系电话：15111111111</p> <p>微信：15111111111</p> <p>邮箱：15111111111@baicua.com</p> <p>我们会尽快处理您的申请！</p> </div> </body> </html>' //支持传入html
                        ,id: 'error'
                    });
                    return false;
                }
                $menu.append(forTree(data.children));
                element.render("nav", "menu");
                //element.render("nav", "admin-pagetabs-nav");
                element.on('nav(menu)', function(elem){
                    var menuUrl=elem.attr("menu-url");
                    if(!!menuUrl){
                        router.jump(menuUrl);
                        //router.router(menuId,menuUrl,menuName);
                    }
                });
                router.bindMenu($menu);
                $(".loading-shade").addClass("loaded");
                if(routerUrl){
                    router.jump(routerUrl);
                    //router.jump(routerUrl);
                }
            } else {
                layer.msg('获取菜单失败！',{skin: 'mb-warn'});
            }
        });
    }
    function forTree(o) {
        var str="";
        if(!o||o.length===0)return "";
        str+='';
        for (var i = 0; i < o.length; i++) {
            var urlstr = '<li class="layui-nav-item">';
            try {
                urlstr+=['<a href="javascript:;"lay-id="'+o[i]["id"]+'" menu-url="'+(o[i]["href"]||"")+'" menu-name="'+(o[i]["name"]||"")+'">',
                    '<i class="'+(o[i]["icon"]||"")+'"></i>',
                    '<span >&nbsp;&nbsp;'+ o[i]["name"] +'</span>',
                    '</a>'
                ].join("");
                //str += urlstr;
                if (!!o[i]["children"]&&o[i]["children"].length != 0) {
                    urlstr+='<dl class="layui-nav-child">';
                    urlstr+=forChildrenTree(o[i]["children"]);
                    urlstr+='</dl>';
                }
                router.register(o[i]["href"]);
            } catch (e) {
                console.log(e);
            }
            urlstr += '</li>';
            str+=urlstr;
        }
        return str;
    };
    function forChildrenTree(o) {
        if(!o||o.length===0)return "";
        var restr="";
        for (var i = 0; i < o.length; i++) {
            var urlstr = '<dd>',href;
            try {
                urlstr+=['<a href="javascript:;" lay-id="'+o[i]["id"]+'" menu-url="'+(o[i]["href"]||"")+'" menu-name="'+(o[i]["name"]||"")+'">',
                    '<i class="'+(o[i]["icon"]||"")+'"></i>',
                    '<span >&nbsp;&nbsp;'+ o[i]["name"] +'</span>',
                    '</a>'
                ].join("");
                if (!!o[i]["children"]&&o[i]["children"].length != 0) {
                    urlstr+='<dl class="layui-nav-child">';
                    urlstr+=forChildrenTree(o[i]["children"]);
                    urlstr+='</dl>';
                }
                urlstr += '</dd>';
                router.register(o[i]["href"]);
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