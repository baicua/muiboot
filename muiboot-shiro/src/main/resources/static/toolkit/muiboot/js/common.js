var $MB = (function() {
        var ajax_default={
            url:"",
            type: 'POST',
            async:true,
            cache:false,
            contentType: "application/x-www-form-urlencoded",
            data:""
        };
    function _layerPost(settings,callback) {
        var params = $.extend({}, ajax_default, settings);
        $.ajax({
            type: 'POST',
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
            error: function (data) {
                console.info("error: " + data.responseText);
                layer.alert('请求失败！', {icon: 0});
            }
        });
    }
    function _layerGet(settings,callback) {
        var params = $.extend({}, ajax_default, settings);
        $.ajax({
            type: 'GET',
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
            error: function (data) {
                console.info("error: " + data.responseText);
                layer.alert('请求失败！', {icon: 0});
            }
        });
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