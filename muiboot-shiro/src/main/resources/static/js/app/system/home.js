;layui.use(['table','carousel','layer'], function(){
    "use strict";
    var table,carousel,form;
    table = layui.table,form=layui.form,carousel = layui.carousel;
    //建造实例
    carousel.render({
        elem: '.mb-shortcut'
        ,index:0
        ,width: '100%' //设置容器宽度
        ,height: '180px'
        ,arrow: 'none' //始终显示箭头
        ,autoplay: false //是否自动切换
    });
    carousel.render({
        elem: '.mb-backlog'
        ,index:0
        ,width: '100%' //设置容器宽度
        ,height: '180px'
        ,arrow: 'none' //始终显示箭头
        ,autoplay: false //是否自动切换
    });
    carousel.render({
        elem: '.mb-notice'
        ,index:0
        ,width: '100%' //设置容器宽度
        ,height: '480px'
        ,arrow: 'none' //始终显示箭头
        ,autoplay: false //是否自动切换
    });
});