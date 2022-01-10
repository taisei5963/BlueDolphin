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
	ArrayList<LoginBean> userlist = (ArrayList<LoginBean>)session.getAttribute("userlist");
	String pro_result = (String)session.getAttribute("pro_result");
	
	String bumon = null, name = null, kinmu = null;
	Integer emp_num = null, authority = null;
	
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
		<title>アカウント削除｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/delete_account_result.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
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
						<a href="./KinmuhyoServlet" class="nav-link">勤務表</a>
					</li>
					<li class="nav-item">
						<a href="#tab3" class="nav-link">経費精算</a>
					</li>
					<li class="nav-item">
						<a href="#tab4" class="nav-link">各種申請</a>
					</li>
					<li class="nav-item">
						<a href="#tab5" class="nav-link">在宅勤務申請</a>
					</li>
					<% if(authority == 1) { %>
						<li class="nav-item">
							<a href="#tab6" class="nav-link">承認却下</a>
						</li>
					<% } %>
					<% if(authority == 2) { %>
						<li class="nav-item">
							<a href="#tab6" class="nav-link">承認却下</a>
						</li>
						<li class="nav-item">
							<a href="./add_account_view.jsp" class="nav-link">アカウント追加</a>
						</li>
						<li class="nav-item">
							<a href="./delete_account_search.jsp" class="nav-link active">アカウント削除</a>
						</li>
					<% } %>
				</ul>
			</main>
		</div>
		<div class="tab-content">
			<div id="./KintaiViewServlet" class="tab-pane"></div>
			<div id="./KinmuhyoServlet" class="tab-pane"></div>
			<div id="tab3" class="tab-pane"></div>
			<div id="tab4" class="tab-pane"></div>
			<div id="tab5" class="tab-pane"></div>
			<% if(authority == 1) { %>
				<div id="tab6" class="tab-content"></div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="tab6" class="tab-content"></div>
				<div id="./add_account_view.jsp" class="tab-pane"></div>
				<div id="./delete_account_search.jsp" class="tab-pane active">
					<h2 align="center" class="head2">アカウント削除</h2>
					<% if("削除成功".equals(pro_result)){ %>
						<h5 align="center" class="head5">従業員情報削除が完了しました。</h5>
						<input type="button" class="button_back" value="戻る" onclick="location.href='./delete_account_search.jsp'">
					<% } else if("削除失敗".equals(pro_result)){ %>
						<h5 align="center" class="head5">従業員情報削除に失敗しました。</h5>
						<input type="button" class="button_back" value="戻る" onclick="location.href='./delete_account_search.jsp'">
					<% } %>
					
				</div>
			<% } %>
		</div>
	</body>
</html>