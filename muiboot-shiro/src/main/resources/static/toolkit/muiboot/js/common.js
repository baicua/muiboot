var $MB = (function() {
    var ajax_default={
        url:"",
        type: 'POST',
        async:true,
        cache:false,
        contentType: "application/x-www-form-urlencoded",
        data:""
    };
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
                    layer.alert('请求失败！', {icon: 0});
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


        }
    }
})(jQuery);