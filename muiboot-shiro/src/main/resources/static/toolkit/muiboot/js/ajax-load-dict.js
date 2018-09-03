;layui.define(['jquery','tree'], function (exports) {
    var loadedCount=0;
    "use strict";
    var $ = layui.jquery;
    var baseUrl=ctx+"dict/loadDics";
    $(document).on("click",".tree-body",function (e) {
        if($(e.target).closest(".dic-tree-input,.dic-tree-ul").length == 0){
            $(".dic-tree-input").toggleClass("show-tree");
            $("body").toggleClass("tree-body");
        }
    });
    var defaultNode=function ($this,nodes,id) {
        layui.each(nodes, function(index, item){
            var hasChild = item.children && item.children.length > 0;
            if(item.id===id){
                var $inputshow=$this.siblings(".dic-tree-input");
                $inputshow.length>0&&($inputshow[0].value=item.name),$this.val(item.id);
                return;
            }else if(hasChild){
                 defaultNode($this,item.children,id);
            }
        });
    };
    var init=function($select){
        if(!$select){
            $select=$("[dic-map]");
        }
        $select.each(function(index,element){
            var $this=$(element);
            loadedCount+=1;
            if ($this.hasClass('dic-map')){
                var key = $this.attr("dic-map");
                var value=$this.attr("dic-value");
                var map =value&&data[key];
                if(!!map){
                    $this.empty();
                    $this.append("<option value=''>--请选择--</option>");
                    for(var k in map){
                        $this.append("<option value='"+k+"'>"+map[k]+"</option>");
                    }
                    $this.val(value||'');
                }
            }else if ($this.hasClass('dic-tree')){
                var id="dic-tree"+loadedCount,key = $this.attr("dic-map"),value=$this.val(),map =data[key]||"";
                var verify=$this.attr("lay-verify"),placeholder=$this.attr("placeholder");
                if(!!map&&map.children){
                    var $input=$('<input type="text" '+(!verify?"":"lay-verify="+verify)+' readonly '+(!placeholder?"":"placeholder="+placeholder)+' class="layui-input dic-tree-input">');
                    var ul =$('<ul id="'+id+'" class="layui-box layui-tree dic-tree-ul"></ul>');
                    var cancel = $('<i class="layui-icon layui-icon-close-fill"></i>');
                    $this.after(ul).after(cancel).after($input),$this.hide();
                    var nodes=$.extend([],map.children);
                    if(!!value)defaultNode($this,nodes,value);
                    ul.empty();
                    layui.tree({elem: "#"+id,nodes:nodes,click: function(node){
                        $input.val(node.name||''),$this.val(node.id),$input.toggleClass("show-tree"), $("body").toggleClass("tree-body");;
                    }
                    });
                    $input.on("click",function () {
                        $input.toggleClass("show-tree");
                        $("body").toggleClass("tree-body");
                    });
                    cancel.on("click",function () {
                        $this.val(""),$input.val("");
                    });
                }
            }else if ($this.hasClass('dic-text')){
                var text = $.trim($this.text());
                var key = $this.attr("dic-map");
                var map =data[key];
                $this.text(map&&map[text]||text);
            }

        })
    };
    var data={};
    var obj = {
        load: function ($data) {
            $.getJSON(baseUrl,{dicKeys:$data},function (r) {
                if(!r||!r.msg||r.code != 0){
                    return false;
                }else {
                    for (var key in r.msg){
                        data[key]=r.msg[key];
                    }
                }
            })
        },
        render:function ($select){
            try {
                init($select)
            }catch(e) {
               console.error(e.message);
            }
        },
        get:function (k) {

        },
        put:function (all) {
        }
    };
    exports('dict', obj);
});