;layui.use(['table','layer'], function(){
    "use strict";
    var table = layui.table;
    //第一个实例
    table.render({
        elem: '#userOnline'
        ,url: '/session/list' //数据接口
        ,page: true //开启分页
        ,size: 'sm'
        ,height: 'full-200'
        ,skin:"line"
        ,cols: [[
            {field: 'id', title: 'ID'}
            ,{field:'username', title: '登录名'}
            ,{field:'startTimestamp',  title: '登录时间'}
            ,{field:'lastAccessTime',  title: '最后访问时间'}
            ,{field:'host', title: 'IP地址'}
            ,{field:'location', title: '登录地点'}
            ,{field:'score', title: '评分'}
            ,{field:'status',title: '状态'}
        ]]
    })
    function offline(id, status, username) {
        if (status == "0") {
            $MB.n_warning("该用户已是离线状态！！");
            return;
        }
        if (username == userName) {
            location.href = ctx + 'logout';
        }
        $.get(ctx + "session/forceLogout", { "id": id }, function(r) {
            if (r.code == 0) {
                $MB.n_success('该用户已强制下线！');
                $MB.refreshTable('onlineTable');
            } else {
                $MB.n_danger(r.msg);
            }
        }, "json");
    }
});
