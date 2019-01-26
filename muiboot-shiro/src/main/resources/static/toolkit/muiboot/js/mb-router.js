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
    var $THIS_LI=null;
    var $THIS_URL=null;
    var $THIS_SHOW=null;
    var $THIS_MENU=null;
    var ALL_COUNT=1;
    var HOME=$maincontent.children("div.layui-tab-item").eq(0);
    var CONTENT_HEIGTH=$maincontent.height();
    var Menu ={
        _addWindows:function (menuId,menuUrl,menuName) {
            if(menuId&&!!routers[menuUrl]){
                //var $length_this=$ul.find("li[lay-id='"+menuId+"']").length;
                if($THIS_LI.length===0){
                    //var $length_all=$ul.find("li[lay-id]").length;
                    if(ALL_COUNT>=10){
                        layer.msg("最多只能打开10个窗口",{skin: 'mb-warn'});
                        return false;
                    }
                    var content=LOADT==="frame"?"<iframe class='mb-frame' scrolling='no' style='height: "+CONTENT_HEIGTH+"px'></iframe>":"";
                    element.tabAdd('mb-pane-top', {
                        title: menuName
                        ,content: content //支持传入html
                        ,id: menuId
                    });
                    ALL_COUNT=$ul.children("li[lay-id]").length;
                    //var url=root_url + menuUrl;
                    //$base=$maincontent.find(">div.layui-show");
                    $THIS_LI=$ul.children("li[lay-id='"+menuId+"']");
                    var index=$THIS_LI.index();
                    $base=$maincontent.children("div.layui-tab-item").eq(index);
                    if(LOADT==="frame"){
                        $base.find("iframe").first().attr("src",$THIS_URL);
                    }else {
                        ajaxload($THIS_URL,function (content) {
                            $base.empty().append(content);
                        })
                    }
                }
            }
            $THIS_SHOW=$ul.children("li.layui-this");
            return true;
        },
        _updateWindows:function () {
            if(!this.menuUrl){
                return;
            }
            if(!routers[this.menuUrl]){
                ajaxload($THIS_URL,function (content) {
                    HOME.empty().append(content);
                });
                return;
            }
            if(!this.menuId){
                return;
            }
            //var $thisWindow=$mbtop.find("ul.mb-banner li.layui-this");
            if($THIS_SHOW&&!($THIS_SHOW.attr("lay-id")==parseInt(this.menuId))){
                element.tabChange('mb-pane-top', this.menuId);
                //var $add=$mbtop.find("li[lay-id='"+this.menuId+"']");
                var pane_l=$mbtop.offset().left;
                var next_l=$next.offset().left;
                var add_w=$THIS_LI.outerWidth();
                var add_l=$THIS_LI.offset().left;
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
            window.location.hash="#"+this.menuUrl;
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
                _thisMenu=null;
            }
            $menu=null;
        },
        _detach:function(){
            //var $detach=$maincontent.children(".layui-tab-item:not(.layui-show,.detach)");
            if(this.pre){
                var pre=routers[this.pre];
                var preLoadT=pre&&pre["attributes"]["loadType"]||"ajax";
                var menuId=pre&&pre["id"]||0;
                var index=$ul.children("li[lay-id='"+menuId+"']").index();
                var $detach=$maincontent.children("div.layui-tab-item").eq(index);
                if($detach.hasClass("detach"))return;
                if(preLoadT==="ajax"){
                    DOM_CACHE[this.pre]=$detach.children().detach();
                }
                $detach.addClass("detach");
                var $this=$maincontent.children("div.layui-tab-item.layui-show");
                if($this.hasClass("detach")){
                    if(LOADT==="ajax"){
                        $this.append(DOM_CACHE[this.cur]);
                        table.resize();
                    }else {
                        var iframe=$this.children("iframe").first();
                        if (iframe.attr("src")!=$THIS_URL){
                            $this.children("iframe").first().attr("src",$THIS_URL);
                        }
                    }
                    $this.removeClass("detach");
                }
                $this=null;
                $detach=null;
            }
        }
    };
    function router(menuId,menuUrl,menuName) {
        $THIS_LI=$ul.find("li[lay-id='"+menuId+"']");
        $THIS_MENU=$menuNav.find("a[lay-id='"+menuId+"']");
        $THIS_URL=root_url + menuUrl;
        var hash = window.location.hash.replace(/^(\#\!)?\#/, '');
        if($THIS_LI.hasClass("layui-this")&&$THIS_MENU.parent().hasClass("layui-this")&&hash==menuUrl){
            return;
        }
        if(!Menu._addWindows(menuId,menuUrl,menuName)){
            return;
        };
        NODE=routers[menuUrl];
        LOADT=NODE&&NODE["attributes"]["loadType"]||"ajax";
        if(menuUrl!=Menu.cur){
            Menu.pre=Menu.cur;
            Menu.cur=menuUrl;
        }
        Menu.menuId=menuId;
        Menu.menuUrl=menuUrl;
        Menu.menuName=menuName;
        //修改窗口状态
        Menu._updateWindows();
        //修改浏览器hash
        Menu._updateLocalHash();
        //修改菜单状态
        Menu._updateMenu();
        //缓存DOM
        Menu._detach();
    }
    function ajaxload(menuUrl,callback) {
        var content='<div style="text-align: center;"><i class="layui-icon layui-icon-404" style="font-size: 23em"></i></div>';
        var loadindex="";
        $.ajax({
            url: menuUrl,
            dataType: "html",
            beforeSend:function (r) {
                loadindex=layer.load(4,{shade: [0.6,'#e6f7ff']})
            },
            success: function (r) {
                try {
                    content = r.match(/<body(([\s\S])*?)<\/body>/g);//包装数据
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
                //fragment.innerHTML = content;
                //content=null;
                setTimeout(function () {
                    callback(content);
                },300);
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
        var url=_thisMenu.attr("menu-url");
        var _CAHCE=DOM_CACHE[url];
        if(_CAHCE){
            _CAHCE.remove();
            delete (_CAHCE[0]);
            $THIS_LI=null;
            $THIS_URL=null;
            $THIS_SHOW=null;
        }
        _CAHCE=null;
        this.parentNode.removeChild(this);
        delete(this);
        ALL_COUNT=$ul.find("li[lay-id]").length;
        delete DOM_CACHE[url];
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
            model=_menu||model;
        }
    };
    exports('mrouter', obj);
});