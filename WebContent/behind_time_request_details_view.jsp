<%@page import="Bean.ApprovalApplicationDetailsBean"%>
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
	
	//セッションリスト情報のNULLチェック
	if(userlist == null || appList == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String name = null;
	Integer authority = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
	}
	
	String status = null;
	for(ApplicationContentsBean acb : appList){
		status = acb.getApplicationStatus();
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>承認・却下｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/behind_time_request_details_view.css">
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
						<a href="./WorkRemotelyViewServlet" class="nav-link">在宅勤務申請</a>
					</li>
					<% if(authority == 1) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link active">承認・却下</a>
						</li>
					<% } %>
					<% if(authority == 2) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link active">承認・却下</a>
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
			<div id="./various_applications_view.jsp" class="tab-content"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-content"></div>
			<% if(authority == 1) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content active">
					<a href="./ApprovalRejectedViewServlet" class="link1">戻る</a>
					<div class="div" align="center">
						<form action="ApprovalRejectedDetailsServlet" method="post">
		                    <h2 class="context">申請詳細</h2>
		                    <p class="content1">以下の申請に対し、承認または却下対応をお願いいたします。</p>
	                    	<table class="table1" border="1">
	                            <tr class="table1_tr1" align="center">
	                                <th class="table1_tr1_th1" align="center" width="120">申請日</th>
	                                <th class="table1_tr1_th2" align="center" width="100">申請者</th>
	                                <th class="table1_tr1_th3" align="center" width="200">対象日</th>
	                                <th class="table1_tr1_th4" align="center" width="150">申請内容</th>
	                                <th class="table1_tr1_th5" align="center" width="150">申請理由</th>
	                                <th class="table1_tr1_th6" align="center" width="100">出勤時間(予定)</th>
	                                <th class="table1_tr1_th7" align="center" width="100">申請状況</th>
	                            </tr>
	                            <% for(ApplicationContentsBean acb : appList) { %>
	                            	<tr class="table1_tr2" align="center">
	                            		<!-- 申請日 -->
		                                <td class="table1_tr2_td1"><%= acb.getApplicationDate() %></td>
		                                <!-- 申請者 -->
		                                <td class="table1_tr2_td2"><%= acb.getApplicant() %></td>
		                                <!-- 対象日 -->
		                                <td class="table1_tr2_td3"><%= acb.getTargetDate() %></td>
		                                <!-- 申請内容 -->
			                            <td class="table1_tr2_td4"><%= acb.getApplicationName() %></td>
			                            <!-- 申請理由 -->
			                            <td class="table1_tr2_td5"><%= acb.getApplicationReason() %></td>
		                                <!-- 出勤時間(予定) -->
		                                <td class="table1_tr2_td6"><%= acb.getWorkingHours() %></td>
		                                <!-- 申請状況 -->
		                                <% if("申請中".equals(acb.getApplicationStatus())) { %>
		                                	<td class="table1_tr2_td7">
		                                		<span style="color: red; font-weight: 600"><%= acb.getApplicationStatus() %></span>
		                                	</td>
		                                <% } else { %>
		                                	<td class="table1_tr2_td7"><%= acb.getApplicationStatus() %></td>
		                                <% } %>
		                            </tr>
	                            <% } %>
	                            <tr class="table1_tr3">
	                                <td colspan="9" class="table1_tr3_td1"></td>
	                            </tr>
	                            <% if("申請中".equals(status)) { %>
	                            	<tr class="table1_tr4">
		                                <td colspan="9" class="table1_tr4_td1">
		                                    <textarea name="comment" class="table1_tr4_td1_textarea" cols="50" rows="2" placeholder="却下理由およびコメントは、こちらに記載ください。"></textarea>
		                                </td>
		                            </tr>
	                            <% } else {} %>
	                            <tr class="table1_tr3">
	                                <td colspan="9" class="table1_tr3_td1"></td>
	                            </tr>
	                        </table>
	                        <% if("申請中".equals(status)) { %>
	                        	<button class="submit_rejected" name="process" value="却下処理">却下</button>
                        		<button class="submit_appoval" name="process" value="承認処理">承認</button>
	                        <% } else {} %>
						</form>
	                </div>
				</div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content active">
					<a href="./ApprovalRejectedViewServlet" class="link1">戻る</a>
					<div class="div" align="center">
						<form action="ApprovalRejectedDetailsServlet" method="post">
		                    <h2 class="context">申請詳細</h2>
		                    <p class="content1">以下の申請に対し、承認または却下対応をお願いいたします。</p>
	                    	<table class="table1" border="1">
	                            <tr class="table1_tr1" align="center">
	                                <th class="table1_tr1_th1" align="center" width="120">申請日</th>
	                                <th class="table1_tr1_th2" align="center" width="100">申請者</th>
	                                <th class="table1_tr1_th3" align="center" width="200">対象日</th>
	                                <th class="table1_tr1_th4" align="center" width="150">申請内容</th>
	                                <th class="table1_tr1_th5" align="center" width="150">申請理由</th>
	                                <th class="table1_tr1_th6" align="center" width="100">出勤時間(予定)</th>
	                                <th class="table1_tr1_th7" align="center" width="100">申請状況</th>
	                            </tr>
	                            <% for(ApplicationContentsBean acb : appList) { %>
	                            	<tr class="table1_tr2" align="center">
	                            		<!-- 申請日 -->
		                                <td class="table1_tr2_td1"><%= acb.getApplicationDate() %></td>
		                                <!-- 申請者 -->
		                                <td class="table1_tr2_td2"><%= acb.getApplicant() %></td>
		                                <!-- 対象日 -->
		                                <td class="table1_tr2_td3"><%= acb.getTargetDate() %></td>
		                                <!-- 申請内容 -->
			                            <td class="table1_tr2_td4"><%= acb.getApplicationName() %></td>
			                            <!-- 申請理由 -->
			                            <td class="table1_tr2_td5"><%= acb.getApplicationReason() %></td>
		                                <!-- 出勤時間(予定) -->
		                                <td class="table1_tr2_td6"><%= acb.getWorkingHours() %></td>
		                                <!-- 申請状況 -->
		                                <% if("申請中".equals(acb.getApplicationStatus())) { %>
		                                	<td class="table1_tr2_td7">
		                                		<span style="color: red; font-weight: 600"><%= acb.getApplicationStatus() %></span>
		                                	</td>
		                                <% } else { %>
		                                	<td class="table1_tr2_td7"><%= acb.getApplicationStatus() %></td>
		                                <% } %>
		                            </tr>
	                            <% } %>
	                            <tr class="table1_tr3">
	                                <td colspan="9" class="table1_tr3_td1"></td>
	                            </tr>
	                            <% if("申請中".equals(status)) { %>
	                            	<tr class="table1_tr4">
		                                <td colspan="9" class="table1_tr4_td1">
		                                    <textarea name="comment" class="table1_tr4_td1_textarea" cols="50" rows="2" placeholder="却下理由およびコメントは、こちらに記載ください。"></textarea>
		                                </td>
		                            </tr>
	                            <% } else {} %>
	                            <tr class="table1_tr3">
	                                <td colspan="9" class="table1_tr3_td1"></td>
	                            </tr>
	                        </table>
	                        <% if("申請中".equals(status)) { %>
	                        	<button class="submit_rejected" name="process" value="却下処理">却下</button>
                        		<button class="submit_appoval" name="process" value="承認処理">承認</button>
	                        <% } else {} %>
						</form>
	                </div>
				</div>
				<div id="./add_account_view.jsp" class="tab-pane"></div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>