;layui.use(['table','layer'], function () {
    "use strict";
    var table, form, laytpl, layer;
    table = layui.table, form = layui.form, laytpl = layui.laytpl, layer = layui.layer;
    form.render();
    table.render({
        id: 'lay-model-list'
        , elem: '#modelList'
        , url: '/workflow/model/list' //数据接口
        , page: true //开启分页
        , size: 'sm'
        , height: 'full'
        , skin: "line"
        , cols: [[
            {type: 'checkbox'}
            , {field: 'id', title: '键值', hide: true}
            , {field: 'key', title: '编号'}
            , {field: 'name', title: '流程名称'}
            , {field: 'version', title: '版本'}
            , {field: 'createTime', title: '创建时间'}
            , {field: 'lastUpdateTime', title: '更新时间'}
            , {field: '', title: '操作'}
        ]]
    });
    form.on('submit(search)', function ($data) {
        var data = $data.field;
        table.reload('lay-user-list', {
            where: $.extend({}, data)//设定异步数据接口的额外参数，任意设
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(reset)', function (data) {
        form.val("search-form", {
            "organId": ""
            , "ignore-form": ""
            , "groupId": ""
            , "status": ""
            , "username": ""
            , "realName": ""
            , "mobile": ""
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    $("#addBtn").on("click", function (r) {
        method.add();
    });
    $("#updBtn").on("click", function (r) {
        method.update(table.checkStatus('lay-model-list'));
    });
    $("#deployBtn").on("click", function (r) {
        method.deploy(table.checkStatus('lay-model-list'));
    });
    $("#delBtn").on("click", function (r) {
        method.del(table.checkStatus('lay-model-list'));
    });
    var method = (function () {
        return {
            add: function () {
                var w=layer.open({
                    title: '<i class="layui-icon layui-icon-app"></i>&nbsp; 新增模型',
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['480px', '350px'], //宽高
                    content: $("#addModel").html(),
                    btn: ['保存', '关闭'],
                    btnAlign: 'c',
                    yes: function (index, layero) {
                        return false;
                    },
                    success: function (layero, index) {
                        layero.addClass("layui-form");
                        layero.find(".layui-layer-btn0").attr("lay-filter", "form-verify").attr("lay-submit", "");
                        method.onsubmit(layero.find(".layui-layer-btn0"), layero, "/workflow/model/create", function (msg) {
                            //table.reload('lay-prop-list', {page: {curr: 1}});
                            //window.open("/modeler.html?modelId="+msg,"流程编辑","_blank");
                            layer.close(w);
                            table.reload('lay-model-list', {page: {curr: 1}});
                            var full_index=layer.open({
                                title: '<i class="layui-icon layui-icon-app"></i>&nbsp; 模型编辑',
                                type: 2,
                                content: "/modeler.html?modelId="+msg,
                                area: ['320px', '195px'],
                                maxmin: true
                            });
                            layer.full(full_index);
                        });
                        form.render();
                    }
                });
            },
            update: function (checkStatus) {
                if (checkStatus.data.length !== 1) {
                    layer.msg('请先选择一个模型修改！');
                    return false;
                }
                var modelId = checkStatus.data[0].id;
                var full_index=layer.open({
                    title: '<i class="layui-icon layui-icon-app"></i>&nbsp; 模型编辑',
                    type: 2,
                    content: "/modeler.html?modelId="+modelId,
                    area: ['320px', '195px'],
                    maxmin: true
                });
                layer.full(full_index);
            },
            deploy: function (checkStatus) {
                if (checkStatus.data.length !== 1) {
                    layer.msg('请先选择一个模型！');
                    return false;
                }
                var modelId = checkStatus.data[0].id;
                $MB.layerPost({url: "/workflow/model/deploy", data: {modelId:modelId}}, function (r) {
                    if (r.code == 0) {
                        layer.msg(r.msg);
                    } else {
                        layer.msg(r.msg,{skin: 'mb-warn'});
                    }
                });
            },
            del: function (checkStatus) {
                if (checkStatus.data.length === 0) {
                    layer.msg('请先选择你要删除的用户！');
                    return false;
                }
                var userArr = new Array;
                for (var i in checkStatus.data) {
                    userArr.push(checkStatus.data[i].userId);
                }
                layer.msg('你确定要删除选中的用户吗？', {
                    time: 0 //不自动关闭
                    , btn: ['确定', '取消']
                    , yes: function (index) {
                        layer.close(index);
                        $MB.layerPost({
                            url: "/user/delete",
                            data: {"ids": userArr.join(",")},
                            cache: false
                        }, function (data) {
                            layer.msg(data.msg);
                            table.reload('lay-user-list', {page: {curr: 1}});
                        });
                    }
                });
            },
            onsubmit: function (subBtn, layero, url, callback) {
                form.on("submit(form-verify)", function (data) {
                    if (!!subBtn.attr("sub")) {
                        layer.msg("不能重复提交！");
                        return false;
                    }
                    subBtn.attr("sub", true);
                    $MB.layerPost({url: url, data: layero.find("form").serialize()}, function (r) {
                        if (r.code == 0) {
                            callback(r.msg);
                        } else {
                            layer.msg(r.msg,{skin: 'mb-warn'});
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            }
        }
    })(jQuery);
});
