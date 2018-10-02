;layui.use(['dict','table','treeTable'], function(){
    "use strict";
    var table, dict, form, laytpl, layer;
    table = layui.table, dict = layui.dict, form = layui.form, laytpl = layui.laytpl, layer = layui.layer;
    dict.load("DIC_ORGAN_TREE,DIC_SEX,DIC_DISABLE,DIC_DEPT_URL,DIC_DEPT_TREE,DIC_ORGAN_TABLE,DIC_ROLE_LEVEL");
    form.render();
    table.render({
        id: 'lay-role-list'
        ,elem: '#roleList'
        ,url: '/role/list' //数据接口
        ,page: true //开启分页
        ,size: 'sm'
        ,height: 'full'
        ,skin:"line"
        ,cols: [[
            {type:'checkbox'}
            ,{field: 'roleId', title: 'roleId'}
            ,{field:'roleKey', title: '角色编号'}
            ,{field:'roleName', title: '角色名'}
            ,{field:'roleLevel', title: '角色级别',
                templet: function (d) {
                    return '<span class="dic-text" dic-map="DIC_ROLE_LEVEL">' + d.roleLevel + '</span>';
                }
            }
            ,{field:'remark',  title: '备注'}
            ,{field:'createTime',  title: '创建时间'}
            ,{field:'modifyTime',  title: '修改时间'}
        ]],
        done: function (res, curr, count) {
            dict.render($('.layui-table [dic-map]'));
        }
    });
    form.on('submit(search)', function($data){
        var data = $data.field;
        delete data["ignore-form"];
        table.reload('lay-role-list', {
            where: $.extend({},data)//设定异步数据接口的额外参数，任意设
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(reset)', function(data){
        form.val("search-form", {
            "organId": ""
            ,"ignore-form": ""
            ,"deptId": ""
            ,"valid": ""
            ,"username": ""
            ,"realName":  ""
            ,"mobile": ""
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    $("#addBtn").on("click", function (r) {
        method.add();
    });
    $("#updBtn").on("click", function (r) {
        method.update(table.checkStatus('lay-role-list'));
    });
    $("#grantBtn").on("click", function (r) {
        method.grant(table.checkStatus('lay-role-list'));
    });
    $("#expBtn").on("click", function (r) {
        $MB.layerPost({url: "/role/excel", data: {}}, function (r) {
            if (r.code == 0) {
                window.location.href = $MB.getRootPath() + "/common/download?fileName=" + r.msg + "&delete=" + true;
            } else {
                layer.msg(r.msg);
            }
        });
    });
    $("#delBtn").on("click", function (r) {
        method.del(table.checkStatus('lay-role-list'));
    });
    var method = (function () {
        var menuModel = null;
        $MB.layerGet({url: ctx + "model/role/add.html", cache: true}, function (text) {
            menuModel = text;
        });
        var loadModel = function (data, title, url) {
            var openIndex = 0;
            try {
                laytpl(menuModel).render(data, function (html) {
                    //页面层
                    openIndex = layer.open({
                        title: title,
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
                                table.reload('lay-role-list', {page: {curr: 1}});
                            });
                            $MB.layerGet({url:ctx+"session/getAuthList",data:data,cache:false},function (data) {
                                var nodes=$.extend([], data.msg.children);
                                $("#authTree").empty();
                                layui.treeTable({
                                    elem: '#authTree'
                                    ,nodes:nodes
                                    ,checkName:"menuId"
                                    ,check:"checkbox"
                                    ,click: function(node){
                                        return false;
                                    }
                                });
                            });
                            form.render();
                        }
                    });
                });
            } catch (e) {
                layer.close(openIndex);
                layer.msg('请求数据异常：' + e.message);
            }
        };
        function grant(checkStatus) {
            if (checkStatus.data.length === 0) {
                layer.msg('请先选择角色！');
                return false;
            }
            var roleArr = new Array;
            var roleNames=new Array;
            for (var i in checkStatus.data) {
                roleArr.push(checkStatus.data[i].roleId);
                roleNames.push(checkStatus.data[i].roleName)
            }
            var openIndex = 0;
            try {
                $MB.layerGet({url: ctx + "model/role/grant.html", cache: true}, function (text) {
                    laytpl(text).render({roleIds:roleArr.join(",")}, function (html) {
                        //页面层
                        openIndex = layer.open({
                            title: "用户授权("+roleNames.join("、")+")",
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
                                loadusers();
                                layero.find(".layui-layer-btn0").attr("lay-filter", "form-verify").attr("lay-submit", "");
                                method.onsubmit(layero.find(".layui-layer-btn0"), layero, ctx + "role/grant", function () {

                                });
                                form.render();
                            }
                        });
                    });
                });
            } catch (e) {
                layer.close(openIndex);
                layer.msg('请求数据异常：' + e.message);
            }
        }
        function loadusers() {
            table.render({
                id: 'lay-user-grout'
                , elem: '#users-grout-list'
                , url: '/user/list' //数据接口
                , page: true //开启分页
                , size: 'sm'
                , height: 'full'
                , skin: "line"
                , cols: [[
                    {type: 'checkbox'}
                    , {field: 'userId', title: 'userId', hide: true}
                    , {field: 'username', title: '用户名'}
                    , {field: 'realName', title: '真实名'}
                    , {field: 'groupName', title: '所属部门'}
                    , {field: 'mobile', title: '手机号'}
                ]],
                done: function (res, curr, count) {
                    dict.render($('.layui-table [dic-map]'));
                }
            });
        }
        return {
            add: function () {
                loadModel({roleLevel: 0}, "新增角色", ctx + "role/add");
            },
            update: function (checkStatus) {
                if (checkStatus.data.length !== 1) {
                    layer.msg('请先现在一个角色修改！');
                    return false;
                }
                var roleId = checkStatus.data[0].roleId;
                try {
                    $MB.layerGet({url: ctx + "role/getRole", data: {"roleId": roleId}}, function (data) {
                        if (!data || !data.msg || data.code != 0) {
                            layer.msg('请求数据失败,您选择的角色不存在');
                            return false;
                        }
                        loadModel(data.msg, "修改角色", ctx + "role/update");
                    });
                } catch (e) {
                    layer.msg('请求数据异常：' + e.message);
                }
            },
            del: function (checkStatus) {
                if (checkStatus.data.length === 0) {
                    layer.msg('请先选择你要删除的用户！');
                    return false;
                }
                var roleArr = new Array;
                for (var i in checkStatus.data) {
                    roleArr.push(checkStatus.data[i].roleId);
                }
                layer.msg('你确定要删除选中的角色吗？', {
                    time: 0 //不自动关闭
                    , btn: ['确定', '取消']
                    , yes: function (index) {
                        layer.close(index);
                        $MB.layerPost({
                            url: "/role/delete",
                            data: {"ids": roleArr.join(",")},
                            cache: false
                        }, function (data) {
                            layer.msg(data.msg);
                            table.reload('lay-role-list', {page: {curr: 1}});
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
                            layer.msg(r.msg);
                            subBtn.removeAttr("sub");
                        }
                    });
                    return false;
                });
            },
            grant:function (checkStatus) {
                grant(checkStatus);
            }
        }
    })(jQuery);
});
