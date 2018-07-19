var validator;
var $bookAddForm = $("#book-add-form");
$(function() {
    validateRule();
    $("#book-add .btn-save").click(function() {
        var name = $(this).attr("name");
        var validator = $bookAddForm.validate();
        var flag = validator.form();
        if (flag) {
            $MB.ajaxPost($(this),{url:ctx + "record/book/update",data:$bookAddForm.serialize()},function (r) {
                if (r.code == 0) {
                    closeModal();
                    $MB.n_success(r.msg);
                    $MB.refreshTable("recordBookTable");
                } else $MB.n_danger(r.msg);
            });
        }
    });

    $("#book-add .btn-close").click(function() {
        closeModal();
    });

});
function closeModal() {
    $("#book-add-button").attr("name", "save");
    validator.resetForm();
    $MB.closeAndRestModal("book-add");
    $("#book-add-modal-title").html('新增记录本');
}

function validateRule() {
    validator = $bookAddForm.validate({
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
function updateBook() {
    var selected = $("#recordBookTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的记录本！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个记录本！');
        return;
    }
    var rId = selected[0].rId;
    $.post(ctx + "record/getBook", { "rId": rId }, function(r) {
        if (r.code == 0) {
            var $form = $('#book-add');
            $form.modal();
            var book = r.msg;
            $("#book-add-modal-title").html('修改记录本');
            $form.find("input[name='rId']").val(book.rId);
            $form.find("input[name='rName']").val(book.rName);
            $form.find("select[name='rDeptId']").val(book.rDeptId);
            $form.find("input[name='rSerialNum']").val(book.rSerialNum);
            $form.find("input[name='rVesion']").val(book.rVesion);
            $("#book-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}