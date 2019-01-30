;layui.use(['element', 'laytpl','form','dict','layer'], function () {
    "use strict";
    var element,form,laytpl,dict;
    element = layui.element,form = layui.form,laytpl = layui.laytpl,dict=layui.dict;
    element.init();
    dict.load("DIC_DIC_TYPE,DIC_YESORNO,DIC_VALID,DIC_CATEGORY_TREE,DIC_CATEGORY_TABLE");
    form.render();
    var method =(function() {
        var loadModel=function(data,title,url){
            var openIndex=0;
            try{
                laytpl($("#dicAdd").html()).render(data, function(html){
                    //页面层
                    openIndex=layer.open({
                        title:'<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
                        type: 1,
                        skin: 'mb-layer-rim', //加上边框
                        area: ['640px', '400px'], //宽高
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
                            method.onsubmit(layero.find(".layui-layer-btn0"),layero,url,function () {
                                method.refresh($("#dicInfoPanle table").attr("data-name-dic"));
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
                layer.close(openIndex);
                layer.msg('请求数据异常：'+e.message,{skin: 'mb-warn'});
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
                            layer.msg('请求数据失败,您选择的字典不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg,"字典修改",ctx + "dict/update");
                    });
                }catch(e) {
                    layer.msg('请求数据异常：'+e.message);
                }
            },
            del:function(dicId,name){
                if(!dicId){
                    layer.msg('请先选择你想删除的'+name+'！');
                    return false;
                }
                layer.msg('你确定要删除该'+name+'吗？', {
                    time: 0 //不自动关闭
                    ,btn: ['确定', '取消']
                    ,yes: function(index){
                        layer.close(index);
                        $MB.layerPost({url:"/dict/delete",data:{"ids": dicId},cache:false},function (r) {
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
                $MB.layerPost({url: "/dict/excel",data:{}}, function (r) {
                    if (r.code == 0) {
                        window.location.href ="/common/download?fileName=" + r.msg + "&delete=" + true;
                    } else {
                        layer.msg(r.msg);
                    }
                });
            },
            refresh:function (dicId) {
                $MB.layerGet({url:ctx+"dict/getDicDetail",data:{dicId:dicId}},function(data){
                    laytpl($("#dicInfo").html()).render($.extend({},data.msg.info), function(html){
                        $("#dicInfoPanle").html(html);
                    });
                    laytpl($("#dicList").html()).render($.extend({},data.msg.list), function(html){
                        $("#dicListPanle").html(html);
                        if(data.msg.list&&data.msg.list.children){
                            var nodes=$.extend([], data.msg.list.children);
                            layui.tree({elem: '#dicListPanle .layui-tree',nodes:nodes});
                        }
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
                            method.resetTree();
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
                var dicName =$("#search_input_dic").val();
                var data = {dicName:dicName};
                $MB.layerGet({url:ctx+"dict/tree",data:data,cache:false,noloading:noloading},function (data) {
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
    method.resetTree(true);
    $("#addBtn").on("click",function (r) {
        method.add($("#dicInfoPanle table").attr("data-name-dic"));
    });
    $("#updBtn").on("click",function (r) {
        method.update($("#dicInfoPanle table").attr("data-name-dic"));
    });
    $("#expBtn").on("click",function (r) {
        method.exp();
    });
    $("#search_input_span").on("click",function (r) {
        method.resetTree();
    });
    $("#delBtn").on("click",function (r) {
        method.del($("#dicInfoPanle table").attr("data-name-dic"),"字典");
    });
});