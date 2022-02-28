<%@page import="Bean.ApplicationContentsBean"%>
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
	@SuppressWarnings("unchecked")
	ArrayList<ApplicationContentsBean> appList = (ArrayList<ApplicationContentsBean>)session.getAttribute("appList");
	
	String status = (String)session.getAttribute("status");
	
	//ユーザリストのNULLチェック
	if(userlist == null || appList == null){
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
		<link rel="stylesheet" type="text/css" href="common/css/check_application_status_view.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="common/js/displayFilter.js"></script>
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
				 	<form action="CheckApplicationStatusServlet" method="post" name="processForm">
				 		<h2 class="context">申請状況一覧</h2>
	                	<p class="content1">申請中の申請を取消を行う場合は、「申請取消」ボタンを押下してください。</p>
	                	申請状況：<select name="status" class="status_select" id="filter">
	                        	<% if("申請中".equals(status)) { %>
	                        		<option value="すべて">すべて</option>
		                            <option value="申請中" selected>申請中</option>
		                            <option value="承認">承認</option>
		                            <option value="却下">却下</option>
	                        	<% } else if("承認".equals(status)) { %>
	                        		<option value="すべて">すべて</option>
		                            <option value="申請中">申請中</option>
		                            <option value="承認" selected>承認</option>
		                            <option value="却下">却下</option>
	                        	<% } else if("却下".equals(status)) { %>
	                        		<option value="すべて">すべて</option>
		                            <option value="申請中">申請中</option>
		                            <option value="承認">承認</option>
		                            <option value="却下" selected>却下</option>
	                        	<% } else { %>
	                        		<option value="すべて" selected>すべて</option>
		                            <option value="申請中">申請中</option>
		                            <option value="承認">承認</option>
		                            <option value="却下">却下</option>
	                        	<% } %>
	                        </select>
	                    <table class="table1" border="1">
	                        <tr class="table1_tr1">
	                            <th class="table1_tr1_th1">申請番号</th>
	                            <th class="table1_tr1_th1">申請区分</th>
	                            <th class="table1_tr1_th1">申請日</th>
	                            <th class="table1_tr1_th1">承認者</th>
	                            <th class="table1_tr1_th1">承認日</th>
	                            <th class="table1_tr1_th1">承認者コメント</th>
	                            <th class="table1_tr1_th1">申請状況</th>
	                            <th class="table1_tr1_th1">処理</th>
	                        </tr>
	                        <% for(ApplicationContentsBean acb : appList) { %>
	                        	<tr class="table1_tr2">
	                        		<td class="table1_tr2_td1"><%= acb.getApplicationNumber() %></td>
	                        		<td class="table1_tr2_td2"><%= acb.getApplicationCategory() %></td>
	                        		<td class="table1_tr2_td3"><%= acb.getApplicationDate() %></td>
	                        		<td class="table1_tr2_td4"><%= acb.getCorrespondingPerson() %></td>
	                        		<td class="table1_tr2_td5"><%= acb.getCorrespondenceDate() %></td>
	                        		<td class="table1_tr2_td6"><%= acb.getApproverComment() %></td>
	                        		<td class="table1_tr2_td7"><%= acb.getApplicationStatus() %></td>
	                        		<td class="table1_tr2_td8">
	                        			<% if("申請中".equals(acb.getApplicationStatus())) { %>
	                        				<button class="table1_tr2_td8_button" name="process" value="申請取消処理">申請取消</button>
	                        			<% } else {} %>
	                        		</td>
	                        	</tr>
	                        <% } %>
	                    </table>
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