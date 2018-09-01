"use strict";
var $MB = (function() {
    var ajax_default={
        url:"",
        type: 'POST',
        async:true,
        cache:false,
        contentType: "application/x-www-form-urlencoded",
        data:""
    };
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
            },
            success: function (data) {
                callback(data);
            },
            complete: function () {
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
                    layer.msg('请求失败！');
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
        }
    }
})(jQuery);