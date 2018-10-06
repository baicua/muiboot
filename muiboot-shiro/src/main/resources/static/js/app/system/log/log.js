;layui.use(['table','layer'], function(){
    "use strict";
    var table;
    table = layui.table;
    //第一个实例
    table.render({
        id: 'lay-userOnline'
        ,elem: '#userOnline'
        ,url: '/log/list' //数据接口
        ,page: true //开启分页
        ,size: 'sm'
        ,height: 'full-200'
        ,skin:"line"
        ,cols: [[
            {type:'checkbox'}
            ,{field: 'id', title: 'ID'}
            ,{field:'username', title: '操作用户'}
            ,{field:'operation',  title: '描述'}
            ,{field:'time',  title: '耗时（毫秒）'}
            ,{field:'ip', title: 'IP地址'}
            ,{field:'method',  title: '操作方法'}
            ,{field:'params',  title: '参数'}
            ,{field:'location', title: '操作地点'}
            ,{field:'createTime', title: '操作时间'}
        ]]
    });

    $("#expBtn").on("click",function (r) {
        $MB.layerPost({url: $MB.getRootPath()+"/log/excel",data:{}}, function (r) {
            if (r.code == 0) {
                window.location.href = $MB.getRootPath()+"/common/download?fileName=" + r.msg + "&delete=" + true;
            } else {
                layer.msg(r.msg);
            }
        });
    });
    $("#delBtn").on("click",function (r) {
        deleteLogs();
    });
    function deleteLogs() {
        var checkStatus = table.checkStatus('lay-userOnline');
        if(checkStatus.data.length===0){
            layer.msg('没有选择数据');
            return;
        }
        var ids = "";
        for (var i = 0; i < checkStatus.data.length; i++) {
            ids += checkStatus.data[i].id;
            if (i != (checkStatus.data.length - 1)) ids += ",";
        }
        layer.msg('你确定要删除选中日志吗？', {
            time: 0 //不自动关闭
            ,btn: ['确定', '取消']
            ,yes: function(index){
                layer.close(index);
                $MB.layerPost({url:ctx + "log/delete",data:{"ids": ids},cache:false},function (data) {
                    layer.msg(data.msg);
                    table.reload("lay-userOnline");
                });
            }
        });
    }
});
