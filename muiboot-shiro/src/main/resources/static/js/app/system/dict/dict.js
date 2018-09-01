;$(document).ready(function() {
    "use strict";
    //initTreeTable();
    var element,form,laytpl,dicutils;
    layui.use(['element', 'laytpl','form','dicutils'], function () {
        element = layui.element,form = layui.form,laytpl = layui.laytpl,dicutils=layui.dicutils;
        element.init();
        dicutils.load("dicType,yesOrNo,menuType");
        form.render();
    });
    setTimeout(function(){
        method.resetTree();
    },100);
    $("#addBtn").on("click",function (r) {
        method.add($("#dicInfoPanle table").attr("data-name-dic"));
    });
    $("#updBtn").on("click",function (r) {
        method.update($("#dicInfoPanle table").attr("data-name-dic"));
    });
    $("#expBtn").on("click",function (r) {
        method.expMenu();
    });
    $("#search_input_span").on("click",function (r) {
        method.resetTree();
    });
    $("#delBtn").on("click",function (r) {
        method.delMenu($("#menuInfoPanle table").attr("data-name-menu"),"菜单");
    });
    $("#menuButtonPanle").on("click","i[function='del']",function (r) {
        var menuid=$(this).attr("permissionid");
        if(!menuid){
            layer.msg('获取按钮信息失败');
            return;
        }
        method.delMenu(menuid,"按钮权限");

    });
    $("#menuButtonPanle").on("click","a",function (r) {
        var menuid=$(this).attr("permissionid");
        if(!menuid){
            layer.msg('获取按钮信息失败');
            return;
        }
        method.update(menuid);

    });
    $("#menuAuthPanle").on("click","a",function (r) {
        layer.msg('还未开发相应功能');
    });
    var method =(function() {
        var menuModel = "";
        $MB.layerGet({url:ctx+"model/dic/add.html",cache:true},function(text){
            menuModel=text;
        });
        var loadModel=function(data,title,url){
            try{
                laytpl(menuModel).render(data, function(html){
                    //页面层
                    layer.open({
                        title:title,
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['640px', '400px'], //宽高
                        content: html,
                        btn: ['保存', '关闭'],
                        btnAlign: 'c',
                        yes: function(index, layero){
                            return false;
                        },
                        success:function (layero,index) {
                            layero.addClass("layui-form");
                            dicutils.render();
                            layero.find(".layui-layer-btn0").attr("lay-filter","form-verify").attr("lay-submit","");
                            method.onsubmit(layero.find(".layui-layer-btn0"),layero,url,function () {
                                method.refresh($("#menuInfoPanle table").attr("data-name-menu"));
                            });
                            form.on('select(dicType)', function(data){
                                var sel = data.value;
                                if("SIMPLE"===sel){
                                    layero.find("[name='content']").attr("lay-verify","required").parents(".layui-row").show(300);
                                    layero.find("[name='sqlContent']").removeAttr("lay-verify").parents(".layui-row").hide(200);
                                }else if("SQLDIC"===sel){
                                    layero.find("[name='content']").removeAttr("lay-verify").parents(".layui-row").hide(200);
                                    layero.find("[name='sqlContent']").attr("lay-verify","required").parents(".layui-row").show(300);
                                }else if("TREEDIC"===sel){
                                    layero.find("[name='content']").removeAttr("lay-verify").parents(".layui-row").hide(200);
                                    layero.find("[name='sqlContent']").attr("lay-verify","required").parents(".layui-row").show(300);
                                }else {
                                    layero.find("[name='content']").attr("lay-verify","required").parents(".layui-row").show(300);
                                    layero.find("[name='sqlContent']").removeAttr("lay-verify").parents(".layui-row").hide(200);
                                }
                            });
                            $MB.verify(form);
                            form.render();
                        }
                    });
                });
            }catch (e){
                layer.msg('请求数据异常：'+e.message);
            }
        };
        return {
            add:function(dictId){
                if(!dictId)dictId="";
                loadModel({parentId:dictId,dicType:'SIMPLE',valid:'1'},"新增字典",ctx + "dict/add");
            },
            update:function(dicId){
                if(!dicId){
                    layer.msg('请先选择你想修改的字典！');
                    return false;
                }
                try{
                    $MB.layerGet({url:ctx + "dict/getDic",data:{"dicId": dicId}},function (data) {
                        if(!data||!data.msg||data.code != 0){
                            layer.msg('请求数据失败,您选择的字典不存在');
                            return false;
                        }
                        loadModel(data.msg,"字典修改",ctx + "dict/update");
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
                            method.resetTree();
                            method.refresh($("#menuInfoPanle table").attr("data-name-menu"));
                        });
                    }
                });
            },
            expMenu:function(){
                $MB.layerPost({url: ctx+"menu/excel",data:{}}, function (r) {
                    if (r.code == 0) {
                        window.location.href = "common/download?fileName=" + r.msg + "&delete=" + true;
                    } else {
                        layer.msg(r.msg);
                    }
                });
            },
            refresh:function (dicId) {
                $MB.layerGet({url:ctx+"model/dic.html",cache:true},function(text){
                    var $compent=$("<code></code>").html(text);
                    $MB.layerGet({url:ctx+"dict/getDicDetail",data:{dicId:dicId}},function(data){
                        laytpl($compent.find("#layui-table-dic-info").html()).render($.extend({},data.msg.info), function(html){
                            $("#dicInfoPanle").html(html);
                        });
                        laytpl($compent.find("#layui-table-dic-list").html()).render($.extend({},data.msg.list), function(html){
                            $("#dicListPanle").html(html);
                            if(data.msg.list&&data.msg.list.children){
                                var nodes=$.extend([], data.msg.list.children);
                                layui.tree({elem: '#dicListPanle .layui-tree',nodes:nodes});
                            }
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
                            method.resetTree();
                            callback();
                        } else {
                            layer.msg(r.msg);
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            },
            resetTree:function(){
                var dicName =$("#search_input_dic").val();
                var data = {dicName:dicName};
                $MB.layerGet({url:ctx+"dict/tree",data:data,cache:false},function (data) {
                    var nodes=$.extend([], data.msg.children);
                   $("#dicTree").empty();
                    layui.tree({
                        elem: '#dicTree'
                        ,nodes:nodes
                        ,click: function(node){
                            method.refresh(node.id);
                        }
                    });
                });
            }
        }
    })(jQuery);
});