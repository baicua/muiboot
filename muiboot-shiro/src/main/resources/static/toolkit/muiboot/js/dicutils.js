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
                var $inputshow=$this.next(".dic-tree-input");
                $inputshow.val(item.name),$this.val(item.id);
            }else if(hasChild){
                 defaultNode($this,item.children,id);
            }
        });
    };
    var obj = {
        data:{},
        load: function ($data) {
            $.getJSON(baseUrl,{dicKeys:$data},function (r) {
                if(!r||!r.msg||r.code != 0){
                    return false;
                }else {
                    for (var key in r.msg){
                        obj.data[key]=r.msg[key];
                    }
                }
            })
        },
        render:function ($select){
            if(!$select){
                $select=$("[dic-map]");
            }
            $select.each(function(index,element){
                var $this=$(element);
                loadedCount+=1;
                if ($this.hasClass('dic-map')){
                    var key = $this.attr("dic-map");
                    var value=$this.attr("dic-value");
                    var map =obj.data[key];
                    if(!!map){
                        $this.empty();
                        $this.append("<option value=''>--请选择--</option>");
                        for(var k in map){
                            $this.append("<option value='"+k+"'>"+map[k]+"</option>");
                        }
                        if(value)
                            $this.val(value);
                    }
                }else if ($this.hasClass('dic-tree')){
                    var id="dic-tree"+loadedCount,key = $this.attr("dic-map"),value=$this.val(),map =obj.data[key];
                    var verify=$this.attr("lay-verify"),placeholder=$this.attr("placeholder");
                    if(!!map&&map.children){
                        var $input=$('<input type="text" '+(!verify?"":"lay-verify="+verify)+' readonly '+(!placeholder?"":"placeholder="+placeholder)+' class="layui-input dic-tree-input">');
                        var ul =$('<ul id="'+id+'" class="layui-box layui-tree dic-tree-ul"></ul>');
                        $this.after(ul).after($input),$this.hide();
                        var nodes=$.extend([],map.children);
                        if(!!value)defaultNode($this,nodes,value);
                        ul.empty();
                        layui.tree({elem: "#"+id,nodes:nodes,click: function(node){
                            $input.val(node.name),$this.val(node.id),$input.toggleClass("show-tree"), $("body").toggleClass("tree-body");;
                        }
                        });
                        $input.on("click",function () {
                            $input.toggleClass("show-tree");
                            $("body").toggleClass("tree-body");
                        });
                    }
                }else if ($this.hasClass('dic-text')){
                    var text = $.trim($this.text());
                    var key = $this.attr("dic-map");
                    var map =obj.data[key];
                    $this.text(map&&map[text]||text);
                }

            })
        }
    };
    exports('dicutils', obj);
});