$(function(){
    $("input").on("focus",function (r) {
        $(".login-form").removeClass("invalid");
    });
});
function reloadCode() {
    $("#validateCodeImg").attr("src","/gifCode?data=" + new Date() + "");
}
function login() {
    var $loginButton = $("#loginButton");
    var username = $(".login-form input[name='username']").val().replace(/^\s+|\s+$/gm,'');
    var password = $(".login-form input[name='password']").val().replace(/^\s+|\s+$/gm,'');
    //var code = $(".login-form input[name='code']").val().trim();
    var rememberMe = $(".login-form input[name='rememberme']").is(':checked');
    if (username == "") {
        //$MB.n_warning("请输入用户名！");
        $(".login_error").html("请输入用户名！");
        $(".login-form").addClass("invalid");
        //$(".login-form input[name='username']").focus().addClass("invalid");
        return;
    }
    if (password == "") {
        //$MB.n_warning("请输入密码！");
        $(".login_error").html("请输入密码！");
        $(".login-form").addClass("invalid");
        return;
    }
    /*    if (code == "") {
     $MB.n_warning("请输入验证码！");
     return;
     }*/
    $loginButton.addClass("loading");
    $loginButton.html("").append('正在登录...');
    $.ajax({
        type: "post",
        url: "/login",
        data: {
            "username": username,
            "password": password,
            "code": "",
            "rememberMe": rememberMe
        },
        dataType: "json",
        success: function(r) {
            if (r.code == 0) {
                location.href = '/sys';
            } else {
                // if (r.msg == '验证码错误！') reloadCode();
                //$MB.n_warning(r.msg);
                $(".login_error").html(r.msg);
                $(".login-form").addClass("invalid");
                $loginButton.html("立即登录");
                $loginButton.removeClass("loading");
            }
        }
    });
}
document.onkeyup = function(e) {
    if (window.event)
        e = window.event;
    var code = e.charCode || e.keyCode;
    if (code == 13) {
        login();
    }
}
