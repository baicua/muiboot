;$(document).ready(function() {
    "use strict";
    var table,dict,form;
    layui.use(['dict','table'], function(){
        table = layui.table,dict=layui.dict,form=layui.form;
        table.render({
            id: 'lay-user-list'
            ,elem: '#userList'
            ,url: '/user/list' //数据接口
            ,page: true //开启分页
            ,size: 'sm'
            ,height: 'full-300'
            ,skin:"line"
            ,cols: [[
                {type:'checkbox'}
                ,{field: 'userId', title: 'userId'}
                ,{field:'username', title: '用户名'}
                ,{field:'realName',  title: '真实姓名'}
                ,{field:'deptName',  title: '所属部门'}
                ,{field:'email', title: '邮箱'}
                ,{field:'mobile', title: '手机号'}
                ,{field:'ssex', title: '性别'}
                ,{field:'status', title: '状态'}
            ]]
        });
        dict.load("dicDeptFirstTree,disableDic");
        dict.render();
        form.render();
        $("#expBtn").on("click",function (r) {
            $MB.layerPost({url: "/user/excel",data:{}}, function (r) {
                if (r.code == 0) {
                    window.location.href = $MB.getRootPath()+"/common/download?fileName=" + r.msg + "&delete=" + true;
                } else {
                    layer.msg(r.msg);
                }
            });
        });
        $("#delBtn").on("click",function (r) {
        });
    });

});