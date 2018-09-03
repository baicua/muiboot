;$(document).ready(function() {
    "use strict";
    var model='<hr><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">操作用户:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.username||"/"}}</div></div><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">耗时（毫秒）:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.time||"/"}}</div></div><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">操作时间:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.createTime||"/"}}</div></div><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">操作方法:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.method||"/"}}</div></div><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">IP地址:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.ip||"/"}}</div></div><div class="layui-row"><div class="layui-col-xs4 layui-col-sm4 layui-col-md4">定位地点:</div><div class="layui-col-xs8 layui-col-sm8 layui-col-md8">{{ d.location||"/"}}</div></div>';
    $("#userOnline").after('<ul class="flow-default" id="flow-userOnline"></ul>');
    var page=1,limit=10;
    layui.use(['flow', 'laytpl'], function() {
        var flow = layui.flow,laytpl=layui.laytpl;
        flow.load({
            elem: '#flow-userOnline' //流加载容器
            //, scrollElem: '#LAY_demo1' //滚动条所在元素，一般不用填，此处只是演示需要。
            , done: function ($page, next) { //执行下一页的回调
                //模拟数据插入
                setTimeout(function () {
                    var lis = [];
                    $MB.layerGet({url:"/log/list",data:{"page": page,"limit":limit}},function (data) {
                        if(!data||!data.data||data.code != 0){
                            layer.msg('加载失败');
                            return false;
                        }else {
                            var length=data.data&&data.data&&data.data.length;
                            if(length){
                                for (var i = 0; i < length; i++) {
                                    laytpl(model).render($.extend({},data.data[i]), function(html){
                                        lis.push(html);
                                    });
                                }
                            }
                            page++;
                            next(lis.join(''), $page < Math.ceil(data.count/limit)); //假设总页数为 10
                        }
                    });
                    //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                    //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                }, 500);
            }
        });
    });

});