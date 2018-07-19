var validator;
var $sheetAddForm = $("#sheet-add-form");
$(function() {
    validateRule();
    $("#sheet-add .btn-save").click(function() {
        var name = $(this).attr("name");
        var validator = $sheetAddForm.validate();
        var flag = validator.form();
        if (flag) {
            $MB.ajaxPost($(this),{url:ctx + "record/sheet/update",data:$sheetAddForm.serialize()},function (r) {
                if (r.code == 0) {
                    closeModal();
                    $MB.n_success(r.msg);
                    $MB.refreshTable("recordSheetTable");
                } else $MB.n_danger(r.msg);
            });
        }
    });

    $("#sheet-add .btn-close").click(function() {
        closeModal();
    });

});
function closeModal() {
    $("#sheet-add-button").attr("name", "save");
    validator.resetForm();
    $MB.closeAndRestModal("sheet-add");
    $("#sheet-add-modal-title").html('新增记录单');
}

function validateRule() {
    validator = $sheetAddForm.validate({
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
function updateSheet() {
    var selected = $("#recordSheetTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的记录单！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个记录单！');
        return;
    }
    var rId = selected[0].rId;
    $.post(ctx + "record/getSheet", { "rId": rId }, function(r) {
        if (r.code == 0) {
            var $form = $('#sheet-add');
            $form.modal();
            var sheet = r.msg;
            $("#sheet-add-modal-title").html('修改记录单');
            $form.find("input[name='rId']").val(sheet.rId);
            $form.find("input[name='rCode']").val(sheet.rCode);
            $form.find("select[name='rType']").val(sheet.rType);
            $form.find("input[name='rSolName']").val(sheet.rSolName);
            $form.find("input[name='rPotency']").val(sheet.rPotency);
            $form.find("input[name='rRefMethod']").val(sheet.rRefMethod);
            $form.find("input[name='rVesion']").val(sheet.rVesion);
            $("#sheet-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}