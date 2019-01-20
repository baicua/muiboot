/**
 * 路由控制
 */
;layui.define(['jquery','element','layer','table'], function (exports) {
    "use strict";
    var $ = layui.jquery;
    var element=layui.element;
    var layer=layui.layer;
    var table=layui.table;
    var root_url = $MB.getRootPath();
    var routers={};
    var NAV_TOP="mb-pane-top",MENU="sys-menu-tree",BANNER="mb-banner",MAIN_CONTENT="mb-content",BANNER_NEXT="mb-banner-next";
    var $mbtop=$("#"+NAV_TOP);
    var $menuNav=$("#"+MENU);
    var $ul=$("#"+BANNER);
    var $next=$("#"+BANNER_NEXT);
    var $maincontent=$("#"+MAIN_CONTENT);
    var model="sys";
    var $base=null;
    var DOM_CACHE={};
    var NODE=null;
    var LOADT="ajax";
    var Menu ={
        _updateWindows:function () {
            if(!this.menuUrl){
                return;
            }
            if(!routers[this.menuUrl]){
                var url=root_url + this.menuUrl;
                ajaxload(url,this.menuUrl,function (content) {
                    $base=$maincontent.find(">div.layui-show");
                    $base.empty().html(content);
                });
                return;
            }
            if(!this.menuId){
                return;
            }
            var $length_this=$ul.find("li[lay-id='"+this.menuId+"']").length;
            if($length_this===0){
                  var $length_all=$ul.find("li[lay-id]").length;
                 if($length_all>=10){
                     layer.msg("最多只能打开10个窗口",{skin: 'mb-warn'});
                     return;
                 }
                 var heigth=$maincontent.height();
                var content=LOADT==="frame"?"<iframe class='mb-frame' scrolling='no' style='height: "+heigth+"px'></iframe>":"";
                element.tabAdd('mb-pane-top', {
                    title: this.menuName
                    ,content: content //支持传入html
                    ,id: this.menuId
                });
                var url=root_url + this.menuUrl;
                //$base=$maincontent.find(">div.layui-show");
                var index=$ul.find("li[lay-id='"+this.menuId+"']").index();
                $base=$maincontent.find(">div.layui-tab-item").eq(index);
                if(LOADT==="frame"){
                    $base.find("iframe").first().attr("src",url);
                }else {
                    ajaxload(url,this.menuName,function (content) {
                        $base.empty().html(content);
                    })
                }
            }
            var $thisWindow=$mbtop.find("ul.mb-banner li.layui-this");
            if(!($thisWindow.attr("lay-id")==parseInt(this.menuId))){
                element.tabChange('mb-pane-top', this.menuId);
                var $add=$mbtop.find("li[lay-id='"+this.menuId+"']");
                var pane_l=$mbtop.offset().left;
                var next_l=$next.offset().left;
                var add_w=$add.outerWidth();
                var add_l=$add.offset().left;
                var ul_offset=$ul.offset();
                if(next_l<add_l+add_w){
                    ul_offset.left+=next_l-(add_l+add_w);
                    $ul.offset(ul_offset);
                }
                if(add_l<pane_l+30){
                    ul_offset.left+=pane_l+30-add_l;
                    $ul.offset(ul_offset);
                }
            }

        },
        _updateLocalHash:function () {
            var _thisHash=window.location.hash;
            _thisHash= _thisHash.replace(/^(\#\!)?\#/, '');
            if(!this.menuUrl||_thisHash===this.menuUrl){
                return;
            }
            if($MB.hasHistoryApi()){
                var push=root_url+model+"/"+this.menuUrl;
                history.pushState(null,this.menuName||"default",push);
            }else {
                window.location.hash="#"+this.menuUrl;
            }
        },
        _updateMenu:function () {
            if(!this.menuId){
                return;
            }
            var $menu=$menuNav.find(".layui-this");
            if(!($menu.next("a").attr("lay-id")==parseInt(this.menuId))){
                var _thisMenu=$menuNav.find("a[lay-id='"+this.menuId+"']");
                $menuNav.find(".layui-this").removeClass("layui-this");
                _thisMenu.parent().addClass("layui-this");
                _thisMenu.parents("li").addClass("layui-nav-itemed");
            }
        },
        _detach:function(){
            //var $detach=$maincontent.children(".layui-tab-item:not(.layui-show,.detach)");
            if(this.pre){
                var pre=routers[this.pre];
                var preLoadT=pre&&pre["attributes"]["loadType"]||"ajax";
                var menuId=pre&&pre["id"]||0;
                var index=$ul.find("li[lay-id='"+menuId+"']").index();
                var $detach=$maincontent.find(">div.layui-tab-item").eq(index);
                if($detach.hasClass("detach"))return;
                if(preLoadT==="ajax"){
                    DOM_CACHE[this.pre]=$detach.children().detach();
                }
                $detach.addClass("detach");
                var $this=$maincontent.find(".layui-show");
                if($this.hasClass("detach")){
                    if(LOADT==="ajax"){
                        $this.append(DOM_CACHE[this.cur]);
                        table.resize();
                    }
                    $this.removeClass("detach");
                }

            }
        }
    };
    function router(menuId,menuUrl,menuName) {
        NODE=routers[menuUrl];
        LOADT=NODE&&NODE["attributes"]["loadType"]||"ajax";
        if(menuUrl!=Menu.cur){
            Menu.pre=Menu.cur;
            Menu.cur=menuUrl;
        }
        Menu.menuId=menuId;
        Menu.menuUrl=menuUrl;
        Menu.menuName=menuName;
        //修改（添加）窗口状态
        Menu._updateWindows();
        //修改浏览器hash
        Menu._updateLocalHash();
        //修改菜单状态
        Menu._updateMenu();
        //缓存DOM
        Menu._detach();
    }
    function ajaxload(menuUrl,menuName,callback) {
        var content="页面加载失败";
        var loadindex="";
        $.ajax({
            url: menuUrl,
            dataType: "html",
            beforeSend:function (r) {
                loadindex=layer.load(4,{shade: [0.01,'#fff']})
            },
            success: function (r) {
                try {
                    content = $(r).siblings("div[lay-filter='ajax-content']");//包装数据
                } catch (e) {
                    console.error("error:" + e.message + ";url:" + menuUrl);
                    return true;
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (XMLHttpRequest.status === 303) {
                    layer.msg('当前未登录或登录超时，是否返回重新登录？', {
                        time: 0 //不自动关闭
                        , btn: ['确定', '取消']
                        , yes: function (index) {
                            location.reload();
                        }
                    });
                }
            },
            complete:function (r) {
                layer.close(loadindex);
                callback(content);
            }
        });
    };
    element.on('tab(mb-pane-top)', function(data){
        //var $this=$(this);
        var menuId=this.getAttribute("lay-id");
        var $menu=$menuNav.find("a[lay-id='"+menuId+"']");
        var menuUrl=$menu.attr("menu-url");
        if(menuId==0)menuUrl="home";
        var menuName=$menu.attr("menu-name");
        router(menuId,menuUrl,menuName);
    });
    element.on('tabDelete(mb-pane-top)', function(data){
        var menuId=$(this).parent().attr("lay-id");
        var _thisMenu=$menuNav.find("a[lay-id='"+menuId+"']");
        var _CAHCE=DOM_CACHE[_thisMenu.attr("menu-url")];
        if(_CAHCE){
            _CAHCE.remove();
        }
        delete DOM_CACHE[_thisMenu.attr("menu-url")];
    });
    var obj = {
        router: function (menuId,menuUrl,menuName) {
            router(menuId,menuUrl,menuName);
        },
        register:function (menuUrl,node) {
            if(menuUrl)routers[menuUrl]=node;
        },
        jump:function (hash) {
            NODE=routers[hash];
            var menuId,menuName;
            if(NODE){
                menuId=NODE["id"];
                menuName=NODE["name"];
                LOADT=NODE["attributes"]["loadType"]||"ajax";
            }
            router(menuId,hash,menuName);
        },
        bindMenu:function (_menu) {
            $menuNav=_menu;
            model=_menu.data("model")||model;
        }
    };
    exports('mrouter', obj);
});