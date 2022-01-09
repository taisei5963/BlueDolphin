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
	Integer number = (Integer)session.getAttribute("num");
	String naming = (String)session.getAttribute("name");
	String email = (String)session.getAttribute("email");
	String pass1 = (String)session.getAttribute("pass1");
	Integer authorities = (Integer)session.getAttribute("authority");
	
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
		<title>アカウント追加｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/add_account_check.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
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
							<a href="./add_account_view.jsp" class="nav-link active">アカウント追加</a>
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
			<div id="tab3" class="tab-content"></div>
			<div id="tab4" class="tab-content"></div>
			<div id="tab5" class="tab-content"></div>
			<% if(authority == 1) { %>
				<div id="tab6" class="tab-content"></div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="tab6" class="tab-content"></div>
				<div id="./add_account_view.jsp" class="tab-pane active">
					<h2 align="center" class="head2">アカウント追加</h2>
					<h5 align="center" style="font-size: 16px;">
					以下の内容でアカウントを作成します。<br>
					問題がなければ、「登録」ボタンを押下してください。
					</h5>
						<form action="AccountAdditionServlet" method="post">
		                    <div align="center">
		                        <table class="table1">
		                            <tr class="table1_tr1">
		                                <td class="table1_tr1_td1" align="right" width="520">社員番号</td>
		                                <td class="table1_tr1_td2" align="left">　<%= number %></td>
		                            </tr>
		                            <tr class="table1_tr2">
		                                <td class="table1_tr2_td1" align="right">社員氏名</td>
		                                <td class="table1_tr2_td2" align="left">　<%= naming %></td>
		                            </tr>
		                            <tr class="table1_tr3">
		                                <td class="table1_tr3_td1" align="right">社用メールアドレス</td>
		                                <td class="table1_tr3_td2" align="left">　<%= email %></td>
		                            </tr>
		                            <tr class="table1_tr4">
		                                <td class="table1_tr4_td1" align="right">パスワード</td>
		                                <td class="table1_tr4_td2" align="left">　
		                                	<% for(int i=0; i<pass1.length(); i++) { %>
		                                		&#xFF0A;
		                                	<% } %>
		                                </td>
		                            </tr>
		                            <tr class="table1_tr5">
		                                <td class="table1_tr5_td1" align="right">ユーザ種別</td>
		                                <td class="table1_tr5_td2" align="left">　
		                                	<% if(authorities == 0) { %>
		                                		一般ユーザ
		                                	<% } else if(authorities == 1) { %>
		                                		承認者権限付きユーザ
		                                	<% } else if(authorities == 2) { %>
		                                		管理者権限付きユーザ
		                                	<% } %>
		                                </td>
		                            </tr>
		                        </table>
	                        <input type=button class="reset" onclick="location.href='./add_account_view.jsp'" value="入力内容を編集">
	                        <input type="submit" class="submit_addtion" value="入力内容を登録">
	                    </div>
		            </form>
				</div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>