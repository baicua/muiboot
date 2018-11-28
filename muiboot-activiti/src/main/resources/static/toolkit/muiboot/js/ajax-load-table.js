/**
 * 表格工具
 */
;layui.define(['jquery'], function (exports) {
    "use strict";
    var $ = layui.jquery;
    var M_TABLE=['<table class="imgtable">','</table>']
        ,M_HEADER=['<thead>','</thead>']
        ,M_TBODY=['<tbody>','</tbody>']
        ,M_PAGINATION=['<div class="pagin">','<div class="message">共<i class="blue">'
        ,0,'</i>条记录，当前显示第&nbsp;<i class="blue">',1,'&nbsp;</i>页</div>'
        ,'<ul class="paginList">'
        ,'<li class="paginItem"><a href="javascript:;"><span class="pagepre"></span></a></li>'
        ,'<li class="paginItem"><a href="javascript:;"><span class="pagenxt"></span></a></li>'
        ,'</ul>'
        ,'</div>']
    //构造器
    ,Class = function(options){
        var that = this;
        that.index = ++table.index;
        that.config = $.extend({}, that.config, table.config, options);
        that.render();
    };
    //默认配置
    Class.prototype.config = {
        limit: 10 //每页显示的数量
        ,loading: true //请求数据时，是否显示loading
        ,cellMinWidth: 60 //所有单元格默认最小宽度
        ,defaultToolbar: ['filter', 'exports', 'print'] //工具栏右侧图标
        ,text: {
            none: '无数据'
        }
    };

    //表格渲染
    Class.prototype.render = function(){
        var that = this
            ,options = that.config;

        var $table= $(options.elem);
        options.where = options.where || {};
        options.id = options.id || $table.attr('id') || options.index;

        //请求参数的自定义格式
        options.request = $.extend({
            pageName: 'page'
            ,limitName: 'limit'
        }, options.request)

        //响应数据的自定义格式
        options.response = $.extend({
            statusName: 'code'
            ,statusCode: 0
            ,msgName: 'msg'
            ,dataName: 'data'
            ,countName: 'count'
        }, options.response);

        //如果 page 传入 laypage 对象
        if(typeof options.page === 'object'){
            options.limit = options.page.limit || options.limit;
            options.limits = options.page.limits || options.limits;
            that.page = options.page.curr = options.page.curr || 1;
            delete options.page.elem;
            delete options.page.jump;
        }

        if(!options.elem[0]) return that;

        //高度铺满：full-差距值
        if(options.height && /^full-\d+$/.test(options.height)){
            that.fullHeightGap = options.height.split('-')[1];
            options.height = $(window).height() - that.fullHeightGap;
        }

        //初始化一些参数
        that.setInit();

        //开始插入替代元素
        var othis = options.elem
            ,hasRender = othis.next('.' + ELEM_VIEW)

            //主容器
            ,reElem = that.elem = $(laytpl(TPL_MAIN).render({
                VIEW_CLASS: ELEM_VIEW
                ,data: options
                ,index: that.index //索引
            }));

        options.index = that.index;

        //生成替代元素
        hasRender[0] && hasRender.remove(); //如果已经渲染，则Rerender
        othis.after(reElem);

        //各级容器
        that.layTool = reElem.find(ELEM_TOOL);
        that.layBox = reElem.find(ELEM_BOX);
        that.layHeader = reElem.find(ELEM_HEADER);
        that.layMain = reElem.find(ELEM_MAIN);
        that.layBody = reElem.find(ELEM_BODY);
        that.layFixed = reElem.find(ELEM_FIXED);
        that.layFixLeft = reElem.find(ELEM_FIXL);
        that.layFixRight = reElem.find(ELEM_FIXR);
        that.layTotal = reElem.find(ELEM_TOTAL);
        that.layPage = reElem.find(ELEM_PAGE);

        //初始化工具栏
        that.renderToolbar();

        //让表格平铺
        that.fullSize();

        //如果多级表头，则填补表头高度
        if(options.cols.length > 1){
            //补全高度
            var th = that.layFixed.find(ELEM_HEADER).find('th');
            th.height(that.layHeader.height() - 1 - parseFloat(th.css('padding-top')) - parseFloat(th.css('padding-bottom')));
        }

        that.pullData(that.page); //请求数据
        //that.events(); //事件
    };
    //页码
    Class.prototype.page = 1;
    //根据列类型，定制化参数
    Class.prototype.initOpts = function(item){
        var that = this
            ,options = that.config
            ,initWidth = {
            checkbox: 48
            ,radio: 48
            ,space: 15
            ,numbers: 40
        };

        //让 type 参数兼容旧版本
        if(item.checkbox) item.type = "checkbox";
        if(item.space) item.type = "space";
        if(!item.type) item.type = "normal";

        if(item.type !== "normal"){
            item.unresize = true;
            item.width = item.width || initWidth[item.type];
        }
    };

    //初始化一些参数
    Class.prototype.setInit = function(type){
        var that = this
            ,options = that.config;

        options.clientWidth = options.width || function(){ //获取容器宽度
                //如果父元素宽度为0（一般为隐藏元素），则继续查找上层元素，直到找到真实宽度为止
                var getWidth = function(parent){
                    var width, isNone;
                    parent = parent || options.elem.parent()
                    width = parent.width();
                    try {
                        isNone = parent.css('display') === 'none';
                    } catch(e){}
                    if(parent[0] && (!width || isNone)) return getWidth(parent.parent());
                    return width;
                };
                return getWidth();
            }();

        if(type === 'width') return options.clientWidth;

        //初始化列参数
        layui.each(options.cols, function(i1, item1){
            layui.each(item1, function(i2, item2){

                //如果列参数为空，则移除
                if(!item2){
                    item1.splice(i2, 1);
                    return;
                }

                item2.key = i1 + '-' + i2;
                item2.hide = item2.hide || false;

                //设置列的父列索引
                //如果是组合列，则捕获对应的子列
                if(item2.colGroup || item2.colspan > 1){
                    var childIndex = 0;
                    layui.each(options.cols[i1 + 1], function(i22, item22){
                        //如果子列已经被标注为{HAS_PARENT}，或者子列累计 colspan 数等于父列定义的 colspan，则跳出当前子列循环
                        if(item22.HAS_PARENT || (childIndex > 1 && childIndex == item2.colspan)) return;

                        item22.HAS_PARENT = true;
                        item22.parentKey = i1 + '-' + i2;

                        childIndex = childIndex + parseInt(item22.colspan > 1 ? item22.colspan : 1);
                    });
                    item2.colGroup = true; //标注是组合列
                }

                //根据列类型，定制化参数
                that.initOpts(item2);
            });
        });

    };
    //获得数据
    Class.prototype.pullData = function(curr){
        var that = this
            ,options = that.config
            ,request = options.request
            ,response = options.response
            ,sort = function(){
            if(typeof options.initSort === 'object'){
                that.sort(options.initSort.field, options.initSort.type);
            }
        };

        that.startTime = new Date().getTime(); //渲染开始时间

        if(options.url){ //Ajax请求
            var params = {};
            params[request.pageName] = curr;
            params[request.limitName] = options.limit;

            //参数
            var data = $.extend(params, options.where);
            if(options.contentType && options.contentType.indexOf("application/json") == 0){ //提交 json 格式
                data = JSON.stringify(data);
            }

            $.ajax({
                type: options.method || 'get'
                ,url: options.url
                ,contentType: options.contentType
                ,data: data
                ,dataType: 'json'
                ,headers: options.headers || {}
                ,success: function(res){
                    //如果有数据解析的回调，则获得其返回的数据
                    if(typeof options.parseData === 'function'){
                        res = options.parseData(res) || res;
                    }
                    //检查数据格式是否符合规范
                    if(res[response.statusName] != response.statusCode){
                        that.renderForm();
                        that.layMain.html('<div class="'+ NONE +'">'+ (
                                res[response.msgName] ||
                                ('返回的数据不符合规范，正确的成功状态码 ('+ response.statusName +') 应为：'+ response.statusCode)
                            ) +'</div>');
                    } else {
                        that.renderData(res, curr, res[response.countName]), sort();
                        options.time = (new Date().getTime() - that.startTime) + ' ms'; //耗时（接口请求+视图渲染）
                    }
                    that.setColsWidth();
                    typeof options.done === 'function' && options.done(res, curr, res[response.countName]);
                }
                ,error: function(e, m){
                    that.layMain.html('<div class="'+ NONE +'">数据接口请求异常：'+ m +'</div>');
                    that.renderForm();
                    that.setColsWidth();
                }
            });
        } else if(options.data && options.data.constructor === Array){ //已知数据
            var res = {}
                ,startLimit = curr*options.limit - options.limit

            res[response.dataName] = options.data.concat().splice(startLimit, options.limit);
            res[response.countName] = options.data.length;

            that.renderData(res, curr, options.data.length), sort();
            that.setColsWidth();
            typeof options.done === 'function' && options.done(res, curr, res[response.countName]);
        }
    };

    //遍历表头
    Class.prototype.eachCols = function(callback){
        var that = this;
        table.eachCols(null, callback, that.config.cols);
        return that;
    };

    //数据渲染
    Class.prototype.renderData = function(res, curr, count, sort){
        var that = this
            ,options = that.config
            ,data = res[options.response.dataName] || []
            ,trs = []
            ,trs_fixed = []
            ,trs_fixed_r = []

            //渲染视图
            ,render = function(){ //后续性能提升的重点
                var thisCheckedRowIndex;
                if(!sort && that.sortKey){
                    return that.sort(that.sortKey.field, that.sortKey.sort, true);
                }
                layui.each(data, function(i1, item1){
                    var tds = [], tds_fixed = [], tds_fixed_r = []
                        ,numbers = i1 + options.limit*(curr - 1) + 1; //序号

                    if(item1.length === 0) return;
                    if(!sort){
                        item1[table.config.indexName] = i1;
                    }

                    that.eachCols(function(i3, item3){
                        var field = item3.field || i3
                            ,key = options.index + '-' + item3.key
                            ,content = item1[field];

                        if(content === undefined || content === null) content = '';
                        if(item3.colGroup) return;

                        //td内容
                        var td = ['<td data-field="'+ field +'" data-key="'+ key +'" '+ function(){ //追加各种属性
                            var attr = [];
                            if(item3.edit) attr.push('data-edit="'+ item3.edit +'"'); //是否允许单元格编辑
                            if(item3.align) attr.push('align="'+ item3.align +'"'); //对齐方式
                            if(item3.templet) attr.push('data-content="'+ content +'"'); //自定义模板
                            if(item3.toolbar) attr.push('data-off="true"'); //行工具列关闭单元格事件
                            if(item3.event) attr.push('lay-event="'+ item3.event +'"'); //自定义事件
                            if(item3.style) attr.push('style="'+ item3.style +'"'); //自定义样式
                            if(item3.minWidth) attr.push('data-minwidth="'+ item3.minWidth +'"'); //单元格最小宽度
                            return attr.join(' ');
                        }() +' class="'+ function(){ //追加样式
                            var classNames = [];
                            if(item3.hide) classNames.push(HIDE); //插入隐藏列样式
                            if(!item3.field) classNames.push('layui-table-col-special'); //插入特殊列样式
                            return classNames.join(' ');
                        }() +'">'
                            ,'<div class="layui-table-cell laytable-cell-'+ function(){ //返回对应的CSS类标识
                                return item3.type === 'normal' ? key
                                    : (key + ' laytable-cell-' + item3.type);
                            }() +'">' + function(){
                                var tplData = $.extend(true, {
                                    LAY_INDEX: numbers
                                }, item1)
                                    ,checkName = table.config.checkName;

                                //渲染不同风格的列
                                switch(item3.type){
                                    case 'checkbox':
                                        return '<input type="checkbox" name="layTableCheckbox" lay-skin="primary" '+ function(){
                                                //如果是全选
                                                if(item3[checkName]){
                                                    item1[checkName] = item3[checkName];
                                                    return item3[checkName] ? 'checked' : '';
                                                }
                                                return tplData[checkName] ? 'checked' : '';
                                            }() +'>';
                                        break;
                                    case 'radio':
                                        if(tplData[checkName]){
                                            thisCheckedRowIndex = i1;
                                        }
                                        return '<input type="radio" name="layTableRadio_'+ options.index +'" '
                                            + (tplData[checkName] ? 'checked' : '') +' lay-type="layTableRadio">';
                                        break;
                                    case 'numbers':
                                        return numbers;
                                        break;
                                };

                                //解析工具列模板
                                if(item3.toolbar){
                                    return laytpl($(item3.toolbar).html()||'').render(tplData);
                                }
                                return item3.templet ? function(){
                                        return typeof item3.templet === 'function'
                                            ? item3.templet(tplData)
                                            : laytpl($(item3.templet).html() || String(content)).render(tplData)
                                    }() : content;
                            }()
                            ,'</div></td>'].join('');

                        tds.push(td);
                        if(item3.fixed && item3.fixed !== 'right') tds_fixed.push(td);
                        if(item3.fixed === 'right') tds_fixed_r.push(td);
                    });

                    trs.push('<tr data-index="'+ i1 +'">'+ tds.join('') + '</tr>');
                    trs_fixed.push('<tr data-index="'+ i1 +'">'+ tds_fixed.join('') + '</tr>');
                    trs_fixed_r.push('<tr data-index="'+ i1 +'">'+ tds_fixed_r.join('') + '</tr>');
                });

                that.layBody.scrollTop(0);
                that.layMain.find('.'+ NONE).remove();
                that.layMain.find('tbody').html(trs.join(''));
                that.layFixLeft.find('tbody').html(trs_fixed.join(''));
                that.layFixRight.find('tbody').html(trs_fixed_r.join(''));

                that.renderForm();
                typeof thisCheckedRowIndex === 'number' && that.setThisRowChecked(thisCheckedRowIndex);
                that.syncCheckAll();

                that.scrollPatch();
                /*
                 that.haveInit ? that.scrollPatch() : setTimeout(function(){
                 that.scrollPatch();
                 }, 50);
                 that.haveInit = true;
                 */

                layer.close(that.tipsIndex);

                //同步表头父列的相关值
                options.HAS_SET_COLS_PATCH || that.setColsPatch();
                options.HAS_SET_COLS_PATCH = true;
            };

        that.key = options.id || options.index;
        table.cache[that.key] = data; //记录数据

        //显示隐藏分页栏
        that.layPage[(count == 0 || (data.length === 0 && curr == 1)) ? 'addClass' : 'removeClass'](HIDE);

        //排序
        if(sort){
            return render();
        }

        if(data.length === 0){
            that.renderForm();
            that.layFixed.remove();
            that.layMain.find('tbody').html('');
            that.layMain.find('.'+ NONE).remove();
            return that.layMain.append('<div class="'+ NONE +'">'+ options.text.none +'</div>');
        }

        render(); //渲染数据
        that.renderTotal(data); //数据合计

        //同步分页状态
        if(options.page){
            options.page = $.extend({
                elem: 'layui-table-page' + options.index
                ,count: count
                ,limit: options.limit
                ,limits: options.limits || [10,20,30,40,50,60,70,80,90]
                ,groups: 3
                ,layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
                ,prev: '<i class="layui-icon">&#xe603;</i>'
                ,next: '<i class="layui-icon">&#xe602;</i>'
                ,jump: function(obj, first){
                    if(!first){
                        //分页本身并非需要做以下更新，下面参数的同步，主要是因为其它处理统一用到了它们
                        //而并非用的是 options.page 中的参数（以确保分页未开启的情况仍能正常使用）
                        that.page = obj.curr; //更新页码
                        options.limit = obj.limit; //更新每页条数

                        that.loading();
                        that.pullData(obj.curr);
                    }
                }
            }, options.page);
            options.page.count = count; //更新总条数
            laypage.render(options.page);
        }
    };


    //找到对应的列元素
    Class.prototype.getColElem = function(parent, key){
        var that = this
            ,options = that.config;
        return parent.eq(0).find('.laytable-cell-'+ (options.index + '-' + key) + ':eq(0)');
    };

    //标记当前行选中状态
    Class.prototype.setThisRowChecked = function(index){
        var that = this
            ,options = that.config
            ,ELEM_CLICK = 'layui-table-click'
            ,tr = that.layBody.find('tr[data-index="'+ index +'"]');

        tr.addClass(ELEM_CLICK).siblings('tr').removeClass(ELEM_CLICK);
    };

    //请求loading
    Class.prototype.loading = function(hide){
        var that = this
            ,options = that.config;
        if(options.loading){
            if(hide){
                that.layInit && that.layInit.remove();
                delete that.layInit;
                that.layBox.find(ELEM_INIT).remove()
            } else {
                that.layInit = $(['<div class="layui-table-init">'
                    ,'<i class="layui-icon layui-icon-loading layui-icon"></i>'
                    ,'</div>'].join(''));
                that.layBox.append(that.layInit);
            }
        }
    };

    //让表格铺满
    Class.prototype.fullSize = function(){
        var that = this
            ,options = that.config
            ,height = options.height, bodyHeight;

        if(that.fullHeightGap){
            height = _WIN.height() - that.fullHeightGap;
            if(height < 135) height = 135;
            that.elem.css('height', height);
        }

        if(!height) return;

        //减去列头区域的高度
        bodyHeight = parseFloat(height) - (that.layHeader.outerHeight() || 38); //此处的数字常量是为了防止容器处在隐藏区域无法获得高度的问题，暂时只对默认尺寸的表格做支持。

        //减去工具栏的高度
        if(options.toolbar){
            bodyHeight = bodyHeight - (that.layTool.outerHeight() || 50);
        }

        //减去统计朗的高度
        if(options.totalRow){
            bodyHeight = bodyHeight - (that.layTotal.outerHeight() || 40);
        }

        //减去分页栏的高度
        if(options.page){
            bodyHeight = bodyHeight - (that.layPage.outerHeight() || 41) - 2;
        }

        that.layMain.css('height', bodyHeight);
    };

    //获取滚动条宽度
    Class.prototype.getScrollWidth = function(elem){
        var width = 0;
        if(elem){
            width = elem.offsetWidth - elem.clientWidth;
        } else {
            elem = document.createElement('div');
            elem.style.width = '100px';
            elem.style.height = '100px';
            elem.style.overflowY = 'scroll';

            document.body.appendChild(elem);
            width = elem.offsetWidth - elem.clientWidth;
            document.body.removeChild(elem);
        }
        return width;
    };

    //滚动条补丁
    Class.prototype.scrollPatch = function(){
        var that = this
            ,layMainTable = that.layMain.children('table')
            ,scollWidth = that.layMain.width() - that.layMain.prop('clientWidth') //纵向滚动条宽度
            ,scollHeight = that.layMain.height() - that.layMain.prop('clientHeight') //横向滚动条高度
            ,getScrollWidth = that.getScrollWidth(that.layMain[0]) //获取主容器滚动条宽度，如果有的话
            ,outWidth = layMainTable.outerWidth() - that.layMain.width() //表格内容器的超出宽度

            //添加补丁
            ,addPatch = function(elem){
                if(scollWidth && scollHeight){
                    elem = elem.eq(0);
                    if(!elem.find('.layui-table-patch')[0]){
                        var patchElem = $('<th class="layui-table-patch"><div class="layui-table-cell"></div></th>'); //补丁元素
                        patchElem.find('div').css({
                            width: scollWidth
                        });
                        elem.find('tr').append(patchElem);
                    }
                } else {
                    elem.find('.layui-table-patch').remove();
                }
            }

        addPatch(that.layHeader);
        addPatch(that.layTotal);

        //固定列区域高度
        var mainHeight = that.layMain.height()
            ,fixHeight = mainHeight - scollHeight;
        that.layFixed.find(ELEM_BODY).css('height', layMainTable.height() > fixHeight ? fixHeight : 'auto');

        //表格宽度小于容器宽度时，隐藏固定列
        that.layFixRight[outWidth > 0 ? 'removeClass' : 'addClass'](HIDE);

        //操作栏
        that.layFixRight.css('right', scollWidth - 1);
    };

    var obj = {
        render: function (options) {
        },
        refresh: function (options) {
        },
        getRowData: function (id) {
        }
    };
    exports('mtable', obj);
});