;$(document).ready(function() {
    "use strict";
    //initTreeTable();
    var element,form,laytpl,dict;
    layui.use(['element', 'laytpl','form','dict'], function () {
        element = layui.element,form = layui.form,laytpl = layui.laytpl,dict=layui.dict;
        element.init();
        dict.load("dicDeptLevel,dicDeptFirstTree,disableDic,dicDeptFirstTable");
        form.render();
    });
    setTimeout(function(){
        method.resetTree();
    },100);
    $("#addBtn").on("click",function (r) {
        method.add($("#InfoPanle table").attr("data-name-id"));
    });
    $("#updBtn").on("click",function (r) {
        method.update($("#InfoPanle table").attr("data-name-id"));
    });
    $("#expBtn").on("click",function (r) {
        method.exp();
    });
    $("#search_input_span").on("click",function (r) {
        method.resetTree();
    });
    $("#delBtn").on("click",function (r) {
        method.del($("#InfoPanle table").attr("data-name-id"),"部门");
    });
    var method =(function() {
        var model = "";
        $MB.layerGet({url:ctx+"model/dept/add.html",cache:true},function(text){
            model=text;
        });
        var loadModel=function(data,title,url){
            var openIndex=0;
            try{
                laytpl(model).render(data, function(html){
                    //页面层
                    openIndex=layer.open({
                        title:title,
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['640px', '350px'], //宽高
                        content: html,
                        btn: ['保存', '关闭'],
                        btnAlign: 'c',
                        yes: function(index, layero){
                            return false;
                        },
                        success:function (layero,index) {
                            dict.render();
                            layero.addClass("layui-form");
                            layero.find(".layui-layer-btn0").attr("lay-filter","form-verify").attr("lay-submit","");
                            method.onsubmit(layero.find(".layui-layer-btn0"),layero,url,function () {
                                method.refresh($("#InfoPanle table").attr("data-name-id"));
                            });
                            $MB.verify(form);
                            form.render();
                        }
                    });
                });
            }catch (e){
                console.error(e);
                layer.msg('请求数据异常：'+e.message);
            }
        };
        return {
            add:function($id){
                if(!$id)$id="";
                loadModel({parentId:$id,valid:'1'},"新增部门",ctx + "dept/add");
            },
            update:function($id){
                if(!$id){
                    layer.msg('请先选择你想修改的字典！');
                    return false;
                }
                try{
                    $MB.layerGet({url:ctx + "dept/getDept",data:{"deptId": $id}},function (data) {
                        if(!data||!data.msg||data.code != 0){
                            layer.msg('请求数据失败,您选择的部门不存在');
                            return false;
                        }
                        loadModel(data.msg,"部门修改",ctx + "dept/update");
                    });
                }catch(e) {
                    layer.msg('请求数据异常：'+e.message);
                }
            },
            del:function($id,name){
                if(!$id){
                    layer.msg('请先选择你想删除的'+name+'！');
                    return false;
                }
                layer.msg('你确定要删除该'+name+'吗？', {
                    time: 0 //不自动关闭
                    ,btn: ['确定', '取消']
                    ,yes: function(index){
                        layer.close(index);
                        $MB.layerPost({url:"/dept/delete",data:{"ids": $id},cache:false},function (data) {
                            layer.msg(data.msg);
                            method.resetTree();
                        });
                    }
                });
            },
            exp:function(){
                $MB.layerPost({url:"/dept/excel",data:{}}, function (r) {
                    if (r.code == 0) {
                        window.location.href = $MB.getRootPath() + "/common/download?fileName=" + r.msg + "&delete=" + true;
                    } else {
                        layer.msg(r.msg);
                    }
                });
            },
            refresh:function ($id) {
                $MB.layerGet({url:ctx+"model/dept.html",cache:true},function(text){
                    var $compent=$("<code></code>").html(text);
                    $MB.layerGet({url:ctx+"dept/getDeptDetail",data:{deptId:$id}},function(data){
                        laytpl($compent.find("#layui-table-info").html()).render($.extend({},data.msg.info), function(html){
                            $("#InfoPanle").html(html);
                        });
                        laytpl($compent.find("#layui-table-list").html()).render($.extend({},data.msg.list), function(html){
                            $("#ListPanle").html(html);
                        });
                        dict.render();
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
                var deptName =$("#search_input").val();
                var data = {deptName:deptName};
                $MB.layerGet({url:ctx+"dept/tree",data:data},function (data) {
                    var nodes=$.extend([], data.msg.children);
                    if(nodes.length>0&&nodes[0].children.length>0){
                        nodes[0].spread=true;
                    }
                    $("#Tree").empty();
                    layui.tree({
                        elem: '#Tree'
                        ,nodes:nodes
                        ,click: function(node){
                            method.refresh(node.id);
                            if($MB.isMobile())
                                $("body .layui-body").animate({scrollTop: $("#InfoPanle").parents(".site-tips").offset().top }, {duration: 500,easing: "swing"});
                        }
                    });
                });
            }
        }
    })(jQuery);
});