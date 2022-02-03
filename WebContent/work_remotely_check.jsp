<%@page import="Bean.WorkRemotelyBean"%>
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
	
	@SuppressWarnings("unchecked")
	ArrayList<WorkRemotelyBean> reasonList = (ArrayList<WorkRemotelyBean>)session.getAttribute("reasonList");
	
	String applicant = (String)session.getAttribute("applicant");
	String start_application_date = (String)session.getAttribute("start_application_date");
	String end_application_date = (String)session.getAttribute("end_application_date");
	String start_application_time = (String)session.getAttribute("start_application_time");
	String end_application_time = (String)session.getAttribute("end_application_time");
	String reason_for_application = (String)session.getAttribute("reason_for_application");
	String work_content = (String)session.getAttribute("work_content");
	String contact_address = (String)session.getAttribute("contact_address");
	
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
		<title>在宅勤務申請｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/work_remotely_check.css">
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
						<a href="./various_applications_view.jsp" class="nav-link">各種申請</a>
					</li>
					<li class="nav-item">
						<a href="./WorkRemotelyViewServlet" class="nav-link active">在宅勤務申請</a>
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
			<div id="./transportation_expenses_view.jsp" class="tab-content">
				<script src="common/js/datepicker.js"></script>
			</div>
			<div id="./various_applications_view.jsp" class="tab-content"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-content active">
				<h4 class="head4">在宅勤務申請【内容確認】</h4>
                <p class="content1">以下の内容で申請を行います。<br>
               	内容に問題がなければ、「申請」ボタンを押下してください。</p>
                <form action="WorkRemotelyResultServlet" method="post">
                    <table class="table1">
                        <tr class="table1_tr1">
                            <td class="table1_tr1_td1" colspan="2">在宅勤務申請</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1">申請者</td>
                            <td class="table1_tr2_td2"><span class="table1_tr2_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr3">
                            <td class="table1_tr3_td1" colspan="2"><%= applicant %></td>
                        </tr>
                        <tr class="table1_tr4">
                            <td class="table1_tr4_td1">開始日</td>
                            <td class="table1_tr4_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr5">
                            <td class="table1_tr5_td1" colspan="2"><%= start_application_date %></td>
                        </tr>
                        <tr class="table1_tr4">
                            <td class="table1_tr4_td1">終了日</td>
                            <td class="table1_tr4_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr5">
                            <td class="table1_tr5_td1" colspan="2"><%= end_application_date %></td>
                        </tr>
                        <tr class="table1_tr6">
                            <td class="table1_tr6_td1">開始時間（予定）</td>
                            <td class="table1_tr6_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr7">
                            <td class="table1_tr7_td1" colspan="2"><%= start_application_time %></td>
                        </tr>
                        <tr class="table1_tr6">
                            <td class="table1_tr6_td1">終了時間（予定）</td>
                            <td class="table1_tr6_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr7">
                            <td class="table1_tr7_td1" colspan="2"><%= end_application_time %></td>
                        </tr>
                        <tr class="table1_tr8">
                            <td class="table1_tr8_td1">申請理由</td>
                            <td class="table1_tr8_td2"><span class="table1_tr8_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr9">
                            <td class="table1_tr9_td1" colspan="2"><%= reason_for_application %></td>
                        </tr>
                        <tr class="table1_tr10">
                            <td class="table1_tr10_td1">作業内容（予定）</td>
                            <td class="table1_tr10_td2"><span class="table1_tr10_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr11">
                            <td class="table1_tr11_td1" colspan="2"><%= work_content %></td>
                        </tr>
                        <tr class="table1_tr12">
                            <td class="table1_tr12_td1">連絡先電話番号（携帯 or 固定）<br><span style="font-size: 15px;">※BP社社員の方は、営業担当者様の携帯電話番号を記載してください。</span></td>
                            <td class="table1_tr12_td2"><span class="table1_tr12_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr13">
                            <td class="table1_tr13_td1" colspan="2"><%= contact_address %></td>
                        </tr>
                    </table>
                    <button type="submit" name="process" value="内容修正処理" class="button_submit_reset">内容修正</button>　
                    <button type="submit" name="process" value="申請処理" class="button_submit">申請</button>
                </form>
			</div>
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