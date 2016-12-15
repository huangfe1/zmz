<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/view/common/common.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="${keywords}">
    <meta http-equiv="description" content="">
    <%@include file="/WEB-INF/view/common/head_css.jsp" %>
    <%@include file="/WEB-INF/view/common/head_css_typeahead.jsp" %>
    <%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp" %>
    <%@include file="/WEB-INF/view/common/datepicker_css.jsp" %>
    <%@include file="/WEB-INF/view/common/head_css_fav.jsp" %>
    <!--引入微信的js-->
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <title>产品防伪码管理</title>
    <style>
        .input-daterange {
            width: inherit !important;
        }

        td {
            word-break: keep-all;
        }
    </style>
</head>
<body>
<form id="editForm" action="<c:url value='/securityCode/scanEdit.json'/>" method="POST">
    <div id="wrapper">
        <jsp:include page="/menu.html"></jsp:include>
        <div id="page-wrapper">
            <nav class="navbar navbar-default" role="navigation">
                <div class="container-fluid">
                    <!-- <div class="collapse navbar-collapse" id="top-navbar-collapse"> -->
                    <div class="navbar-form navbar-left">
                        <div class="form-group">
                            <label class="">产品名称</label> <input required type="text"
                                                                value="心生爱目" name="goodsName" id="goodsName"
                                                                autofocus class="typeahead form-control"
                                                                placeholder="产品名称">
                            <label class="">货物持有人</label> <input required type="text" value="${param.owner}"
                                                                 name="owner" id="owner"
                                                                 autofocus class="typeahead form-control"
                                                                 placeholder="代理编号">
                            <label class="">货物持有人编号</label> <input required type="text"
                                                                   name="agentCode" id="agentCode"
                                                                   autofocus class="typeahead form-control"
                                                                   placeholder="代理姓名" value="${param.agentCode}">
                            <button type="button" data-toggle="tooltip" data-url="" data-placement="bottom"
                                    title="点击录入新的防伪码"
                                    class="btn btn-info navbar-btn scanBtn">
                                <li class="fa fa-search"></li>
                                扫一扫
                            </button>


                            <button type="button" class="btn btn-primary" id="sbtn" data-toggle="tooltip"
                                    data-placement="bottom" data-loading-text="正在录入......">
                                <li class="fa fa-edit"></li>&nbsp;点击录入
                            </button>




                        </div>
                        <%--<ul class="nav navbar-nav navbar-right">--%>
                        <%--<li>--%>
                        <%--<button type="button" data-toggle="tooltip"--%>
                        <%--data-url="<c:url value='/securityCode/edit_num.html'/>" data-placement="bottom"--%>
                        <%--title="点击录入新的防伪码"--%>
                        <%--class="btn btn-info navbar-btn addBtn">--%>
                        <%--<li class="fa fa-plus fa-fw"></li>--%>
                        <%--按数量录入--%>
                        <%--</button>&nbsp;</li>--%>
                        <%--<li>--%>
                        <%--<button type="button" data-toggle="tooltip"--%>
                        <%--data-url="<c:url value='/securityCode/edit.html'/>" data-placement="bottom"--%>
                        <%--title="点击录入新的防伪码"--%>
                        <%--class="btn btn-info navbar-btn addBtn">--%>
                        <%--<li class="fa fa-plus fa-fw"></li>--%>
                        <%--录入防伪码(带删除)--%>
                        <%--</button></li>--%>
                        <%--<li>--%>
                        <%--</ul>--%>
                        <!-- </div> -->
                    </div>
                </div>
            </nav>
            <div class="row">
                <div class="col-lg-12 col-md-12">
                    <div class="table-responsive">
                        <table
                                class="table table-striped table-bordered table-hover">
                            <thead>
                            <tr>
                                <th>防伪码</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="dataList">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <%--<div class="row">--%>
            <%--<div class="col-md-12 col-sm-12 col-xs-12">--%>
            <%--<jsp:include page="/WEB-INF/view/common/pager.jsp"></jsp:include>--%>
            <%--</div>--%>
            <%--</div>--%>
        </div>

    </div>
</form>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body" id="messageBody"></div>
            <div class="modal-footer">
                <div class="col-md-12 col-xs-12">
                    <button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
                            id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
                            tabindex="4" data-loading-text="正在返回......">
                        <span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<script type="text/javascript">
    var arr=[];

    var aclick = function (a) {
        arr.splice($.inArray($("#tid" + a).val(),arr),1);
        $("#trid" + a).remove();
    };

    $(function () {
        var jp = ${jsapiParamJson}
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: jp.appId, // 必填，公众号的唯一标识
                    timestamp: jp.timestamp, // 必填，生成签名的时间戳
                    nonceStr: jp.nonceStr, // 必填，生成签名的随机串
                    signature: jp.signature,// 必填，签名，见附录1
                    jsApiList: [
                        'scanQRCode'
                    ]
                });


        var index = 0;


        $(".scanBtn").click(function () {

            var code = "";

            index++;
            //扫一扫
            wx.scanQRCode({
                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                success: function (res) {
                    code = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                    if(code.indexOf("zmz365")>=0){
                        var sc=code.substr(code.lastIndexOf("=")+1,code.length);
                        if(code.indexOf("code")>=0){
                            if($.inArray(i,arr)==-1){
                                $("#dataList").append("<tr id=trid" + index + "><td>" + sc + "<input id=tid"+index+" type='hidden' name='codes' value=" + sc + "></td><td ><a onclick='aclick(" + index + ")' class='btn btn-danger default ajaxLink'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span>删除</a></td></tr>");
                                arr.push(sc);
                            }else{
                                alert("重复录入"+sc);
                            }

                        }else if(code.indexOf("boxs")>=0){
                            var start=parseInt(sc.substr(0,code.indexOf("-")-1));
                            var end=parseInt(sc.substr(sc.lastIndexOf("-")+1,sc.length));
                            if(start>=end){
                                alert("二维码不合法");
                            }else{
                                for(var i=start;i<=end;i++){
                                    if($.inArray(i,arr)==-1){
                                        $("#dataList").append("<tr id=trid" + index + "><td>" + i + "<input id=tid"+index+" type='hidden' name='codes' value=" + i + "></td><td ><a onclick='aclick(" + index + ")' class='btn btn-danger default ajaxLink'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span>删除</a></td></tr>");
                                        index++;
                                        arr.push(i);
                                    }else{
                                        alert("重复录入"+i);
                                    }

                                }
                            }

                        }
                        $(".scanBtn").trigger("click");
                    }else{
                        alert("二维码有误");
                    }

                }
            });
        });

        /**
         * 提交
         */


        $("#sbtn").click(function () {
            $("#editForm").validate({
                submitHandler: function (form) {
                    $(form).ajaxSubmit({
                        beforeSubmit: function (arr, $form, options) {
                            $("#sbtn").button("loading");
                        },
                        success: function (responseText,
                                           statusText, xhr, $form) {
                            var m = $.parseJSON(xhr.responseText);
                            $("#sbtn").button("reset");
                            if (m.flag == "0") {
                                alert("录入成功");
//                            location.reload();
                            } else {
                                alert("录入失败"+xhr.responseText);
                            }

                        },
                        error: function (xhr, textStatus,
                                         errorThrown) {
                            $("#sbtn").button("reset");
                            alert("录入失败" + xhr.responseText);
                        }
                    });
                },
                rules: {
                    goodsName: {
                        required: true
                    },
                    owner: {
                        required: true,
                    },
                    agentCode: {
                        required: true,
                    }
                },
                onkeyup: false,
                messages: {
                    goodsName: {
                        required: "产品名字必填"
                    },
                    owner: {
                        required: "代理名字必须填写",
                    },
                    agentCode: {
                        required: "代理编号必须填写",
                    }
                },
                focusInvalid: true,
                errorClass: "text-danger",
                validClass: "valid",
                errorElement: "small",
                errorPlacement: function (error, element) {
                    error.appendTo(element.closest("div.form-group")
                            .children("div.text-error"));
                }
            });
            $("#editForm").submit();//提交表格
        });


    })
</script>
<%--<script type="text/javascript">--%>
<%--$(function() {--%>
<%--$("#datepicker.input-daterange").datepicker({--%>
<%--autoclose : true,--%>
<%--language : "zh-CN",--%>
<%--todayHighlight : true,--%>
<%--todayBtn : "linked",--%>
<%--format:"yyyy-mm-dd",--%>
<%--endDate:new Date()--%>
<%--});--%>
<%--$('[data-toggle="tooltip"]').tooltip();--%>
<%--$(".addBtn").click(--%>
<%--function(e) {--%>
<%--e.preventDefault();--%>
<%--e.stopPropagation();--%>
<%--var url=$(this).attr("data-url");--%>
<%--$("#myModal").load(--%>
<%--url,--%>
<%--function(e) {--%>
<%--$('#myModal').modal({--%>
<%--backdrop : "static"--%>
<%--});--%>
<%--});--%>
<%--});--%>
<%--$(".editBtn,.detailBtn").click(function(e){--%>
<%--e.preventDefault();--%>
<%--e.stopPropagation();--%>
<%--$("#myModal").load($(this).attr("href"),function(e) {--%>
<%--$('#myModal').modal({--%>
<%--backdrop : "static"--%>
<%--});--%>
<%--});--%>
<%--});--%>
<%--$("#dataList a.ajaxLink").click(function(e){--%>
<%--e.preventDefault();--%>
<%--e.stopPropagation();--%>
<%--var method="POST";--%>
<%--if($(this).attr("data-role")){--%>
<%--method="DELETE";--%>
<%--if(!window.confirm("删除防伪码后无法恢复,是否继续?")){--%>
<%--return false;--%>
<%--}--%>
<%--}--%>
<%--$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){--%>
<%--if(xhr.status>=200 && xhr.status<300){--%>
<%--$("#messageBody").empty().html("操作成功");--%>
<%--$("#myModal").modal({backdrop:"static",show:true});--%>
<%--window.setTimeout(function() {--%>
<%--$("#search").click();--%>
<%--}, 1000);--%>
<%--}else{--%>
<%--$("#messageBody").empty().html("操作失败").addClass("text-danger");--%>
<%--$("#myModal").modal({backdrop:"static",show:true});--%>
<%--}--%>
<%--}});--%>
<%--});--%>
<%--$("tbody tr[data-row]").each(function(index,row){--%>
<%--$(row).click(function(event){--%>
<%--if(event.target.nodeName!="INPUT"){--%>
<%--rowSelect($(this));--%>
<%--}--%>
<%--switchCss($(this));--%>
<%--});--%>
<%--$(row).bind("contextmenu",function(){--%>
<%--return false;--%>
<%--});--%>
<%--});--%>
<%--});--%>
<%--</script>--%>
</body>
</html>
