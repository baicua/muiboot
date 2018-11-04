layui.use(['menu','layer','laytpl','dict','form'], function(args) {
    "use strict";
    //1.如果是移动端，添加触屏关闭菜单事件
    if ($MB.isMobile() || $MB.isXsScreen()) {
        $(".layui-layout.mb-layout-admin").addClass("shrink");
        $("body").on("touchend", ".mb-body", function (e) {
            $(".layui-layout.mb-layout-admin").addClass("shrink");
        });
    }
    //2.hash监听事件
    if (!$MB.hasHistoryApi()) {
        $(".mb-header .topleft>a").attr("href","#home");
        window.onhashchange = function (e) {
            var hash = window.location.hash;
            if (!hash)return;
            $("#main-content").ajax_load("loading", hash);
        }
    } else {
        $("body").on("click", "a[menu-id]", function () {
            var $this = $(this);
            var id = $this.attr("menu-id");
            var $thisa = $("#navigation").find("a[menu-id='" + id + "']");
            var url = $thisa.attr("menu-url");
            if (!!url) {
                $("#main-content").ajax_load("loading", url);
            }
        });
        $(window).on("popstate", function () {
            var href = window.location.href;
            var url = href && href.substring(href.indexOf("sys") + 4);
            $("#main-content").ajax_load("popstate", url);
        });
    }
    //3.菜单栏打开关闭点击监听
    $(".mb-banner").on("click", 'a.flexible', function () {
        if ($(".mb-layout-admin").hasClass("shrink")) {
            $(".mb-layout-admin").removeClass("shrink");
            $('.layui-side-scroll li>a,.layui-side-scroll dd>a').unbind("mouseenter").unbind("mouseleave");
        } else {
            $(".mb-layout-admin").addClass("shrink");
            $('.shrink .layui-side-scroll li>a,.shrink .layui-side-scroll dd>a').hover(function (r) {
                layer.tips($(this).text(), $(this));
            }, function (r) {
                layer.close(layer.index);
            });
        }
    });
    //4.加载菜单
    layui.menu.loadmenu(userName);

    //5.绑定个人资料修改事件
    var tipsIndex=0;
    $('.topright .user').hover(function () {
        tipsIndex=layer.tips('点击修改个人资料和密码', '.topright .user', {
                tips: [3, '#3595CC']
            });
        }, function () {
            layer.close(tipsIndex);
        });
    });
    $('.topright').on('click',".user",function () {
        $MB.layerGet({url: ctx + "user/getUser", data: {"userId": userId}}, function (data) {
            if (!data || !data.msg || data.code != 0) {
                layer.msg('请求数据失败,您选择的用户不存在',{skin: 'mb-warn'});
                return false;
            }
            layui.laytpl($("#userProfile").html()).render(data.msg, function (html) {
                layer.open({
                    title: '<i class="layui-icon layui-icon-app"></i>&nbsp; 个人资料',
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['480px', '400px'], //宽高
                    content: html,
                    success: function (layero, index) {
                        layui.dict.render();
                        layui.form.on('submit(updateProfile)', function ($data) {
                            var data = $data.field;
                            $MB.layerPost({url: "/user/updateProfile", data:data}, function (r) {
                                if (r.code == 0) {
                                    layui.layer.msg(r.msg);
                                } else {
                                    layui.layer.msg(r.msg,{skin: 'mb-warn'});
                                }
                            });
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                        layui.form.on('submit(updatePwd)', function ($data) {
                            var data = $data.field;
                            $MB.layerPost({url: "/user/updatePwd", data:data}, function (r) {
                                if (r.code == 0) {
                                    layui.layer.msg(r.msg);
                                } else {
                                    layui.layer.msg(r.msg,{skin: 'mb-warn'});
                                }
                            });
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                        layui.form.render();
                    }
                });
            });
        });
    });
