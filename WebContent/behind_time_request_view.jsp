<%@page import="Bean.PaidHolidayBean"%>
<%@page import="Bean.VacationRequestBean"%>
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
	
	//代休有無選択ラジオボタン
	String holiday = (String)session.getAttribute("holiday");
	
	//セッションにて取得したリストのNULLチェック
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
		<link rel="stylesheet" type="text/css" href="common/css/behind_time_request_view.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"/>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.css"/>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" defer></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" defer></script>
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script type="text/javascript" src="common/js/datepicker.js"></script>
        
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
				<a href="./various_applications_view.jsp" class="link1">戻る</a>
				<div class="div" align="center">
					<form action="BehindTimeRequestProcessServlet" method="post">
	                	<h2 class="context">遅刻申請</h2>
	                	<p class="content1">以下の内容を入力のうえ、「承認申請」ボタンを押下してください。</p>
	                    <table class="table1" border="1">
	                        <tr class="table1_tr1">
                                <td class="table1_tr1_td1" colspan="3">申請内容</td>
                            </tr>
                            <tr class="table1_tr2">
                                <td class="table1_tr2_td1">申請日</td>
                                <td class="table1_tr2_td2">
                                    <span class="table1_tr2_td2_span">必須</span>
                                </td>
                                <td class="table1_tr2_td3">
                                    <input type="text" name="startDate" class="text_class" value="" required>
                                    <div class="appendDatepicker1"></div>
                                </td>
                            </tr>
                            <tr class="table1_tr3">
                                <td class="table1_tr3_td1">出社時間(予定)</td>
                                <td class="table1_tr3_td2">
                                    <span class="table1_tr2_td2_span">必須</span>
                                </td>
                                <td class="table1_tr3_td3">
                                    <input type="time" name="attendance_time" id="start" value="00:00" class="table1_tr3_td3_time1">
                                </td>
                            </tr>
                            <tr class="table1_tr6">
                                <td class="table1_tr6_td1">申請理由</td>
                                <td class="table1_tr6_td2"></td>
                                <td class="table1_tr6_td3">
                                    <textarea name="application_reason" class="table1_tr6_td3_textarea" cols="50" rows="1" placeholder="例：リリース対応及びエラー発生対応のため"></textarea>
                                </td>
                            </tr>
                            <tr class="table1_tr7">
                                <td class="table1_tr7_td1">コメント</td>
                                <td class="table1_tr7_td2"></td>
                                <td class="table1_tr7_td3">
                                    <textarea name="comment" class="table1_tr7_td3_textarea" cols="50" rows="2"></textarea>
                                </td>
                            </tr>
	                    </table>
	                    <button type="reset" class="reset_rejected">リセット</button>
                        <button type="submit" class="submit_appoval" name="process" value="申請処理">承認申請</button>
                    </form>
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