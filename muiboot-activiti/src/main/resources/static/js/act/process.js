;layui.use(['table','layer'], function () {
    "use strict";
    var table, form, laytpl, layer;
    table = layui.table, form = layui.form, laytpl = layui.laytpl, layer = layui.layer;
    form.render();
    table.render({
        id: 'lay-process-list'
        , elem: '#processList'
        , url: '/workflow/process/list' //数据接口
        , page: true //开启分页
        , size: 'sm'
        , height: 'full'
        , skin: "line"
        , cols: [[
            {type: 'checkbox'}
            , {field: 'id', title: '键值', hide: true}
            , {field: 'key', title: '编号'}
            , {field: 'name', title: '流程名称'}
            , {field: 'version', title: '版本(对应部署版本号)'}
            , {field: 'deploymentId', title: '部署编号'}
            , {field: 'deploymentTime', title: '部署时间'}
        ]]
    });
    $("#delBtn").on("click", function (r) {
        method.del(table.checkStatus('lay-process-list'));
    });
    var method = (function () {
        return {
            del: function (checkStatus) {
                if (checkStatus.data.length === 0) {
                    layer.msg('请先选择你要删除的流程！');
                    return false;
                }
                var ids = new Array;
                for (var i in checkStatus.data) {
                    ids.push(checkStatus.data[i].deploymentId);
                }
                layer.msg('你确定要删除选中的流程吗？', {
                    time: 0 //不自动关闭
                    , btn: ['确定', '取消']
                    , yes: function (index) {
                        layer.close(index);
                        $MB.layerPost({
                            url: "/workflow/process/del",
                            data: {"ids": ids.join(",")},
                            cache: false
                        }, function (data) {
                            layer.msg(data.msg);
                            table.reload('lay-process-list', {page: {curr: 1}});
                        });
                    }
                });
            }
        }
    })(jQuery);
});
