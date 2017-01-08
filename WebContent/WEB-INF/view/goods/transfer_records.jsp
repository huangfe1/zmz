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
    <%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp" %>
    <%@include file="/WEB-INF/view/common/datepicker_css.jsp" %>
    <%@include file="/WEB-INF/view/common/head_css_fav.jsp" %>
    <%@include file="/WEB-INF/view/common/head_css_datatables.jsp" %>
    <style>
        .input-daterange {
            width: inherit !important;
        }

        td {
            word-break: keep-all;
        }
    </style>
    <title>我的转货记录</title>
</head>
<body>
<div id="wrapper">
    <jsp:include page="/menu.html"></jsp:include>
    <div id="page-wrapper">
        <nav class="navbar navbar-default" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle animated flip pull-left"
                            data-toggle="collapse" data-target="#transfer-navbar-collapse">
                        <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
                        <span class="icon-bar"></span> <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse" id="transfer-navbar-collapse">
                    <form class="navbar-form navbar-left" id="searchForm"
                          role="search" action="<c:url value='/transfer/records.html'/>"
                          method="GET">
                        <input type="hidden" name="from" value="${user.id}">
                        <div class="form-group">
                            <label>转入方</label>
                            <input type="text" name="entity.userByToAgent.realName" value="${parameter.entity.userByToAgent.realName}" class="form-control"
                                   id="applyAgent">
                            <label>转出方</label>
                            <input type="text" name="entity.userByFromAgent.realName" value="${parameter.entity.userByFromAgent.realName}" class="form-control"
                                   id="applyAgent">
                            <label>申请时间</label>
                            <div class="input-daterange input-group" id="datepicker">
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-calendar"></span></span>
                                <input type="text" class="form-control pointer"
                                       value="${transfer.startTime }" id="startDate" data-date-format="yyyy-mm-dd"
                                       name="startTime" placeholder="开始日期"/>
                                <span class="input-group-addon">到</span> <input type="text"
                                                                                class="form-control pointer"
                                                                                value="${transfer.endTime }"
                                                                                name="endTime" placeholder="截止日期"/>
                                <span class="input-group-addon"><span
                                        class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                            <button type="submit" class="btn btn-primary" id="search"
                                    name="search">
                                <span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
                            </button>
                            <button type="button" class="btn btn-danger  btn-re" id="download"
                                    name="search">
                                <span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;下载
                            </button>
                        </div>
                    </form>
                    <%--<ul class="nav navbar-nav navbar-right">--%>
                    <%--<li><button type="button" data-url="<c:url value='/transfer/to.html?from=${user.id}' />"--%>
                    <%--class="btn btn-primary navbar-btn toBtn">--%>
                    <%--<li class="fa fa-mail-forward fa-fw"></li>转出货物--%>
                    <%--</button></li>--%>
                    <%--</ul>--%>
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
                            <th>选择</th>
                            <th>转入方</th>
                            <th>转出方</th>
                            <th>转货数量</th>
                            <th>总金额</th>
                            <%--<th>产生积分</th>--%>
                            <th>申请时间</th>
                            <th>转货时间</th>
                            <th>使用代金券+预存款</th>
                            <th>状态</th>
                            <th>备注</th>
                            <th>类型</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="dataList">
                        <c:forEach items="${ts}" var="g">
                            <tr data-row="${g.id}">
                                <td><input type="checkbox" value="${g.id}"></td>
                                <td>${g.userByToAgent.realName}(${g.userByToAgent.agentCode})</td>
                                <td>${g.userByFromAgent.realName}(${g.userByFromAgent.agentCode})</td>
                                <td>${g.quantity}</td>
                                <td>${g.amount}</td>
                                    <%--<td>${g.points}</td>--%>
                                <td>${g.applyTime}</td>
                                <td>${g.transferTime}</td>
                                <td>${g.voucher}+${g.advance}</td>
                                <td>${g.status.desc}</td>
                                <td>${g.remittance}</td>
                                <td>${g.applyOrigin.desc}</td>

                                <td>
                                    <a class="btn btn-info default detailBtn"
                                       href="<c:url value='/transfer/detail.html?id=${g.id}'/>">
                                        <span class="glyphicon glyphicon-list"></span>转货明细</a>
                                    <c:if test="${!g.backedTransfer}">
                                        <c:if test="${g.userByFromAgent.id eq user.id  }">
                                            <a class="btn btn-success default ajaxLink"
                                               <c:if test="${g.status!='NEW'}">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/confirm.json?id=${g.id}' /> "><span
                                                    class="fa fa-share-square-o" aria-hidden="true"></span>同意转货</a>
                                            <a class="btn btn-warning default ajaxLink"
                                               <c:if test="${g.status!='NEW' }">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/refuse.json?id=${g.id}' /> "><span
                                                    class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span>拒绝转货</a>
                                        </c:if>
                                        <c:if test="${g.userByToAgent.id eq user.id }">
                                            <a class="btn btn-success default ajaxLink" data-role="DELETE"
                                               <c:if test="${g.status!='NEW'}">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/remove.json?id=${g.id}' /> "><span
                                                    class="fa fa-share-square-o" aria-hidden="true"></span>撤销申请</a>
                                        </c:if>
                                    </c:if>
                                    <!--如果是退货-->
                                    <c:if test="${g.backedTransfer}">
                                        <!--不是管理员-->
                                        <c:if test="${g.userByFromAgent.id eq user.id  }">
                                            <a class="btn btn-success default ajaxLink" data-role="DELETE"
                                               <c:if test="${g.status!='NEW'}">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/remove.json?id=${g.id}' /> "><span
                                                    class="fa fa-share-square-o" aria-hidden="true"></span>撤销申请</a>
                                        </c:if>
                                        <!--是管理员-->
                                        <c:if test="${user.id eq 1 }">
                                            <a class="btn btn-success default ajaxLink"
                                               <c:if test="${g.status!='NEW'}">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/confirm.json?id=${g.id}' /> "><span
                                                    class="fa fa-share-square-o" aria-hidden="true"></span>同意退货</a>
                                            <a class="btn btn-warning default ajaxLink"
                                               <c:if test="${g.status!='NEW' }">disabled="disabled"</c:if>
                                               href="<c:url value='/transfer/refuse.json?id=${g.id}' /> "><span
                                                    class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span>拒绝退货</a>
                                        </c:if>
                                    </c:if>
                                </td>


                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <jsp:include page="/WEB-INF/view/common/pager.jsp"></jsp:include>
            </div>
        </div>
    </div>
</div>
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
            <div class="modal-footer"></div>
        </div>
    </div>
</div>
<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
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
            <div class="modal-body" id='alertMessageBody'></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-info btn-block quitBtn" tabIndex="26"
                        id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
                        tabindex="4" data-loading-text="正在关闭......">
                    <span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="selectModal" tabindex="-2" role="dialog" style="z-index:999999;"
     aria-labelledby="selectModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="selectModalLabel"></h4>
            </div>
            <div class="modal-body" id="selectBody"></div>
            <div class="modal-footer"></div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>

<script type="text/javascript">
    $(function () {

        $("#download").click(function(){
            $("#searchForm").attr("action", "<c:url value='/transfer/recordsDownload.html'/>")
            $("#searchForm").submit();
        });

        $("#datepicker.input-daterange").datepicker({
            autoclose: true,
            language: "zh-CN",
            todayHighlight: true,
            todayBtn: "linked",
            format: "yyyy-mm-dd",
            endDate: new Date()
        });

        $(".toBtn").click(
            function (e) {
                e.preventDefault();
                e.stopPropagation();
                var url = $(this).attr("data-url");
                $("#winModal").load(
                    url,
                    function (e) {
                        $("#winModal").modal({
                            backdrop: "static"
                        });
                    });
            });

        $(".addBtn").click(
            function (e) {
                e.preventDefault();
                e.stopPropagation();
                $("#myModal").load(
                    "<c:url value='/transfer/from.html'/>",
                    function (e) {
                        $('#myModal').modal({
                            backdrop: "static"
                        });
                    });
            });
        $(".editBtn,.detailBtn").click(function (e) {
            e.preventDefault();
            e.stopPropagation();
            $("#myModal").load($(this).attr("href"), function (e) {
                $('#myModal').modal({
                    backdrop: "static"
                });
            });
        });
        $("#dataList a.ajaxLink").click(function (e) {
            e.preventDefault();
            var method = "POST";
            if ($(this).attr("data-role")) {
                method = "DELETE";
            }
            $.ajax({
                url: $(e.target).attr("href"), method: method, complete: function (xhr, ts) {
                    var m = $.parseJSON(xhr.responseText);
                    if (xhr.status >= 200 && xhr.status < 300 && m.flag == "0") {//黄小飞修改
                        $("#alertMessageBody").empty().html("操作成功");
                        $("#winModal").modal({backdrop: "static", show: true});
                        window.setTimeout(function () {
                            $("#search").click();
                        }, 1500);
                    } else {
                        $("#alertMessageBody").empty().html("操作失败").addClass("text-danger");
                        $("#winModal").modal({backdrop: "static", show: true});
                    }
                }
            });
        });
        $("tbody tr[data-row]").each(function (index, row) {
            $(row).click(function (event) {
                if (event.target.nodeName != "INPUT") {
                    rowSelect($(this));
                }
                switchCss($(this));
            });
            $(row).bind("contextmenu", function () {
                return false;
            });
        });
    });
</script>
</body>
</html>
