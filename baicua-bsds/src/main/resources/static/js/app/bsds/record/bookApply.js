var validator;
var $applyPrintForm = $("#apply-print-form");
$(function() {
    validateRule();
    var settings = {
        url: ctx + "recordBook/list",
        pageSize: 10,
        uniqueId:'rId',
        queryParams: function(params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                rName: $(".book-table-form").find("input[name='rName']").val(),
            };
        },
        columns: [ {
                field: 'rId',
                visible: false
            }, {
                field: 'rName',
                title: '分类'
            }, {
                field: 'rDept',
                title: '部门'
            }, {
                field: 'rVesion',
                title: '版本号',
            },{
            title: '操作',
            field: 'operate',
            valign: 'middle',
            align: 'center',
            formatter: function(value, row, index) {
                return '<a class="btn-sm btn-outline-info sheet-print"  id="'+row.rId+'"><i class="zmdi zmdi-print"/>&nbsp;打印</a>';
            }
        }

        ]
    };
    $MB.initTable('dataTable', settings);

    $("table").on("click", ".sheet-print", function (r) {
        var _this=$(this);
        var data=$MB.getRowData('dataTable', _this.attr("id"));
        var $form = $('#apply-print');
        $form.modal();

        $form.find("input[name='rId']").val(data.rId);
        $form.find("input[name='rName']").val(data.rName);
        $("#applyRecordName").val(data.rName);
        $form.find("select[name='apType']").val("2");//记录本
        $form.find("input[name='apType']").val("2");//记录本
    });
    $("#apply-print .btn-close").click(function() {
        closeModal();
    });

    $("#apply-print .btn-save").click(function() {
        var name = $(this).attr("name");
        var validator = $applyPrintForm.validate();
        var flag = validator.form();
        if (flag) {
            $MB.ajaxPost($(this),{url:ctx + "record/applyBook",data:$applyPrintForm.serialize()},function (r) {
                if (r.code == 0) {
                    closeModal();
                    $MB.n_success(r.msg);
                } else $MB.n_danger(r.msg);
            });
        }
    });
});

function search() {
    $MB.refreshTable('dataTable');
}

function refresh() {
    $(".book-table-form")[0].reset();
    $MB.refreshTable('dataTable');
}
function closeModal() {
    validator.resetForm();
    $MB.closeAndRestModal("apply-print");
}
function validateRule() {
    validator = $applyPrintForm.validate({
        rules: {
        },
        errorPlacement: function(error, element) {
            if (element.is(":checkbox") || element.is(":radio")) {
                error.appendTo(element.parent().parent());
            } else {
                error.insertAfter(element);
            }
        },
        messages: {}
    });
}