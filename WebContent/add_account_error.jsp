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
		<title>アカウント追加エラー｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/add_account_error.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
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
							<a href="./ApprovalRejectedViewServlet" class="nav-link">承認・却下</a>
						</li>
					<% } %>
					<% if(authority == 2) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link">承認・却下</a>
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
			<div id="./transportation_expenses_view.jsp" class="tab-pane"></div>
			<div id="./various_applications_view.jsp" class="tab-pane"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-pane"></div>
			<% if(authority == 1) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content"></div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content"></div>
				<div id="./add_account_view.jsp" class="tab-pane active">
					<h2 align="center" class="head2">アカウント追加</h2>
					<h5 align="center" style="color: red; font-size: 16px;">
					入力用と確認用パスワードが一致しませんでした。<br>
					お手数ですが、再入力をお願いいたします。
					</h5>
	                <form action="AccountCheckServlet" method="post">
	                    <div align="center">
	                        <table class="table1">
                            <tr class="table1_tr1">
                                <td class="table1_tr1_td1" align="right">社員番号</td>
                                <td class="table1_tr1_td2">
                                    <input type="text" name="emp_num" size="40" maxlength="40">
                                </td>
                            </tr>
                            <tr class="table1_tr2">
                                <td class="table1_tr2_td1" align="right">社員氏名</td>
                                <td class="table1_tr2_td2">
                                    <input type="text" name="emp_name" size="40" maxlength="40">
                                </td>
                            </tr>
                            <tr class="table1_tr3">
                                <td class="table1_tr3_td1" align="right">社用メールアドレス</td>
                                <td class="table1_tr3_td2">
                                    <input type="email" name="emp_mail" size="40" maxlength="40">
                                </td>
                            </tr>
                            <tr class="table1_tr4">
                                <td class="table1_tr4_td1" align="right">パスワード</td>
                                <td class="table1_tr4_td2">
                                    <input type="password" name="emp_pass1" size="40" maxlength="40">
                                </td>
                            </tr>
                            <tr class="table1_tr5">
                                <td class="table1_tr5_td1" align="right">パスワード（確認用）</td>
                                <td class="table1_tr5_td2">
                                    <input type="password" name="emp_pass2" size="40" maxlength="40">
                                </td>
                            </tr>
                            <tr class="table1_tr6">
                                <td class="table1_tr6_td1" align="right">ユーザ種別</td>
                                <td class="table1_tr6_td2" width="500">
                                    <input type="radio" name="granting_authority" value="0" checked>一般ユーザ　
                                    <input type="radio" name="granting_authority" value="1">承認者権限付きユーザ　
                                    <input type="radio" name="granting_authority" value="2">管理者権限付きユーザ　
                                </td>
                            </tr>
                        </table>
	                        <input type="reset" class="reset" value="入力内容をリセット">
	                        <input type="submit" class="submit_check" value="入力内容を確認">
	                    </div>
	                </form>
				</div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>