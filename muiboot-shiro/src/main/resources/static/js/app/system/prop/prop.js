;layui.use(['table','layer'], function () {
    "use strict";
    var table, form, laytpl, layer;
    table = layui.table,form = layui.form, laytpl = layui.laytpl, layer = layui.layer;
    form.render();
    table.render({
        id: 'lay-prop-list'
        , elem: '#propList'
        , url: '/prop/list' //数据接口
        , page: true //开启分页
        , size: 'sm'
        , height: 'full'
        , skin: "line"
        , cols: [[
            {type: 'checkbox'}
            , {field: 'propId', title: 'propId', hide: true}
            , {field: 'propKey', title: '配置键'}
            , {field: 'propValue', title: '配置值'}
            , {field: 'propMemo', title: '备注'}
        ]]
    });
    $("#addBtn").on("click", function (r) {
        method.add();
    });
    $("#updBtn").on("click", function (r) {
        method.update(table.checkStatus('lay-prop-list'));
    });
    var method = (function () {
        var loadModel = function (data, title, url) {
            var openIndex = 0;
            try {
                laytpl($("#propAdd").html()).render(data, function (html) {
                    //页面层
                    openIndex = layer.open({
                        title: '<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['480px', '350px'], //宽高
                        content: html,
                        btn: ['保存', '关闭'],
                        btnAlign: 'c',
                        yes: function (index, layero) {
                            return false;
                        },
                        success: function (layero, index) {
                            layero.addClass("layui-form");
                            layero.find(".layui-layer-btn0").attr("lay-filter", "form-verify").attr("lay-submit", "");
                            method.onsubmit(layero.find(".layui-layer-btn0"), layero, url, function () {
                                table.reload('lay-prop-list', {page: {curr: 1}});
                            });
                            form.render();
                        }
                    });
                });
            } catch (e) {
                layer.close(openIndex);
                layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
            }
        };
        return {
            add: function () {
                loadModel({}, "新增配置", ctx + "prop/add");
            },
            update: function (checkStatus) {
                if (checkStatus.data.length !== 1) {
                    layer.msg('请先选择一个配置修改！');
                    return false;
                }
                var propId = checkStatus.data[0].propId;
                try {
                    $MB.layerGet({url: ctx + "prop/getProp", data: {"propId": propId}}, function (data) {
                        if (!data || !data.msg || data.code != 0) {
                            layer.msg('请求数据失败,您选择的用户不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg, "修改用户", ctx + "prop/update");
                    });
                } catch (e) {
                    layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
                }
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
                            layer.msg(r.msg);
                            callback();
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
