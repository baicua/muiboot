/**
 * 字典工具
 */
;layui.define(['jquery','tree','form'], function (exports) {
    var loadedCount=0;
    "use strict";
    var $ = layui.jquery,form=layui.form;
    var baseUrl=ctx+"dict/loadDics";
    $(document).on("click","body",function (e) {
        if($(e.target).closest(".dic-tree-input,.dic-tree-ul").length == 0){
            $(".dic-tree-input").removeClass("show-tree");
            $("body").removeClass("tree-body");
        }
    });
    var dicArray= new Array;
    var defaultNode=function ($this,nodes,id) {
        var success=false;
        layui.each(nodes, function(index, item){
            var hasChild = item.children && item.children.length > 0;
            dicArray.push(item.name);
            if(item.id===id){
                var $inputshow=$this.siblings(".dic-tree-input");
                $inputshow.length>0&&($inputshow[0].value=dicArray.join("/")),$this.val(item.id);
                dicArray.length=0;
                success=true;
                return true;
            }else if(hasChild){
                if(defaultNode($this,item.children,id)) return true;
            }else {
                dicArray.pop();
            }
        });
        dicArray.pop();
        return success;
    };
    var data={};
    var storage=window.localStorage;
    var cacheName="DIC_CACHE";
    function get(k) {
        if(!storage){
            return k&&data[k];
        }else {
            var item=JSON.parse(storage.getItem(cacheName))||{};
            return k&&item&&item[k];
        }
    };
    function put(all) {
        if(!storage){
            for (var key in all){
                data[key]=all[key];
            }
            return true;
        }else {
            var map =JSON.parse(storage.getItem(cacheName))||{};
            for (var key in all){
                map[key]=all[key];
            }
            storage.setItem(cacheName,JSON.stringify(map));
            return true;
        }
    };
    function resetMap($this,$setting,$parent){
        if(!$parent){
            $this.val("");
            $this.empty();
            form.render("select","dic-filter");
            return;
        }
        var param = $.extend($setting,{data:{parentId:$parent}});
        $MB.layerGet(param,function (r) {
            if(r.code == 0){
                $this.val("");
                $this.empty().append("<option value=''>--请选择--</option>");
                for(var k in r.msg){
                    $this.append("<option value='"+k+"'>"+r.msg[k]+"</option>");
                }
                form.render("select","dic-filter");
            }
        });
    };
    function init($select){
        if(!$select){
            $select=$("[dic-map]");
        }
        $select.each(function(index,element){
            var $this=$(element);
            loadedCount+=1;
            if ($this.hasClass('dic-map')){
                renderDicMap($this);
            }else if ($this.hasClass('dic-tree')){
                renderDicTree($this);
            }else if ($this.hasClass('dic-text')){
                renderDicText($this);
            }else if ($this.hasClass('dic-map-url')){
                renderDicUrl($this);
            }

        })
    };

    function renderDicMap($this) {
        var key = $this.attr("dic-map");
        var value=$this.attr("dic-value");
        var isInit = $this.hasClass("dic-finish");
        var val =$this.val();
        var map =key&&get(key);
        if(!!map){
            $this.empty().append("<option value=''>--请选择--</option>");
            for(var k in map){
                $this.append("<option value='"+k+"'>"+map[k]+"</option>");
            }
            if(isInit){
                $this.val(val);
            }else {
                if(value&&value!="undefined"){
                    $this.val(value);
                }
                $this.addClass("dic-finish");
            }
        }
    }
    function renderDicTree($this) {
        var id="dic-tree"+loadedCount,key = $this.attr("dic-map"),value=$this.val(),map =key&&get(key)||"";
        var verify=$this.attr("lay-verify"),placeholder=$this.attr("placeholder"),disabled=$this.attr("disabled");
        var isInit = $this.hasClass("dic-finish");
        if(!!map&&map.children){
            var $input,$ul,$cancel,_input,_ul,_cancel;
            if(isInit){
                $input=$this.siblings(".dic-tree-input");
                $ul=$this.siblings(".dic-tree-ul");
                $cancel=$this.siblings(".dic-tree-close");
            }else {
                _input=['<input name="ignore-form" type="text" '
                    ,(!verify?"":"lay-verify="+verify)
                    ,'readonly',
                    disabled?"disabled":""
                    ,(!placeholder?"":"placeholder="+placeholder)
                    ,' class="mb-input dic-tree-input">'].join(" ");
                _ul =$('<ul id="'+id+'" class="layui-box layui-tree dic-tree-ul"></ul>');
                _cancel = $('<i class="layui-icon layui-icon-close-fill  mb-close dic-tree-close"></i>');
                $this.after(_ul).after(_cancel).after(_input),$this.hide();
                $input=$this.siblings(".dic-tree-input");
                $ul=$this.siblings(".dic-tree-ul");
                $cancel=$this.siblings(".dic-tree-close");

                $input.on("click",function () {
                    $input.toggleClass("show-tree");
                    $("body").toggleClass("tree-body");
                });
                $cancel.on("click",function () {
                    $this.val("").change(),$input.val("");
                });
                $this.addClass("dic-finish");
                $this.attr("treeId",id);
            }
            var nodes=$.extend([],map.children);
            if(nodes.length>0&&nodes[0].children.length>0){
                nodes[0].spread=true;
            }
            if(!!value)defaultNode($this,nodes,value);
            $ul.empty();
            layui.tree({elem: "#"+$this.attr("treeId"),nodes:nodes,click: function(node){
                if(!node.hasChecked){
                    return false;
                }
                defaultNode($this,nodes,node.id);
                $this.val(node.id).change(),$input.toggleClass("show-tree"), $("body").toggleClass("tree-body");
            }
            });
        }
    }
    function renderDicText($this) {
        var isInit = $this.hasClass("dic-finish");
        if(isInit)return;
        var text = $.trim($this.text());
        var key = $this.attr("dic-map");
        var map =get(key);
        $this.text(map&&map[text]||text);
        $this.addClass(["dic-finish ",key,"_",text].join(""));
    }
    function renderDicUrl($this) {
        var key = $this.attr("dic-map");
        var isInit = $this.hasClass("dic-finish");
        var val =$this.val();
        var parent =$($this.attr("dic-parent"));
        var setting =key&&get(key);
        if(parent.length>0&&setting){
            if(isInit){
                resetMap($this,parent.val()||"");
            }else {
                $this.addClass("dic-finish");
                parent.on("change",function () {
                    resetMap($this,setting,parent.val());
                })
            }
        }
    }

    var obj = {
        load: function ($data) {
            $.getJSON(baseUrl,{dicKeys:$data},function (r) {
                if(!r||!r.msg||r.code != 0){
                    return false;
                }else {
                    put(r.msg);
                    try {
                        init();
                        form.render();
                    }catch(e) {
                        console.error(e.message);
                        return false;
                    }
                }
            })
        },
        render:function ($select){
            try {
                init($select);
            }catch(e) {
               console.error(e.message);
               return false;
            }
        }
    };
    exports('dict', obj);
});