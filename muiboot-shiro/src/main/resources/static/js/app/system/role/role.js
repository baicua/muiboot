;layui.use(['dict','table'], function(){
    "use strict";
    var table,dict,form;
    table = layui.table,dict=layui.dict,form=layui.form;
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
    $("#expBtn").on("click",function (r) {
        $MB.layerPost({url: "/role/excel",data:{}}, function (r) {
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
