$(function() {
    var settings = {
        url: ctx + "recordSheet/list",
        pageSize: 10,
        queryParams: function(params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                rSolName: $(".sheet-table-form").find("input[name='rSolName']").val(),
                rType: $(".btn-group-justified").children(" .btn-outline-info.active").attr("data-name")
            };
        },
        columns: [{
                checkbox: true
            }, {
                field: 'rId',
                visible: false
            }, {
            field: 'rVesion',
            visible: false
            },{
                field: 'rCode',
                title: '模板编号'
            }, {
                field: 'rType',
                title: '分类',
                formatter: function(value, row, index) {
                    if (value == '1') return '实验室溶液配制记录模板（流动相）';
                    else if (value == '2') return '实验室溶液配制记录模板（标准溶液）';
                    else return '-';
                }
            }, {
                field: 'rSolName',
                title: '溶液名称'
            }, {
                field: 'rPotency',
                title: '浓度'
            }, {
                field: 'rRefMethod',
                title: '参考方法'
            }, {
                field: 'rUpdDate',
                title: '修改时间'
            }, {
                field: 'rUpdPer',
                title: '修改用户',
            }

        ]
    }
    $MB.initTable('recordSheetTable', settings);
    /*按钮组点击事件*/
    $(".btn-group-justified .btn-outline-info").on("click",function () {
        var _this =$(this);
        $(".btn-group-justified .btn-outline-info").removeClass("active");
        _this.addClass("active");
        search();
    })
});

function search() {
    $MB.refreshTable('recordSheetTable');
}

function refresh() {
    $(".sheet-table-form")[0].reset();
    $MB.refreshTable('recordSheetTable');
}

function deleteSheet() {
    var selected = $("#recordSheetTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的记录单！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].rId;
        if (i != (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中记录单？",
        confirmButtonText: "确定删除"
    }, function() {
        $MB.ajaxPost($(this),{url:ctx + "record/sheet/delete",data:{ "ids": ids }},function (r) {
            if (r.code == 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}