<%@page import="Other.DayOfWeek"%>
<%@page import="Bean.MatterBean"%>
<%@page import="Bean.KinmuAllTimeBean"%>
<%@page import="Servlet.KinmuhyoServlet"%>
<%@page import="Bean.ManHourBean"%>
<%@page import="Other.HolidayName"%>
<%@page import="Sample.Sample"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Calendar"%>
<%@page import="Bean.LoginBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	@SuppressWarnings("unchecked")
	ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
	
	//ユーザリストのNULLチェック
	if(userlist == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String name = null;
	Integer authority = null, emp_num = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
		emp_num = lb.getNumber();
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>工数実績登録エラー｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/transportation_expenses_error.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
		<script type="text/javascript" src="common/js/FiveMinutesUpDownTime.js"></script>
		<script type="text/javascript" src="common/js/TimeRangeReflection.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
	</head>
	<body class="body">
		<div class="sub_header">
			<span class="name">ログイン中：<%= name %></span>
			<a href="./LogoutServlet" class="logout">ログアウト</a>
		</div>
		<div class="main_header">
			<h1 class="title">BlueDolphin</h1>
			<main class="p-3">
				<ul class="nav nav-tabs" style="margin-left: 130px; margin-top: -46px;">
					<li class="nav-item">
						<a href="./KintaiViewServlet" class="nav-link">勤怠打刻</a>
					</li>
					<li class="nav-item">
						<a href="./KinmuhyoServlet" class="nav-link">勤務表</a>
					</li>
					<li class="nav-item">
						<a href="./transportation_expenses_view.jsp" class="nav-link active">経費精算</a>
					</li>
					<li class="nav-item">
						<a href="./various_applications_view.jsp" class="nav-link">各種申請</a>
					</li>
					<li class="nav-item">
						<a href="./WorkRemotelyViewServlet" class="nav-link">在宅勤務申請</a>
					</li>
					<% if(authority == 1) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link">承認・却下</a>
						</li>
					<% } %>
					<% if(authority == 2) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link">承認・却下</a>
						</li>
						<li class="nav-item">
							<a href="./add_account_view.jsp" class="nav-link">アカウント追加</a>
						</li>
						<li class="nav-item">
							<a href="./delete_account_search.jsp" class="nav-link">アカウント削除</a>
						</li>
					<% } %>
				</ul>
			</main>
		</div>
		<div class="tab-content">
			<div id="./KintaiViewServlet" class="tab-pane"></div>
			<div id="./KinmuhyoServlet" class="tab-pane"></div>
			<div id="./transportation_expenses_view.jsp" class="tab-content active">
				<a href="./pass_price_view.jsp" class="link1">定期登録</a>
                <a href="./transportation_expenses_view.jsp" class="link2">交通費精算</a>
                <h2 align="center" class="head2">交通費精算内容入力</h2>
                <h5 align="center" class="head5">登録処理中にエラーが発生し、登録ができませんでした。</h5>
                <form action="TransportationExpensesServlet" method="post">
                    <div align="center">
                        <table class="table1">
                            <tr class="table1_tr1">
                                <th class="table1_tr1_th1" width="100" style="text-align: center;">日付</th>
                                <td class="table1_tr1_td1" colspan="6">
                                    <input type="text" name="date" class="text_class" size="10">
                                </td>
                            </tr>
                            <tr class="table1_tr2">
                                <th class="table1_tr2_th1" width="100" style="text-align: center;">出発</th>
                                <td class="table1_tr2_td1">
                                    <input type="text" name="departure" class="departure" maxlength="20">
                                </td>
                                <th class="table1_tr2_th2" width="100" style="text-align: center;">到着</th>
                                <td class="table1_tr2_td2">
                                    <input type="text" name="arrival" class="arrival" maxlength="20">
                                </td>
                                <th class="table1_tr2_th3" width="100" style="text-align: center;">経由</th>
                                <td class="table1_tr2_td3">
                                    <input type="text" name="via" class="via" maxlength="20">
                                </td>
                                <td class="table1_tr2_td4">
                                    <input type="submit" value="経路検索" class="table1_tr2_td4_submit">
                                </td>
                            </tr>
                            <tr class="table1_tr3">
                                <th class="table1_tr3_th1" width="100" style="text-align: center;">請求範囲</th>
                                <td class="table1_tr3_td1">
                                    <input type="radio" name="claims" value="one_way">片道　
                                    <input type="radio" name="claims" value="round_trip" checked>往復
                                </td>
                                <th class="table1_tr3_th2" width="100" style="text-align: center;">請求先</th>
                                <td class="table1_tr3_td2" colspan="5">
                                    <input type="radio" name="billing_address" value="private_company" checked>自社　
                                    <input type="radio" name="billing_address" value="customer_company">客先
                                </td>
                            </tr>
                            <tr class="table1_tr4">
                                <th class="table1_tr4_th1" width="120" style="text-align: center;">目的・行き先</th>
                                <td class="table1_tr4_td1" colspan="6">
                                    <input type="text" name="purpose" class="table1_tr4_text" size="50">
                                </td>
                            </tr>
                            <tr class="table1_tr5">
                                <th class="table1_tr5_th1" width="100" style="text-align: center;">金額</th>
                                <td class="table1_tr5_td1" colspan="6">
                                    <input type="text" name="amounts" class="table1_tr5_text" maxlength="10">
                                </td>
                            </tr>
                        </table>
                        <input type="button" class="button_cancel" value="キャンセル" onclick="location.href='#'">
                        <input type="submit" class="submit_registration" value="登録">
                    </div>
                </form>
                <script src="common/js/datepicker.js"></script>
			</div>
			<div id="./various_applications_view.jsp" class="tab-content"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-content"></div>
			<% if(authority == 1) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content"></div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content"></div>
				<div id="./add_account_view.jsp" class="tab-pane"></div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>