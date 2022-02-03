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
	String process = (String)session.getAttribute("process");
	
	//ユーザリストのNULLチェック
	if(userlist == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String bumon = null, name = null, kinmu = null, yobi = null;
	Integer authority = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>工数実績登録｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/man_hour_result.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
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
						<a href="./KinmuhyoServlet" class="nav-link active">勤務表</a>
					</li>
					<li class="nav-item">
						<a href="./transportation_expenses_view.jsp" class="nav-link">経費精算</a>
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
			<div id="./KinmuhyoServlet" class="tab-pane active">
				<h2 align="center" class="head2">工数実績登録</h2>
				<% if("削除成功".equals(process)) { %>
					<h5 class="head5" align="center">指定の工数項目の削除が成功しました。</h5>
					<input type="button" value="戻る" onclick="location.href='./ManHourViewServlet'" class="button_back">
				<% } else if("登録成功".equals(process)) { %>
					<h5 class="head5" align="center">指定の工数情報の登録が成功しました。</h5>
					<input type="button" value="戻る" onclick="location.href='./ManHourViewServlet'" class="button_back">
				<% } %>
			</div>
			<div id="./transportation_expenses_view.jsp" class="tab-content"></div>
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