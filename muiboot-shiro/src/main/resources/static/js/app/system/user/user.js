;layui.use(['dict', 'table','layer'], function () {
    "use strict";
    var table, dict, form, laytpl, layer;
    table = layui.table, dict = layui.dict, form = layui.form, laytpl = layui.laytpl, layer = layui.layer;
    dict.load("DIC_ORGAN_TREE,DIC_SEX,DIC_DISABLE,DIC_DEPT_URL,DIC_DEPT_TREE,DIC_ORGAN_TABLE");
    form.render();
    table.render({
        id: 'lay-user-list'
        , elem: '#userList'
        , url: '/user/list' //数据接口
        , page: true //开启分页
        , size: 'sm'
        , height: 'full'
        , skin: "line"
        , cols: [[
            {type: 'checkbox'}
            , {field: 'userId', title: 'userId', hide: true}
            , {field: 'username', title: '登录名'}
            , {field: 'realName', title: '用户名'}
            , {
                field: 'organId', title: '所属机关', templet: function (d) {
                    return '<span class="dic-text" dic-map="DIC_ORGAN_TABLE">' + d.organId + '</span>';
                }
            }
            , {field: 'groupName', title: '所属部门'}
            , {field: 'email', title: '邮箱'}
            , {field: 'mobile', title: '手机号'}
            , {
                field: 'ssex', title: '性别', templet: function (d) {
                    return '<span class="dic-text" dic-map="DIC_SEX">' + d.ssex + '</span>';
                }
            }
            , {
                field: 'status', title: '状态', templet: function (d) {
                    return '<span class="dic-text" dic-map="DIC_DISABLE">' + d.status + '</span>';
                }
            }
        ]],
        done: function (res, curr, count) {
            dict.render($('.layui-table [dic-map]'));
        }
    });
    form.on('submit(search)', function ($data) {
        var data = $data.field;
        table.reload('lay-user-list', {
            where: jQuery.extend({}, data)//设定异步数据接口的额外参数，任意设
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
        method.update(table.checkStatus('lay-user-list'));
    });
    $("#expBtn").on("click", function (r) {
        $MB.layerPost({url: "/user/excel", data: {}}, function (r) {
            if (r.code == 0) {
                window.location.href = $MB.getRootPath() + "/common/download?fileName=" + r.msg + "&delete=" + true;
            } else {
                layer.msg(r.msg);
            }
        });
    });
    $("#delBtn").on("click", function (r) {
        method.del(table.checkStatus('lay-user-list'));
    });
    var method = (function () {
        var loadModel = function (data, title, url) {
            var openIndex = 0;
            try {
                laytpl($("#userAdd").html()).render(data, function (html) {
                    //页面层
                    openIndex = layer.open({
                        title: '<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
                        type: 1,
                        skin: 'layui-layer-rim', //加上边框
                        area: ['640px', '400px'], //宽高
                        content: html,
                        btn: ['保存', '关闭'],
                        btnAlign: 'c',
                        yes: function (index, layero) {
                            return false;
                        },
                        success: function (layero, index) {
                            layero.addClass("layui-form");
                            dict.render();
                            layero.find(".layui-layer-btn0").attr("lay-filter", "form-verify").attr("lay-submit", "");
                            method.onsubmit(layero.find(".layui-layer-btn0"), layero, url, function () {
                                table.reload('lay-user-list', {page: {curr: 1}});
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
                loadModel({status: 1, ssex: 1}, "新增用户", ctx + "user/add");
            },
            update: function (checkStatus) {
                if (checkStatus.data.length !== 1) {
                    layer.msg('请先选择一个用户修改！');
                    return false;
                }
                var userId = checkStatus.data[0].userId;
                try {
                    $MB.layerGet({url: ctx + "user/getUser", data: {"userId": userId}}, function (data) {
                        if (!data || !data.msg || data.code != 0) {
                            layer.msg('请求数据失败,您选择的用户不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg, "修改用户", ctx + "user/update");
                    });
                } catch (e) {
                    layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
                }
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
