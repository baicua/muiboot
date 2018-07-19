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
                return '<a class="btn-sm btn-outline-info sheet-print" data-toggle="modal" data-target="#apply-print" index="'+index+'"><i class="zmdi zmdi-print"/>&nbsp;打印</a>';
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
    $("#apply-print .btn-close").click(function() {
        closeModal();
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