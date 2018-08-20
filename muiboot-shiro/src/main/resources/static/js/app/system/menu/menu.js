$(document).ready(function() {
    //initTreeTable();
    var element,form,laytpl;
    setTimeout(function(){
        layui.use(['element', 'laytpl'], function () {
            element = layui.element,form = layui.form,laytpl = layui.laytpl;
            element.init();
            form.render();
        });
    },100);
    $MB.layerGet({url:ctx+"menu/tree", cache:false},function (data) {
        var nodes=$.extend([], data.msg.children);
        layui.tree({
            elem: '#menuTree'
            ,nodes:nodes
            ,click: function(node){
                $MB.layerGet({url:ctx+"toolkit/compent/menu/menuInfo.html",cache:true},function(text){
                    var $compent=$("<code></code>").html(text);
                    $MB.layerGet({url:ctx+"menu/getMenuDetail",data:{menuId:node.id},cache:false},function(data){
                        laytpl($compent.find("#layui-table-menu").html()).render(data.msg.menu, function(html){
                            $("#menuInfoPanle").html(html);
                        });
                        laytpl($compent.find("#layui-breadcrumb-permission").html()).render(data.msg.permissions, function(html){
                            $("#menuButtonPanle").html(html);
                        });
                        laytpl($compent.find("#layui-breadcrumb-role").html()).render(data.msg.roles, function(html){
                            $("#menuAuthPanle").html(html);
                        });
                        element.init();
                    });
                });
            }
        });
    });
});