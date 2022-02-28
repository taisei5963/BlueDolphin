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
	
	String process_name = (String)session.getAttribute("process_name");
	String status = (String)session.getAttribute("status");
	
	//リストのNULLチェック
	if(userlist == null || appList == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String category = null;
	for(ApplicationContentsBean acb: appList) {
		category = acb.getApplicationCategory();
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
		<title>承認・却下｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/approval_rejected_view.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
		<script type="text/javascript" src="common/js/displayFilter.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
	</head>
	<body class="body">
		<div class="sub_header">
			<span class="name">ログイン中：<%=name%></span>
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
					<div class="div" align="center">
						<form action="ApprovalRejectedServlet" method="post" name="processForm">
							<h2 class="context">申請内容一覧</h2>
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
							<% if(appList.size() == 0) { %>
		                    	<p class="content1">現在届いてる申請は、ございません。</p>
		                    <% } else { %>
		                    	<p class="content1">以下の申請が届いております。<br>申請番号のリンクを押下し、詳細内容をご確認ください。</p>
		                    	<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <th class="table1_tr1_th1" align="center" width="150">申請番号</th>
		                                <th class="table1_tr1_th2" align="center" width="150">申請日</th>
		                                <th class="table1_tr1_th3" align="center" width="150">申請者</th>
		                                <th class="table1_tr1_th4" align="center" width="150">申請区分</th>
		                                <th class="table1_tr1_th5" align="center" width="150">申請状況</th>
		                                <th class="table1_tr1_th6" align="center" width="250">申請者コメント</th>
		                            </tr>
		                            <% for(ApplicationContentsBean acb : appList) { %>
		                            	<tr class="table1_tr2" align="center">
			                                <!-- 申請番号 -->
			                                <td class="table1_tr2_td1">
			                                	<a href="./ApprovalRejectedServlet?AppNum=<%= acb.getApplicationNumber() %>&AppCategory=<%= acb.getApplicationCategory() %>">
			                                		<%= acb.getApplicationNumber() %>
			                                	</a>
			                                </td>
			                                <!-- 申請日 -->
			                                <td class="table1_tr2_td2"><%= acb.getApplicationDate() %></td>
			                                <!-- 申請者 -->
			                                <td class="table1_tr2_td3"><%= acb.getApplicant() %></td>
			                                <!-- 申請区分 -->
			                                <td class="table1_tr2_td4"><%= acb.getApplicationCategory() %></td>
			                                <!-- 申請状況 -->
			                                <% if("申請中".equals(acb.getApplicationStatus())) { %>
			                                	<td class="table1_tr2_td5">
			                                		<span style="color: red; font-weight: 600;"><%= acb.getApplicationStatus() %></span>
			                                	</td>
			                                 <% } else if("承認".equals(acb.getApplicationStatus()) || "却下".equals(acb.getApplicationStatus())) { %>
			                                	<td class="table1_tr2_td5">
			                                		<span style="color: black;"><%= acb.getApplicationStatus() %></span>
			                                	</td>
			                                <% } %>
			                                <!-- コメント -->
			                                <% if(acb.getApplicantComment() == null || acb.getApplicantComment().equals("")) { %>
		                                		<td class="table1_tr2_td6"></td>
		                                	<% } else { %>
		                                		<td class="table1_tr2_td6"><%= acb.getApplicantComment() %></td>
		                                	<% } %>
			                            </tr>
		                            <% } %>
		                        </table>
	                    	<% } %>
						</form>
	                </div>
				</div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content active">
					<div class="div" align="center">
						<form action="ApprovalRejectedServlet" method="post" name="processForm">
							<h2 class="context">申請一覧</h2>
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
							<% if(appList.size() == 0) { %>
		                    	<p class="content1">現在届いてる申請は、ございません。</p>
		                    <% } else { %>
		                    	<p class="content1">以下の申請が届いております。<br>申請番号のリンクを押下し、詳細内容をご確認ください。</p>
		                    	<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <th class="table1_tr1_th1" align="center" width="150">申請番号</th>
		                                <th class="table1_tr1_th2" align="center" width="150">申請日</th>
		                                <th class="table1_tr1_th3" align="center" width="150">申請者</th>
		                                <th class="table1_tr1_th4" align="center" width="150">申請区分</th>
		                                <th class="table1_tr1_th5" align="center" width="150">申請状況</th>
		                                <th class="table1_tr1_th6" align="center" width="250">申請者コメント</th>
		                            </tr>
		                            <% for(ApplicationContentsBean acb : appList) { %>
		                            	<tr class="table1_tr2" align="center">
			                                <!-- 申請番号 -->
			                                <td class="table1_tr2_td1">
			                                	<a href="./ApprovalRejectedServlet?AppNum=<%= acb.getApplicationNumber() %>&AppCategory=<%= acb.getApplicationCategory() %>">
			                                		<%= acb.getApplicationNumber() %>
			                                	</a>
			                                </td>
			                                <!-- 申請日 -->
			                                <td class="table1_tr2_td2"><%= acb.getApplicationDate() %></td>
			                                <!-- 申請者 -->
			                                <td class="table1_tr2_td3"><%= acb.getApplicant() %></td>
			                                <!-- 申請区分 -->
			                                <td class="table1_tr2_td4"><%= acb.getApplicationCategory() %></td>
			                                <!-- 申請状況 -->
			                                <% if("申請中".equals(acb.getApplicationStatus())) { %>
			                                	<td class="table1_tr2_td5">
			                                		<span style="color: red; font-weight: 600;"><%= acb.getApplicationStatus() %></span>
			                                	</td>
			                                 <% } else if("承認".equals(acb.getApplicationStatus()) || "却下".equals(acb.getApplicationStatus())) { %>
			                                	<td class="table1_tr2_td5">
			                                		<span style="color: black;"><%= acb.getApplicationStatus() %></span>
			                                	</td>
			                                <% } %>
			                                <!-- コメント -->
			                                <% if(acb.getApplicantComment() == null || acb.getApplicantComment().equals("")) { %>
		                                		<td class="table1_tr2_td6"></td>
		                                	<% } else { %>
		                                		<td class="table1_tr2_td6"><%= acb.getApplicantComment() %></td>
		                                	<% } %>
			                            </tr>
		                            <% } %>
		                        </table>
	                    	<% } %>
						</form>
	                </div>
				</div>
				<div id="./add_account_view.jsp" class="tab-pane"></div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>