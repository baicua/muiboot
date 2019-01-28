;layui.use(['element', 'laytpl','form',"dict","tree",'layer'], function () {
    "use strict";
    var element,form,laytpl,dict,layer;
    element = layui.element,form = layui.form,laytpl = layui.laytpl,dict=layui.dict,layer=layui.layer;
    dict.load("DIC_MENU_TYPE,DIC_MENU_DATA,DIC_MENU_TREE");
    element.init();
    var menuMethod =(function() {
        var loadModel=function(data,title,url){
            var openIndex=0;
            try{
                laytpl($("#addMenu").html()).render(data, function(html){
                    //页面层
                    openIndex=layer.open({
                        title:'<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
                        type: 1,
                        skin: 'mb-layer-rim', //加上边框
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
                    layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
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
                        $MB.layerPost({url:ctx + "menu/delete",data:{"ids": menuId},cache:false},function (r) {
                            if (r.code == 0) {
                                layer.msg(r.msg);
                                menuMethod.resetTree('#menuTree');
                                menuMethod.refresh($("#menuInfoPanle table").attr("data-name-menu"));
                            } else {
                                layer.msg(r.msg,{skin: 'mb-warn'});
                            }
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
                $MB.layerGet({url:ctx+"menu/getMenuDetail",data:{menuId:menuId},cache:true},function(data){
                    laytpl($("#table").html()).render($.extend({},data.msg.menu), function(html){
                        $("#menuInfoPanle").html(html);
                        dict.render();
                    });
                    laytpl($("#permission").html()).render($.extend({},data.msg.permissions), function(html){
                        $("#menuButtonPanle").html(html);
                    });
                    laytpl($("#role").html()).render($.extend({},data.msg.roles), function(html){
                        $("#menuAuthPanle").html(html);
                    });
                    element.init();
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
                            layer.msg(r.msg,{skin: 'mb-warn'});
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            },
            resetTree:function(id,noloading){
                $MB.layerGet({url:ctx+"menu/tree", cache:true,noloading:noloading},function (data) {
                    var nodes=$.extend([], data.msg.children);
                    $(id).empty();
                    layui.tree({
                        elem: id
                        ,nodes:nodes
                        ,click: function(node){
                            menuMethod.refresh(node.id);
                        }
                    });
                });
            }
        }
    })(jQuery);
    menuMethod.resetTree('#menuTree',true);
    //form.render();
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
});