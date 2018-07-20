var validator;
var $applyPrintForm = $("#apply-print-form");
$(function() {
    validateRule();
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
        uniqueId:'rId',
        buttonsToolbar: '#toolbar',//工具按钮用哪个容器
        buttonsAlign:'right',
        columns: [{
                field: 'rId',
                visible: false
            }, {
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
            title: '操作',
            field: 'operate',
            valign: 'middle',
            align: 'center',
            formatter: function(value, row, index) {
                return '<a class="btn-sm btn-outline-info sheet-print"  id="'+row.rId+'"><i class="zmdi zmdi-print"/>&nbsp;打印</a>';
            }
        }

        ]
    }
    $MB.initTable('dataTable', settings);
    /*按钮组点击事件*/
    $(".btn-group-justified .btn-outline-info").on("click",function () {
        var _this =$(this);
        $(".btn-group-justified .btn-outline-info").removeClass("active");
        _this.addClass("active");
        search();
    });
    $("table").on("click", ".sheet-print", function (r) {
        var _this=$(this);
        var data=$MB.getRowData('dataTable', _this.attr("id"));
        var $form = $('#apply-print');
        $form.modal();
        $form.find("input[name='rId']").val(data.rId);
        $form.find("input[name='rName']").val(data.rSolName);
        $form.find("select[name='apType']").val("1");//记录单
        $form.find("input[name='apType']").val("1");//记录单
    });
    $("#apply-print .btn-close").click(function() {
        closeModal();
    });

    $("#apply-print .btn-save").click(function() {
        var name = $(this).attr("name");
        var validator = $applyPrintForm.validate();
        var flag = validator.form();
        if (flag) {
            $MB.ajaxPost($(this),{url:ctx + "record/applySheet",data:$applyPrintForm.serialize()},function (r) {
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
function closeModal() {
    validator.resetForm();
    $MB.closeAndRestModal("apply-print");
}
function refresh() {
    $(".sheet-table-form")[0].reset();
    $MB.refreshTable('dataTable');
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
function wider(widerId,narrowerId) {
    $('#'+narrowerId).removeClass('col-sm-6').addClass('col-sm-3');
    $('#'+widerId).removeClass('col-sm-3').addClass('col-sm-6');
}