<%@page import="Other.DayOfWeek"%>
<%@page import="Servlet.AttendanceUpdateViewServlet"%>
<%@page import="Servlet.KinmuhyoServlet"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.DateFormat"%>
<%@page import="Bean.KinmuAllTimeBean"%>
<%@page import="Bean.KintaiUpdateBean"%>
<%@page import="java.util.List"%>
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
	ArrayList<KintaiUpdateBean> syuttaikinList = (ArrayList<KintaiUpdateBean>)session.getAttribute("syuttaikinList");
	@SuppressWarnings("unchecked")
	ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
	String thisMonth = (String)session.getAttribute("thisMonth");
	
	String bumon = null, name = null, kinmu = null;
	Integer emp_num = null, authority = null;
	
	int year = Integer.parseInt(thisMonth.substring(0, 4));
	int month = Integer.parseInt(thisMonth.substring(5, 7));
	int date = Integer.parseInt(thisMonth.substring(8, 10));
	
	DayOfWeek week = DayOfWeek.getInstance();
	
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
		emp_num = lb.getNumber();
	}

	//メールアドレスが「null」の場合は、ログイン画面に戻る
	if(emp_num == null){
		response.sendRedirect("index.jsp");
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>勤怠時間変更申請エラー｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/kintai_update_error.css">
	</head>
	<body>
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
			<div id="./KinmuhyoServlet" class="tab-pane"></div>
			<div id="./transportation_expenses_view.jsp" class="tab-pane"></div>
			<div id="./various_applications_view.jsp" class="tab-pane active">
				<form action="AttendanceUpdateServlet" method="post">
					<div align="center">
						<h2 class="context">勤怠時間修正申請失敗</h2>
						<table border="1">
							<tr>
								<td align="center">出勤日</td>
								<td align="center" colspan="3"><%= year + "年" + month + "月" + date + "日　" + week.getYobiWithoutParentheses(thisMonth) %></td>
							</tr>
							<tr>
								<td align="center">出勤時間</td>
								<td align="center">
								<% for(KintaiUpdateBean kub : syuttaikinList) { %>
									<input type="text" size="5" class="syukkinTime" name="SyukkinTime" value="<%= kub.getSyukkin().substring(0, 5) %>">
								<% } %>
								</td>
							</tr>
							<tr>
								<td align="center">退勤時間</td>
								<td align="center">
								<% for(KintaiUpdateBean kub : syuttaikinList) { %>
									<% if(kub.getTaikin() == null) { %>
										<input type="text" class="taikinTime" size="5" name="TaikinTime" value="">
									<% } else { %>
										<input type="text" class="taikinTime" size="5" name="TaikinTime" value="<%= kub.getTaikin().substring(0, 5) %>">
									<% } %>
								<% } %>
								</td>
							</tr>
							<tr>
								<td align="center">備考</td>
								<td align="center">
									<textarea rows="3" cols="40" name="Remarks" placeholder="(例)電車遅延のため"></textarea>
								</td>
							</tr>
						</table>
						<input type="button" value="勤務表画面へ戻る" class="backButton" onclick="location.href='./KinmuhyoServlet'">
						<input type="submit" value="再申請" class="submitButton">
						<h4 class="update_error">勤怠修正申請に失敗しました。<br>お手数ですが、再申請を行ってください。</h4>
					</div>
				</form>
			</div>
			<div id="./WorkRemotelyViewServlet" class="tab-pane"></div>
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