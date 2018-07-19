$(function() {
    var settings = {
        url: ctx + "recordBook/list",
        pageSize: 10,
        queryParams: function(params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                rName: $(".book-table-form").find("input[name='rName']").val(),
            };
        },
        columns: [{
                checkbox: true
            }, {
                field: 'rId',
                visible: false
            }, {
                field: 'rName',
                title: '分类'
            }, {
                field: 'rDept',
                title: '部门'
            }, {
                field: 'rPro',
                title: '前缀'
            }, {
                field: 'rCrtDate',
                title: '创建时间'
            }, {
                field: 'rUpdDate',
                title: '修改时间'
            }, {
                field: 'rUpdPer',
                title: '修改用户'
            }, {
                field: 'rVesion',
                title: '版本号',
            }

        ]
    }
    $MB.initTable('recordBookTable', settings);
});

function search() {
    $MB.refreshTable('recordBookTable');
}

function refresh() {
    $(".book-table-form")[0].reset();
    $MB.refreshTable('recordBookTable');
}
function deleteBook() {
    var selected = $("#recordBookTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的记录本！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].rId;
        if (i != (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中记录本？",
        confirmButtonText: "确定删除"
    }, function() {
        $MB.ajaxPost($(this),{url:ctx + "record/book/delete",data:{ "ids": ids }},function (r) {
            if (r.code == 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}