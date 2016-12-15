<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<style>
    <!--
    -->
    .none {
        display: none;
    }
</style>
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
        <div class="modal-header bg-primary">
            <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="myModalLabel">模版消息发送</h4>
        </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div class="row">
                    <form action="<c:url value='/agent/sendMessage.json'/>" name="editForm"
                          class="form-horizontal" id="editForm" method="post">
                        <ul class="nav nav-tabs" role="tablist">
                            <li role="presentation" class="active"><a class="tabClick" href="#basicInfo" aria-controls="basicInfo" role="tab" data="-8Vjt5YMTkOw_XdiTuC0kgoOQwXjOBYIuupwh1NRdME" data-toggle="tab">活动模版</a></li>
                            <li role="presentation"><a href="#authInfo" class="tabClick" aria-controls="authInfo" role="tab" data-toggle="tab" data="4s8byn6g55EJ2ngJ8lduMbC-Vtl8L_LcPbIbcZZBM6M">产品模版</a></li>
                            <%--<li role="presentation"><a href="#authedInfo" aria-controls="authedInfo" role="tab" data-toggle="tab">已有授权</a></li>--%>
                        </ul>
                        <div class="tab-content">
                            <!--模版ID默认第一个模版-->
                            <input type="hidden" name="template_id" id="template_id" value="-8Vjt5YMTkOw_XdiTuC0kgoOQwXjOBYIuupwh1NRdME">
                            <div role="tabpanel" class="tab-pane active" id="basicInfo">
                                <div class="col-md-12 col-xs-12">
                                    <div class="panel panel-danger">
                                        <div class="panel-heading">填写活动通知</div>
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <label for="title" class="col-sm-2 control-label">通知标题</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control" required autofocus="autofocus"
                                                           id="title" tabIndex="12" name="title"
                                                           />
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-2 control-label">跳转网址</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                           id="url" tabIndex="12"
                                                           name="url" />

                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-2 control-label">活动主题</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                           id="name" tabIndex="12"
                                                           name="keywords"
                                                          >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="url" class="col-sm-2 control-label">变更时间</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                            tabIndex="12" name="keywords"
                                                          >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="url" class="col-sm-2 control-label">活动时间</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                            tabIndex="12" name="keywords"
                                                         >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="remark" class="col-sm-2 col-xs-12 control-label">主要内容</label>
                                                <div class="col-sm-6 col-xs-12">
                                                    <textarea class="form-control" id="remark" rows="3" name="remark"></textarea>
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error">填写内容</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="authInfo">
                                <div class="col-md-12 col-xs-12">
                                    <div class="panel panel-danger">
                                        <div class="panel-heading">填写产品通知</div>
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <%--<input type="hidden" name="template_id" value="">--%>
                                                <label for="title" class="col-sm-2 control-label">通知标题</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control" required autofocus="autofocus"
                                                           id="title" tabIndex="12" name="title"
                                                           >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-2 control-label">跳转网址</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                          tabIndex="12"
                                                           name="url"
                                                          >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-2 control-label">产品类型</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                            tabIndex="12"
                                                           name="keywords"
                                                           >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="url" class="col-sm-2 control-label">产品名称</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                            tabIndex="12" name="keywords"
                                                           >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="url" class="col-sm-2 control-label">时间</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                           tabIndex="12" name="keywords"
                                                    >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="url" class="col-sm-2 control-label">状态</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control"
                                                           tabIndex="12" name="keywords"
                                                    >
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="remark" class="col-sm-2 col-xs-12 control-label">主要内容</label>
                                                <div class="col-sm-6 col-xs-12">
                                                    <textarea class="form-control" id="remark" rows="3" name="remark"></textarea>
                                                </div>
                                                <div class="col-md-4 col-xs-4 text-error">填写内容</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="authedInfo">
                                <%--<div class="col-md-12 col-xs-12">--%>
                                    <%--<div class="panel panel-danger">--%>
                                        <%--<div class="panel-heading">填写通知基本信息</div>--%>
                                        <%--<div class="panel-body">--%>
                                            <%--<div class="form-group">--%>
                                                <%--<input type="hidden" name="template_id" value="LPzrutfPighnXS9ylCIKTlT6afs3o3aDKo2Qfj7YSGE">--%>
                                                <%--<label for="title" class="col-sm-2 control-label">通知标题</label>--%>
                                                <%--<div class="col-sm-4">--%>
                                                    <%--<input type="text" class="form-control" required autofocus="autofocus"--%>
                                                           <%--id="title" tabIndex="12" name="title"--%>
                                                           <%--value="" placeholder="输入标题">--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-4 col-xs-4 text-error"></div>--%>
                                            <%--</div>--%>
                                            <%--<div class="form-group">--%>
                                                <%--<label for="name" class="col-sm-2 control-label">跳转网址</label>--%>
                                                <%--<div class="col-sm-4">--%>
                                                    <%--<input type="text" class="form-control"--%>
                                                           <%--id="url" tabIndex="12"--%>
                                                           <%--name="url" value=""--%>
                                                           <%--placeholder="跳转网址">--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-4 col-xs-4 text-error"></div>--%>
                                            <%--</div>--%>
                                            <%--<div class="form-group">--%>
                                                <%--<label for="name" class="col-sm-2 control-label">活动名称</label>--%>
                                                <%--<div class="col-sm-4">--%>
                                                    <%--<input type="text" class="form-control"--%>
                                                           <%--id="name" tabIndex="12"--%>
                                                           <%--name="keywords" value=""--%>
                                                           <%--placeholder="活动名称">--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-4 col-xs-4 text-error"></div>--%>
                                            <%--</div>--%>
                                            <%--<div class="form-group">--%>
                                                <%--<label for="url" class="col-sm-2 control-label">活动时间</label>--%>
                                                <%--<div class="col-sm-4">--%>
                                                    <%--<input type="text" class="form-control"--%>
                                                           <%--id="url" tabIndex="12" name="keywords" value=""--%>
                                                           <%--placeholder="时间">--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-4 col-xs-4 text-error"></div>--%>
                                            <%--</div>--%>
                                            <%--<div class="form-group">--%>
                                                <%--<label for="remark" class="col-sm-2 col-xs-12 control-label">主要内容</label>--%>
                                                <%--<div class="col-sm-6 col-xs-12">--%>
                                                    <%--<textarea class="form-control" id="remark" rows="3" name="remark"></textarea>--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-4 col-xs-4 text-error">填写内容</div>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <div class="modal-footer">
            <div class="form-group">
                <div class="col-md-6 col-xs-12">
                    <button type="button" class="btn btn-default btn-block quitBtn"
                            tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
                            value="login" tabindex="4" data-loading-text="正在返回......">
                        <span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
                    </button>
                </div>
                <div class="col-md-6 col-xs-12">
                    <button type="button" class="btn btn-primary btn-block"
                            form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
                            value="saveBtn" tabindex="4" data-loading-text="正在提交......">
                        <span class="glyphicon glyphicon-save">&nbsp;</span>保存
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
<script type="text/javascript">
    $(function() {
        init();
    });
    function init() {
        $(".tabClick").click(function(e){
            $('#editForm')[0].reset();
            $("#template_id").val($(this).attr("data"));
        });
        var btn = null;
        $("#editForm")
                .validate(
                        {
                            submitHandler : function(form) {
                                $(form)
                                        .ajaxSubmit(
                                                {
                                                    beforeSubmit : function(
                                                            arr, $form, options) {
                                                        btn.button("loading");
                                                    },
                                                    success : function(
                                                            responseText,
                                                            statusText, xhr,
                                                            $form) {
                                                        var m = $
                                                                .parseJSON(xhr.responseText);
                                                        btn.button("reset");
                                                        if (m.flag == "0") {
                                                            alert(m.data);
                                                            $(".quitBtn")
                                                                    .click();
                                                            location.reload();
                                                        } else {
                                                            alert("发送失败");
                                                        }

                                                    },
                                                    error : function(xhr,
                                                                     textStatus,
                                                                     errorThrown) {
                                                        btn.button("reset");
                                                        alert("保存失败");
                                                        var m = $
                                                                .parseJSON(xhr.responseText);

                                                    }
                                                });
                            },
                            rules : {
                                title : {
                                    required : true
                                },
                                remark : {
                                    required : false
                                }

                            },
                            onkeyup : false,
                            messages : {
                                title : {
                                    required : "请输入代理姓名"
                                },
                                remark:{
                                    required: "请输入内容"
                                }
                            },
                            focusInvalid : true,
                            errorClass : "text-danger",
                            validClass : "valid",
                            errorElement : "small",
                            errorPlacement : function(error, element) {
                                error.appendTo(element
                                        .closest("div.form-group").children(
                                                "div.text-error"));
                            }
                        });
        $("#saveBtn").click(function(e) {
            $("input[type='text']").each(function () {
                    if($(this).val()==""){
                        $(this).attr("disabled",true);
                    }
            });

            $("textarea").each(function () {
                if($(this).val()==""){
                    $(this).attr("disabled",true);
                }
            });
            btn = $(this).button();
            $(document.forms["editForm"]).submit();
        });
    }
</script>
