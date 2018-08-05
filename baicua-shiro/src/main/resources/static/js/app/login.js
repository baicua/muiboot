function reloadCode() {
    $("#validateCodeImg").attr("src", ctx + "gifCode?data=" + new Date() + "");
}

function login() {
	var $loginButton = $("#loginButton");
    var username = $(".login-form input[name='username']").val().trim();
    var password = $(".login-form input[name='password']").val().trim();
    //var code = $(".login-form input[name='code']").val().trim();
    var rememberMe = $(".login-form input[name='rememberme']").is(':checked');
    if (username == "") {
        //$MB.n_warning("请输入用户名！");
        $(".login-form input[name='username']").focus().addClass("invalid");
        return;
    }
    if (password == "") {
        //$MB.n_warning("请输入密码！");
        $(".login-form input[name='password']").focus().addClass("invalid");
        return;
    }
/*    if (code == "") {
         $MB.n_warning("请输入验证码！");
        return;
    }*/
    $loginButton.html("").append("<span class='icon-spinner'>正在登录</span>");
    
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "code": "",
            "rememberMe": rememberMe
        },
        dataType: "json",
        success: function(r) {
            if (r.code == 0) {
                location.href = ctx + 'index';
            } else {
        		// if (r.msg == '验证码错误！') reloadCode();
                //$MB.n_warning(r.msg);
                Materialize.toast(r.msg, 3000);
                $loginButton.html("登  录");
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