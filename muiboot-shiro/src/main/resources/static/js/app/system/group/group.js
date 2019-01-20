;layui.use(['element', 'laytpl','form','dict','layer'], function () {
    "use strict";
    var element,form,laytpl,dict;
    element = layui.element,form = layui.form,laytpl = layui.laytpl,dict=layui.dict;
    element.init();
    dict.load("DIC_GROUP_TYPE,DIC_ORGAN_TREE,DIC_DISABLE,DIC_ORGAN_TABLE");
    form.render();
    setTimeout(function(){
        method.resetTree(true);
    },100);
    $("#addBtn").on("click",function (r) {
        method.add("");
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
        function loadModel(data,title,url){
            var openIndex=0;
            try{
                laytpl($("#groupAdd").html()).render(data, function(html){
                    //页面层
                    openIndex=layer.open({
                        title:'<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
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
                layer.msg('请求数据异常：'+e.message,{skin: 'mb-warn'});
            }
        };
        return {
            add:function($id){
                if(!$id)$id="";
                loadModel({parentId:$id,valid:'1'},"新增组织机构",ctx + "group/add");
            },
            update:function($id){
                if(!$id){
                    layer.msg('请先选择你想修改的组织机构！');
                    return false;
                }
                try{
                    $MB.layerGet({url:ctx + "group/getGroup",data:{"groupId": $id}},function (data) {
                        if(!data||!data.msg||data.code != 0){
                            layer.msg('请求数据失败,您选择的组织机构不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg,"修改组织机构",ctx + "group/update");
                    });
                }catch(e) {
                    layer.msg('请求数据异常：'+e.message,{skin: 'mb-warn'});
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
                        $MB.layerPost({url:"/group/delete",data:{"ids": $id},cache:false},function (r) {
                            if (r.code == 0) {
                                layer.msg(r.msg);
                                method.resetTree();
                            } else {
                                layer.msg(r.msg,{skin: 'mb-warn'});
                            }
                        });
                    }
                });
            },
            exp:function(){
                $MB.layerPost({url:"/group/excel",data:{}}, function (r) {
                    if (r.code == 0) {
                        window.location.href ="/common/download?fileName=" + r.msg + "&delete=" + true;
                    } else {
                        layer.msg(r.msg);
                    }
                });
            },
            refresh:function ($id) {
                $MB.layerGet({url:ctx+"group/getGroupDetail",data:{groupId:$id}},function(data){
                    laytpl($("#groupInfo").html()).render($.extend({},data.msg.info), function(html){
                        $("#InfoPanle").html(html);
                    });
                    laytpl("<div></div>").render($.extend({},data.msg.list), function(html){
                        $("#ListPanle").html(html);
                    });
                    dict.render();
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
                            method.resetTree(false);
                            callback();
                        } else {
                            layer.msg(r.msg,{skin: 'mb-warn'});
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            },
            resetTree:function(noloading){
                var groupName =$("#search_input").val();
                var data = {groupName:groupName};
                $MB.layerGet({url:ctx+"group/tree",data:data,noloading:noloading},function (data) {
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
                        }
                    });
                });
            }
        }
    })(jQuery);
});