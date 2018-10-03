"use strict";
var $MB = (function() {
    var loading="";
    var count=0;
    var ajax_default={
        url:"",
        type: 'POST',
        async:true,
        cache:false,
        contentType: "application/x-www-form-urlencoded",
        data:""
    };
    var layer;
    layui.use('layer', function () {
        layer = layui.layer;
    });
    function isXsScreen() {
        var width=document.body.clientWidth;
        if(width&&width<992){
            return true
        }
        return false;
    }
    function IsPC() {
        var userAgentInfo = navigator.userAgent;
        var Agents = ["Android", "iPhone",
            "SymbianOS", "Windows Phone",
            "iPad", "iPod"];
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    function hasHistoryApi() {
        if (!!(window.history && history.pushState)){
            return true;
        } else {
            return false;
        }
    }
    function getRootPath() {
        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
        var curWwwPath = window.document.location.href;
        //获取主机地址之后的目录，如： /uimcardprj/share/meun.jsp
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht = curWwwPath.substring(0, pos);
        //获取带"/"的项目名，如：/uimcardprj
        return (localhostPaht + ctx);
    }
    var cacheManager=(function(){
        var cacheObj={};
        function removeLastItem() {
            var lastItem=0;
            var lastValue=0;
            for(var key in cacheObj){
                if(0===lastValue){
                    lastValue=cacheObj[key];
                }
                else if(lastValue>cacheObj[key]){
                    lastItem=key;
                    lastValue=cacheObj[key];
                }
            }
            delete cacheObj[lastItem];
        }
        return {
            setItem:function(key,value){
                cacheObj[key]=value;
            },
            getItem:function(key){
                return cacheObj[key];
            },
            removeItem:function(key){
                if(cacheObj.length>=10){
                    removeLastItem();
                }
                delete cacheObj[key];
            },
            //清空缓存
            clear:function(){
                cacheObj={};
            },
            isUserCache:function (params) {
                var useCache=false;
                var key =JSON.stringify(params);
                var time = this.getItem(key);
                var now = new Date().getTime();
                //计算出相差秒数
                var seconds=Math.floor((now-time)/1000);
                if(seconds<5){
                    useCache=true;
                }else {
                    this.setItem(key,now);
                }
                return useCache;
            }
        }
    })();
    function ajax(params,callback){
        $.ajax({
            type: params.type,
            url: params.url,
            data: params.data,
            async:params.async,
            cache:params.cache,
            beforeSend: function () {
                if(!$MB.getLoading(1)){
                    $MB.setLoading(layer.load(0,{shade: [0.01,'#fff']}));
                }
            },
            success: function (data) {
                callback(data);
            },
            complete: function () {
                var loading = $MB.getLoading(0);
                if (loading){
                    layer.close(loading);
                    $MB.setLoading("");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.info("error: " + XMLHttpRequest.responseText);
                if(XMLHttpRequest.status===303){
                    layer.msg('当前未登录或登录超时，是否返回重新登录？', {
                        time: 0 //不自动关闭
                        ,btn: ['确定', '取消']
                        ,yes: function(index){
                            location.reload();
                        }
                    });
                }else {
                    layer.msg('请求失败！',{skin: 'mb-warn'});
                }
            }
        });
    }
    function _layerPost(settings,callback) {
        var params = $.extend({}, ajax_default, settings);
        params.type= 'POST';
        ajax(params,callback);
    }
    function _layerGet(settings,callback) {
        var params = $.extend({}, ajax_default, settings);
        params.type= 'GET';
        if(!settings.cache){
            params.cache=cacheManager.isUserCache(params);
        }
        ajax(params,callback);
    }

    return {
        layerPost: function(settings,callback){
            try {
                _layerPost(settings,callback)
            }catch (e) {
                console.error("error:"+e.message);
                return false;
            }
        },
        layerGet: function(settings,callback){
            try {
                _layerGet(settings,callback)
            }catch (e) {
                console.error("error:"+e.message);
                return false;
            }
        },
        getScript: function(url,useCache){
            $.ajax({
                type: 'GET',
                url: url,
                dataType: "script",
                cache: useCache
            });
        },
        verify: function (form) {
            form.verify({
                radio: function(value, item){ //value：表单的值、item：表单的DOM对象
                    var name = $(item).prop("name");
                    var radios=$("input[name='"+name+"']");
                    var checked=radios.filter(':checked');
                    if(checked.length===0){
                        radios.addClass("layui-form-danger");
                        return '必填项不能为空';
                    }
                }
            });


        },
        isMobile:function () {
            return !IsPC();
        },
        isXsScreen:function () {
            return isXsScreen();
        },
        getLoading:function (flag){
            if(flag){
                count++;
            }else {
                count--;
            }
            if(count<=0&&loading){
                return loading;
            }else {
                return false;
            }
        },setLoading:function ($loading) {
            loading=$loading;
        },
        hasHistoryApi:function () {
            return hasHistoryApi();
        },
        getRootPath:function () {
            return getRootPath();
        }
    }
})(jQuery);