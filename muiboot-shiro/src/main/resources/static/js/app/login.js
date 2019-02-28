;window.onload = function (){
    var input = document.getElementsByTagName("input");
    var loginForm = document.getElementById("login-form");
    for(var i = 0; i < input.length; i++) {
        input[i].onclick = function () {
            removeClass(loginForm,"invalid");
        }
    }
};
function login() {
    var $loginButton =document.getElementById("loginButton");
    var $usename =document.getElementById("username");
    var $password =document.getElementById("password");
    var $rememberMe =document.getElementById("rememberme");
    var loginError = document.getElementById("login_error");
    var loginForm = document.getElementById("login-form");
    if ($usename.value == "") {
        loginError.innerHTML="请输入用户名！";
        addClass(loginForm,"invalid");
        return;
    }
    if ($password.value == "") {
        loginError.innerHTML="请输入密码！";
        addClass(loginForm,"invalid");
        return;
    }
    addClass($loginButton,"loading");
    $loginButton.innerHTML='正在登录...';
    ajax({
        url: "/login",
        type:'POST',
        dataType:'json',
        data: {
            "username": $usename.value,
            "password": $password.value,
            "code": "",
            "rememberMe": $rememberMe.checked
        },
        success:function(r,xml){
            try {
                r=JSON.parse(r);
            }catch (e){
                r={code:1,msg:"登录失败，服务器繁忙，请稍后再试！"};
            }
            if (r.code == 0) {
                location.href = '/sys';
            } else {
                loginError.innerHTML=r.msg;
                addClass(loginForm,"invalid");
                $loginButton.innerHTML="立即登录";
                removeClass($loginButton,"loading");
            }
        },
        error:function(status){
            loginError.innerHTML="登录失败，服务器繁忙，请稍后再试！";
            addClass(loginForm,"invalid");
            $loginButton.innerHTML="立即登录";
            removeClass($loginButton,"loading");
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
};
function reloadCode() {
    //$("#validateCodeImg").attr("src","/gifCode?data=" + new Date() + "");
}
/*下面几个方法实现原生JS实现登录页面的几个功能，不需要引入jquery.js*/
function addClass(obj, cls){
    var obj_class = obj.className,//获取 class 内容.
        blank = (obj_class != '') ? ' ' : '';//判断获取到的 class 是否为空, 如果不为空在前面加个'空格'.
    if(obj_class.indexOf(cls)>-1){
        return;
    }
    var added = trim(obj_class) + blank + cls;//组合原来的 class 和需要添加的 class.
    obj.className = added;//替换原来的 class.
}
function removeClass(obj, cls){
    var obj_class =obj.className;
    if(obj_class){
        obj.className=trim(obj_class.replace(cls,' '));
    }
}
//去掉前后空格
function trim(str)
{
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

//创建ajax函数
function ajax(options){
    options=options||{};
    options.type=(options.type||'GET').toUpperCase();
    options.dataType=options.dataType||'json';
    params=formatParams(options.data);

    //创建-第一步
    var xhr;
    //非IE6
    if(window.XMLHttpRequest){
        xhr=new XMLHttpRequest();
    }else{
        //ie6及其以下版本浏览器
        xhr=ActiveXObject('Microsoft.XMLHTTP');
    }
    //接收-第三步
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4){
            var status=xhr.status;
            if(status>=200&&status<300){
                options.success&&options.success(xhr.responseText,xhr.responseXML);
            }else{
                options.error&&options.error(status);
            }
        }
    }

    //连接和发送-第二步
    if(options.type=='GET'){
        xhr.open('GET',options.url+'?'+params,true);
        xhr.send(null);
    }else if(options.type=='POST'){
        xhr.open('POST',options.url+"?t="+new Date().getTime(),true);
        xhr.setRequestHeader("If-Modified-Since","0");//防止缓存
        xhr.setRequestHeader("Cache-Control","no-cache");//防止缓存
        //设置表单提交时的内容类型
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(params);
    }
}

//格式化参数
function formatParams(data){
    var arr=[];
    for(var name in data){
        arr.push(encodeURIComponent(name)+'='+encodeURIComponent(data[name]));
    }
    return arr.join('&');
}

