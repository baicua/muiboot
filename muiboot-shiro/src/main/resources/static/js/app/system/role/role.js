;layui.use(['dict','table','treeTable','layer','element'], function(){
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
            ,{field:'roleKey', title: '角色编号',
                templet: function (d) {
                return '<a href="javascript:;" title="查看角色所有权限用户" class="layui-table-link role-cat" data-roleId="'+ d.roleId+'" >'+ d.roleKey + '</a>';
                 }
            }
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
        where: {roleLevel:""}
        ,done: function (res, curr, count) {
            dict.render($('.layui-table [dic-map]'));
            $('.role-cat').on('click',function (e) {
                var $this=$(this);
                var roleId = $this.attr('data-roleId');
                method.cat(roleId);
            })
        }
    });
    form.on('submit(search-role)', function($data){
        var data = $data.field;
        table.reload('lay-role-list', {
            where: $.extend({}, data)//设定异步数据接口的额外参数，任意设
            , page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(search-user)', function($data){
        var organId=$("#role-organId").val();
        var realName=$("#role-realName").val();
        var data = {};data.organId=organId;data.realName=realName;
        table.reload('lay-user-grout', {
            where: $.extend({},data)//设定异步数据接口的额外参数，任意设
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(reset-role)', function (data) {
        form.val("search-form", {
            "roleKey": ""
            , "roleName": ""
            , "roleLevel": ""
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
        var loadModel = function (data, title, url) {
            var openIndex = 0;
            try {
                laytpl($("#roleAdd").html()).render(data, function (html) {
                    //页面层
                    openIndex = layer.open({
                        title: '<i class="layui-icon layui-icon-app"></i>&nbsp; '+title,
                        type: 1,
                        skin: 'mb-layer-rim', //加上边框
                        area: ['640px', '480px'], //宽高
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
                            loadRoleUsers(data.roleId);
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
                layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
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
                laytpl($("#roleGrant").html()).render({roleIds:roleArr.join(",")}, function (html) {
                    //页面层
                    openIndex = layer.open({
                        title: '<i class="layui-icon layui-icon-app"></i>&nbsp; '+"用户授权("+roleNames.join("、")+")",
                        type: 1,
                        skin: 'mb-layer-rim', //加上边框
                        area: ['640px', '480px'], //宽高
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
            } catch (e) {
                layer.close(openIndex);
                layer.msg('请求数据异常：' + e.message,{skin: 'mb-warn'});
            }
        }
        function cat(roleId) {
            if (!roleId) {
                layer.msg('查看角色权限信息失败！');
                return false;
            }
            layer.open({
                title: '<i class="layui-icon layui-icon-app"></i>&nbsp; '+"用户列表",
                type: 1,
                skin: 'mb-layer-rim', //加上边框
                area: ['640px', '400px'], //宽高
                content: '<div class="layui-row"><table class="layui-hide" id="role-user-list"></table> </div>',
                success: function (layero, index) {
                    table.render({
                        id: 'lay-role-user'
                        , elem: '#role-user-list'
                        , url: '/user/user-role' //数据接口
                        , page: true //开启分页
                        , size: 'sm'
                        , height: '300px'
                        , skin: "line"
                        , cols: [[
                            {field: 'userId', title: 'userId', hide: true}
                            , {field: 'username', title: '登录名'}
                            , {field: 'realName', title: '用户名'}
                            , {
                                field: 'organId', title: '所属机关', templet: function (d) {
                                    return '<span class="dic-text" dic-map="DIC_ORGAN_TABLE">' + d.organId + '</span>';
                                }
                            }
                            , {field: 'groupName', title: '所属部门'}
                            , {field: 'mobile', title: '手机号'}
                        ]],
                        where: {roleId:roleId},
                        done: function (res, curr, count) {
                            dict.render($('.layui-table [dic-map]'));
                        }
                    });
                }
            });
        }
        function loadusers() {
            table.render({
                id: 'lay-user-grout'
                , elem: '#users-grout-list'
                , url: '/user/list' //数据接口
                , page: true //开启分页
                , size: 'sm'
                , height: '260px'
                , skin: "line"
                , cols: [[
                    {type: 'checkbox',checked:function (d) {
                        var checkeds=$("#user-select").find("input[name='userIds']:checked");
                        if(checkeds.length>0){
                            for (var i in checkeds){
                                if(checkeds[i].value==d.userId){
                                    return true;
                                }
                            }
                        }
                        return false;
                    }}
                    , {field: 'userId', title: 'userId', hide: true}
                    , {field: 'username', title: '登录名'}
                    , {field: 'realName', title: '用户名'}
                    , {field: 'groupName', title: '所属部门'}
                    , {field: 'mobile', title: '手机号'}
                ]],
                done: function (res, curr, count) {
                    dict.render($('.layui-table [dic-map]'));
                }
            });
            table.on('checkbox(users)', function(obj){
                if(obj.type=='one'){
                    if(obj.checked){//当前是否选中状态
                        var $select=$("#user-select").find("input[name='userIds'][value='"+obj.data.userId+"']");
                        if($select.length>0){
                            $select.prop('checked',obj.checked);
                        }else {
                            $("#user-select").append('<div class="layui-col-md3 layui-col-xs4 layui-timeline-title"><input type="checkbox" name="userIds" title="'+obj.data.realName+'" value="'+obj.data.userId+'" checked></div>');
                        }
                    }else {
                        var $select=$("#user-select").find("input[name='userIds'][value='"+obj.data.userId+"']");
                        $select.prop('checked',obj.checked);
                    }
                }else {
                    if(obj.checked){
                        var checkStatus = table.checkStatus('lay-user-grout');
                        for (var i in checkStatus.data) {
                            var $select=$("#user-select").find("input[name='userIds'][value='"+checkStatus.data[i].userId+"']");
                            if($select.length>0){
                                $select.prop('checked',obj.checked);
                            }else {
                                $("#user-select").append('<div class="layui-col-md3 layui-col-xs4 layui-timeline-title"><input type="checkbox" name="userIds" title="'+checkStatus.data[i].realName+'" value="'+checkStatus.data[i].userId+'" checked></div>');
                            }
                        }
                    }else {
                        var data = table.getAllData('lay-user-grout');
                        for (var i in data) {
                            var $select=$("#user-select").find("input[name='userIds'][value='"+data[i].userId+"']");
                            if($select.length>0){
                                $select.prop('checked',obj.checked);
                            }
                        }
                    }
                }
                var checkeds=$("#user-select").find("input[name='userIds']:checked");
                $("#select-num").text(checkeds.length);
                form.render('checkbox');
            });
        }
        function loadRoleUsers(roleId) {
            if(roleId){//修改角色
                table.render({
                    id: 'lay-role-users-list'
                    , elem: '#role-users-list'
                    , url: '/user/user-role' //数据接口
                    , page: true //开启分页
                    , size: 'sm'
                    , height: '235px'
                    , skin: "line"
                    , cols: [[
                        {field: 'userId', title: 'userId', hide: true}
                        , {field: 'username', title: '登录名'}
                        , {field: 'realName', title: '用户名'}
                        , {
                            field: 'organId', title: '所属机关', templet: function (d) {
                                return '<span class="dic-text" dic-map="DIC_ORGAN_TABLE">' + d.organId + '</span>';
                            }
                        }
                        , {field: 'groupName', title: '所属部门'}
                        , {title: '授权'
                            , templet: function (d) {
                                var checked="";
                                if(!$('#search-select-user-btn').data('flag')){
                                    checked="checked";
                                }
                                return '<input type="checkbox" lay-filter="user-grant"  lay-skin="primary" value="'+d.userId+'" '+checked+'/>';
                            }
                        }
                    ]],
                    where: {roleId:roleId},
                    done: function (res, curr, count) {
                        if(!$('#search-select-user-btn').data('flag')){
                            $("#role-users-count").text(count);
                        }
                        dict.render($('.layui-table [dic-map]'));
                    }
                });
                $("#search-role-user-btn").on("click",function (e) {
                    $('#search-select-user-btn').data("flag",true);
                    var organId=$("#role-organId").val();
                    var realName=$("#role-realName").val();
                    var data = {};data.organId=organId;data.realName=realName;
                    table.reload('lay-role-users-list', {
                        url: '/user/list'//查询全部用户
                        ,where: $.extend({},data),page: {curr: 1}
                    });
                });
                $("#search-select-user-btn").on("click",function (e) {
                    $('#search-select-user-btn').data("flag",false);
                    table.reload('lay-role-users-list', {
                        url: '/user/user-role'
                        ,where: {roleId:roleId,organId:'',realName:''},page: {curr: 1}
                    });
                });
                //监听授权
                form.on('checkbox(user-grant)', function(obj){
                    if(obj.elem.checked){//授权
                        layer.msg('你确定要授予该用户选择的角色吗？', {
                            time: 0, btn: ['确定', '取消']
                            , yes: function (index) {
                                layer.close(index);
                                $MB.layerPost({
                                    url: "/role/grantUser",
                                    data: {"roleId":roleId,"userId":obj.value},
                                    cache: false
                                }, function (r) {
                                    if (r.code == 0) {
                                        layer.msg(r.msg);
                                        $("#search-select-user-btn").click();
                                    } else {
                                        layer.msg(r.msg,{skin: 'mb-warn'});
                                        $("#search-select-user-btn").click();
                                    }
                                });
                            }
                            ,btn2:function (index) {$("#search-role-user-btn").click();}
                        });
                    }else {//取消授权
                        layer.msg('你确定要回收该用户选择的角色吗？', {
                            time: 0, btn: ['确定', '取消']
                            , yes: function (index) {
                                layer.close(index);
                                $MB.layerPost({
                                    url: "/role/revokeUser",
                                    data: {"roleId":roleId,"userId":obj.value},
                                    cache: false
                                }, function (r) {
                                    if (r.code == 0) {
                                        layer.msg(r.msg);
                                        $("#search-select-user-btn").click();
                                    } else {
                                        layer.msg(r.msg,{skin: 'mb-warn'});
                                        $("#search-select-user-btn").click();
                                    }
                                });
                            }
                            ,btn2:function (index) {layer.close(index);$("#search-select-user-btn").click();}
                        });
                    }
                });
            }else {//新增角色
                //监听授权
                form.on('checkbox(user-grant)', function(obj){

                });
            }
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
                            layer.msg('请求数据失败,您选择的角色不存在',{skin: 'mb-warn'});
                            return false;
                        }
                        loadModel(data.msg, "修改角色", ctx + "role/update");
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
                        }, function (r) {
                            if (r.code == 0) {
                                layer.msg(r.msg);
                                table.reload('lay-role-list', {page: {curr: 1}});
                            } else {
                                layer.msg(r.msg,{skin: 'mb-warn'});
                            }
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
            },
            grant:function (checkStatus) {
                grant(checkStatus);
            },
            cat:function (roleId) {
                cat(roleId);
            }
        }
    })(jQuery);
});
