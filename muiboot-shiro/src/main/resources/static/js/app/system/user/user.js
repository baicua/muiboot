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
            ,height: 'full'
            ,skin:"line"
            ,cols: [[
                {type:'checkbox'}
                ,{field: 'userId', title: 'userId'}
                ,{field:'username', title: '用户名'}
                ,{field:'realName',  title: '真实姓名'}
                ,{field:'organId',  title: '所属机关'}
                ,{field:'groupName',  title: '所属部门'}
                ,{field:'email', title: '邮箱'}
                ,{field:'mobile', title: '手机号'}
                ,{field:'ssex', title: '性别'}
                ,{field:'status', title: '状态'}
            ]]
        });
        dict.load("DIC_ORGAN_TREE,DIC_DISABLE,DIC_DEPT_URL");
        dict.render();
        form.render();
        form.on('submit(search)', function($data){
            var data = $data.field;
            delete data["ignore-form"];
            table.reload('lay-user-list', {
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
    });
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
    $("#delBtn").on("click",function (r) {
    });
});
