<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/view/common/common.jsp" %>
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="myModalLabel">上传代理特权订单编号</h4>
        </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div class="row">
                    <form action="<c:url value='/delivery/uploadOrdersNumber.json'/>" name="editForm"
                          class="form-horizontal" id="editForm" method="post">
                        <div class="panel panel-primary">
                            <div class="panel-heading">选择文件</div>
                            <div class="panel-body">
                                <div class="col-md-12 col-xs-12">
                                    <div class="form-group">
                                        <input type="file" class="form-control" id="file" tabIndex="12"
                                               name="file" placeholder="产品图片">
                                        <div class="col-md-4 col-xs-4 text-error"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
        </form>
    </div>
</div>
<div class="modal-footer">
    <div class="form-group">
        <div class="col-md-6 col-xs-6">
            <button type="button" class="btn btn-default btn-block quitBtn btn-lg"
                    tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
                    value="login" tabindex="4" data-loading-text="正在返回......">
                <span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
            </button>
        </div>
        <div class="col-md-6 col-xs-6">
            <button type="button" class="btn btn-danger btn-block btn-lg"
                    form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
                    value="saveBtn" tabindex="4" data-loading-text="正在提交......">
                <span class="glyphicon glyphicon-ok">&nbsp;</span>上传确认
            </button>
        </div>
    </div>
</div>
</div>
</div>




<script type="text/javascript">
    $(function () {
        init();
    });
    function init() {
        var btn = null;
        $("#editLogisticsCode").focus();
        $("#editForm")
                .validate(
                        {
                            submitHandler: function (form) {
                                $(form)
                                        .ajaxSubmit(
                                                {
                                                    beforeSubmit: function (arr, $form, options) {
                                                        btn.button("loading");
                                                    },
                                                    success: function (responseText,
                                                                       statusText, xhr,
                                                                       $form) {
                                                        var m = $
                                                                .parseJSON(xhr.responseText);
                                                        btn.button("reset");
                                                        if (m.flag == "0") {
                                                            alert("上传单号操作成功");
                                                            $("#search").click();
                                                            $(".quitBtn").click();
                                                            window.open(
                                                                    "<c:url value='/pm/order/shipping/print.html?id=${parameter.entity.id}'/>",
                                                                    "print",
                                                                    "toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
                                                        } else {
                                                            alert("上传操作失败,"
                                                                    + m.message);
                                                        }

                                                    },
                                                    error: function (xhr,
                                                                     textStatus,
                                                                     errorThrown) {
//                                                        var m = $
//                                                                .parseJSON(xhr.responseText);
                                                        btn.button("reset");
                                                        alert("上传操作失败"+xhr.responseText);
                                                    }
                                                });
                            },
                            rules: {},
                            onkeyup: false,
                            messages: {},
                            focusInvalid: true,
                            errorClass: "text-danger",
                            validClass: "valid",
                            errorElement: "small",
                            errorPlacement: function (error, element) {
                                error.appendTo(element
                                        .closest("div.form-group").children(
                                                "div.text-error"));
                            }
                        });
        $("#editForm").find("input[type='checkbox']").change(function (e) {
            var $t = $(this);
            var next = $t.next("input[type='hidden']");
            $t.prop("checked") ? next.val(1) : next.val(0);
        });
        $("#saveBtn").click(function (e) {
            btn = $(this).button();
            $(document.forms["editForm"]).submit();
        });
    }
</script>