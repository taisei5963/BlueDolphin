<%@page import="Bean.WorkRemotelyBean"%>
<%@page import="Other.DayOfWeek"%>
<%@page import="Bean.MatterBean"%>
<%@page import="Bean.KinmuAllTimeBean"%>
<%@page import="Servlet.KinmuhyoServlet"%>
<%@page import="Bean.ManHourBean"%>
<%@page import="Other.HolidayName"%>
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
	Integer authority = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>各種申請｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/various_applications_view.css">
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
						<a href="./transportation_expenses_view.jsp" class="nav-link">経費精算</a>
					</li>
					<li class="nav-item">
						<a href="./various_applications_view.jsp" class="nav-link  active">各種申請</a>
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
			<div id="./transportation_expenses_view.jsp" class="tab-content"></div>
			<div id="./various_applications_view.jsp" class="tab-content active">
				 <div class="div" align="center">
                	<h2 class="context">各種申請</h2>
                	<p class="content1">以下から申請を行う項目のリンクを押下してください。</p>
                    <table class="table1">
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" width="50%" align="right">
                                <a href="./VacationRequestViewServlet" class="table1_tr2_td1_link1">休暇申請</a>
                            </td>
                            <td class="table1_tr2_td2">私用や現場指示などで休暇・欠勤が発生する場合に使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./holiday_work_request_view.jsp" class="table1_tr2_td1_link1">休日出勤申請</a>
                            </td>
                            <td class="table1_tr2_td2">自社内、現場先で休日出勤が発生した場合に使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./overtime_request_view.jsp" class="table1_tr2_td1_link1">早出・残業申請</a>
                            </td>
                            <td class="table1_tr2_td2">
                            ノー残業デーのときや19時以降まで作業を行う場合、または定刻前に作業を行う場合に使用する。
                            <br>※定時退社を基本とし、リリース月や年度末等でいたしかたなく残業する場合のみ申請する。
                            <br>※早出残業申請に関しては、基本的には使用禁止とする。
                            </td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./WorkPatternRequestViewServlet" class="table1_tr2_td1_link1">勤務時間変更申請</a>
                            </td>
                            <td class="table1_tr2_td2">現場先が変更になり、新たな勤務時間となったときに使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./behind_time_request_view.jsp" class="table1_tr2_td1_link1">遅刻申請</a>
                            </td>
                            <td class="table1_tr2_td2">体調不良などで出勤時間に遅れが発生する場合に使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./leave_early_request_view.jsp" class="table1_tr2_td1_link1">早退申請</a>
                            </td>
                            <td class="table1_tr2_td2">体調不良などで退勤時間より早く帰宅する場合に使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./direct_bounce_request_view.jsp" class="table1_tr2_td1_link1">直行直帰申請</a>
                            </td>
                            <td class="table1_tr2_td2">自宅から直接営業先に向かう場合や、営業先から直接帰路に着く場合に使用する。<br>※上長に直接で電話で連絡を入れた場合は、本申請は不要とする。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="./ManHourRequestViewServlet" class="table1_tr2_td1_link1">工数申請</a>
                            </td>
                            <td class="table1_tr2_td2">従業員の１ヶ月分の作業工数を申請する場合に使用する。</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1" align="right">
                                <a href="CheckApplicationStatusViewServlet" class="table1_tr2_td1_link1">申請状況確認</a>
                            </td>
                            <td class="table1_tr2_td2">ユーザ従業員が申請した申請物の申請状況を確認する場合に使用する。
                            <br>申請取消の処理を行うことも可能。
                            </td>
                        </tr>
                    </table>
                </div>
			</div>
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