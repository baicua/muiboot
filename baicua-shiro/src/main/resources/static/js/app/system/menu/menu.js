$(document).ready(function() {
    //initTreeTable();
    setTimeout(function(){
        layui.use('element', function () {
            var element = layui.element,form = layui.form;
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
                console.log(node); //node即为当前点击的节点数据
            }
        });
    });
});