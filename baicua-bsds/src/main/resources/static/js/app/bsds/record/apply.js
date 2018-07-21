$(function() {
    var settings = {
        url: ctx + "recordApply/list",
        pageSize: 10,
        queryParams: function(params) {
            var appType = $(".btn-group-justified").children(" .btn-outline-info.active").attr("data-name");
            var sheetType='1';
            if ('1'==appType){
                sheetType = '1';
                appType='1';
            }else  if ('2'==appType){
                sheetType = '2';
                appType='1';
            }else  if ('3'==appType){
                sheetType = '';
                appType='2';
            }else {
                sheetType = '';
                appType='';
            }
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                rName: $(".apply-table-form").find("input[name='rName']").val(),
                apType:appType,
                sheetType :sheetType
            };
        },
        columns: [{
                field: 'apId',
                visible: false
            }, {
            field: 'sheetType',
            visible: false
            }, {
                field: 'apDeptName',
                title: '申请部门'
            }, {
                field: 'apName',
                title: '申请人'
            },{
                field: 'apType',
                title: '分类',
                formatter: function(value, row, index) {
                    if (value == '1'&&row.sheetType=='1') return '流动相(记录单)';
                    else if (value == '1'&&row.sheetType=='2') return '标准溶液(记录单)';
                    else if (value == '2') return '记录本';
                    else return '-';
                }
            }, {
                field: 'rName',
                title: '溶液名称/记录本分类'
            },{
                field: 'apDate',
                title: '申请时间'
            }

        ]
    }
    $MB.initTable('recordApplyTable', settings);
    /*按钮组点击事件*/
    $(".btn-group-justified .btn-outline-info").on("click",function () {
        var _this =$(this);
        $(".btn-group-justified .btn-outline-info").removeClass("active");
        _this.addClass("active");
        search();
    })
});

function search() {
    $MB.refreshTable('recordApplyTable');
}

function refresh() {
    $(".apply-table-form")[0].reset();
    $MB.refreshTable('recordApplyTable');
}

function deleteUsers() {
    var selected = $("#recordApplyTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的记录本！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].userId;
        if (i != (selected_length - 1)) ids += ",";
        if (userName == selected[i].username) contain = true;
    }
    if (contain) {
        $MB.n_warning('勾选用户中包含当前登录用户，无法删除！');
        return;
    }

    $MB.confirm({
        text: "确定删除选中用户？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'user/delete', { "ids": ids }, function(r) {
            if (r.code == 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}