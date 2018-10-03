;$(document).ready(function() {
    "use strict";
    //initTreeTable();
    var element,form,laytpl,dict;
    setTimeout(function(){
        layui.use(['element', 'laytpl','form',"dict","tree"], function () {
            element = layui.element,form = layui.form,laytpl = layui.laytpl,dict=layui.dict;
            dict.load("DIC_MENU_TYPE,DIC_MENU_DATA,DIC_MENU_TREE");
            element.init();
            menuMethod.resetTree('#menuTree');
            form.render();
        });
    },100);
    $("#addBtn").on("click",function (r) {
        menuMethod.add($("#menuInfoPanle table").attr("data-name-menu"));
    });
    $("#updBtn").on("click",function (r) {
        menuMethod.update($("#menuInfoPanle table").attr("data-name-menu"));
    });
    $("#expBtn").on("click",function (r) {
        menuMethod.expMenu();
    });
    $("#delBtn").on("click",function (r) {
        menuMethod.delMenu($("#menuInfoPanle table").attr("data-name-menu"),"菜单");
    });
    $("#menuButtonPanle").on("click","i[function='del']",function (r) {
        var menuid=$(this).attr("permissionid");
        if(!menuid){
            layer.msg('获取按钮信息失败');
            return;
        }
        menuMethod.delMenu(menuid,"按钮权限");

    });
    $("#menuButtonPanle").on("click","a",function (r) {
        var menuid=$(this).attr("permissionid");
        if(!menuid){
            layer.msg('获取按钮信息失败');
            return;
        }
        menuMethod.update(menuid);

    });
    $("#menuAuthPanle").on("click","a",function (r) {
        layer.msg('还未开发相应功能');
    });
    var menuMethod =(function() {
        var menuModel ='<fieldset class="layui-elem-field layui-anim layui-anim-up"><legend>-----</legend><form class="layui-form" action=""><div class="layui-row"><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label" lable-verify="required">菜单名称:</label><div class="layui-input-block"><input type="text" name="menuName" value="{{d.menuName||\"\"}}" lay-verify="required" placeholder="请输入菜单名称"  class="layui-input"><input type="text" name="menuId" value="{{d.menuId||\"\"}}" hidden></div></div><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label" lable-verify="required">菜单类型:</label><div class="layui-input-block"><input type="radio" lay-verify="radio" name="type" value="0" title="菜单" {{#if(d.type == 0){ }}checked{{#}}}><input type="radio" lay-verify="radio" name="type" value="1" title="按钮" {{#if(d.type == 1){ }}checked{{#}}}></div></div></div><div class="layui-row"><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label">上级菜单:</label><div class="layui-input-block"><input type="text" dic-map="DIC_MENU_TREE" name="parentId" value="{{d.parentId||\"\"}}"  placeholder="请输入上级菜单"  class="layui-input dic-tree"></div></div><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label">菜单图标:</label><div class="layui-input-block"><input type="text" name="icon" value="{{d.icon||\"\"}}" placeholder="请输入菜单图标"  class="layui-input"></div></div></div><div class="layui-row"><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label">权限描述:</label><div class="layui-input-block"><input type="text" name="perms" value="{{d.perms||\"\"}}" placeholder="请输入权限描述"  class="layui-input"></div></div><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label">菜单地址:</label><div class="layui-input-block"><input type="text" name="url" value="{{d.url||\"\"}}" placeholder="请输入菜单地址"  class="layui-input"></div></div></div><div class="layui-row"><div class="layui-col-md6 layui-col-xs12"><label class="layui-form-label">菜单排序:</label><div class="layui-input-block"><input type="text" name="orderNum" value="{{d.orderNum||\"\"}}" placeholder="请输入菜单排序"  class="layui-input"></div></div><div class="layui-col-md6 layui-col-xs12"><div class="layui-input-block"></div></div></div></form></fieldset>';
        var loadModel=function(data,title,url){
            var openIndex=0;
            try{
                laytpl(menuModel).render(data, function(html){
                    //页面层
                    openIndex=layer.open({
                        title:title,
                        type: 1,
                        skin: 'layui-layer-rim m-layer-visible', //加上边框
                        area: ['640px', '300px'], //宽高
                        content: html,
                        btn: ['保存', '关闭'],
                        btnAlign: 'c',
                        yes: function(index, layero){
                            return false;
                        },
                        success:function (layero,index) {
                            layero.addClass("layui-form");
                            dict.render();
                            layero.find(".layui-layer-btn0").attr("lay-filter","form-verify").attr("lay-submit","");
                            menuMethod.onsubmit(layero.find(".layui-layer-btn0"),layero,url,function () {
                                menuMethod.refresh($("#menuInfoPanle table").attr("data-name-menu"));
                            });
                            $MB.verify(form);
                            form.render();
                        }
                    });
                });
            }catch (e){
                layer.close(openIndex);
                layer.msg('请求数据异常：'+e.message,{skin: 'mb-warn'});
            }
        };
        return {
            add:function(menuId){
                if(!menuId)menuId="";
                loadModel({parentId:menuId},"菜单新增",ctx + "menu/add");
            },
            update:function(menuId){
                if(!menuId){
                    layer.msg('请先选择你想修改的菜单！');
                    return false;
                }
                try{
                    $MB.layerGet({url:ctx + "menu/getMenu",data:{"menuId": menuId}},function (data) {
                        if(!data||!data.msg||data.code != 0){
                            layer.msg('请求数据失败,您选择的菜单不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg,"菜单修改",ctx + "menu/update");
                    });
                }catch(e) {
                    layer.msg('请求数据异常：'+e.message);
                }
            },
            delMenu:function(menuId,name){
                if(!menuId){
                    layer.msg('请先选择你想删除的'+name+'！');
                    return false;
                }
                layer.msg('你确定要删除该'+name+'吗？', {
                    time: 0 //不自动关闭
                    ,btn: ['确定', '取消']
                    ,yes: function(index){
                        layer.close(index);
                        $MB.layerPost({url:ctx + "menu/delete",data:{"ids": menuId},cache:false},function (data) {
                            layer.msg(data.msg);
                            menuMethod.resetTree('#menuTree');
                            menuMethod.refresh($("#menuInfoPanle table").attr("data-name-menu"));
                        });
                    }
                });
            },
            expMenu:function(){
                $MB.layerPost({url:"/menu/excel",data:{}}, function (r) {
                    if (r.code == 0) {
                        window.location.href = $MB.getRootPath()+"/common/download?fileName=" + r.msg + "&delete=" + true;
                    } else {
                        layer.msg(r.msg);
                    }
                });
            },
            refresh:function (menuId) {
                $MB.layerGet({url:ctx+"toolkit/compent/menu/menuInfo.html",cache:true},function(text){
                    var $compent=$("<code></code>").html(text);
                    $MB.layerGet({url:ctx+"menu/getMenuDetail",data:{menuId:menuId}},function(data){
                        laytpl($compent.find("#layui-table-menu").html()).render($.extend({},data.msg.menu), function(html){
                            $("#menuInfoPanle").html(html);
                            dict.render();
                        });
                        laytpl($compent.find("#layui-breadcrumb-permission").html()).render($.extend({},data.msg.permissions), function(html){
                            $("#menuButtonPanle").html(html);
                        });
                        laytpl($compent.find("#layui-breadcrumb-role").html()).render($.extend({},data.msg.roles), function(html){
                            $("#menuAuthPanle").html(html);
                        });
                        element.init();
                    });
                });
            },
            onsubmit:function (subBtn,layero,url,callback) {
                form.on("submit(form-verify)", function (data) {
                    if (!!subBtn.attr("sub")) {
                        layer.msg("不能重复提交！");
                        return false;
                    }
                    subBtn.attr("sub", true);
                    $MB.layerPost({url: url, data: layero.find("form").serialize()}, function (r) {
                        if (r.code == 0) {
                            layer.msg(r.msg);
                            menuMethod.resetTree('#menuTree');
                            callback();
                        } else {
                            layer.msg(r.msg);
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            },
            resetTree:function(id){
                $MB.layerGet({url:ctx+"menu/tree", cache:true},function (data) {
                    var nodes=$.extend([], data.msg.children);
                   $(id).empty();
                    layui.tree({
                        elem: id
                        ,nodes:nodes
                        ,click: function(node){
                            menuMethod.refresh(node.id);
                            if($MB.isMobile())
                                $("body .layui-body").animate({scrollTop: $("#menuInfoPanle").parents(".site-tips").offset().top }, {duration: 500,easing: "swing"});
                        }
                    });
                });
            }
        }
    })(jQuery);
});