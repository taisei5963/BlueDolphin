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
	
	KinmuhyoServlet ks = new KinmuhyoServlet();
	
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
		<title>案件アサイン｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/matter_assignment_view.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" 
		integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">
		<script type="text/javascript" src="common/js/FiveMinutesUpDownTime.js"></script>
		<script type="text/javascript" src="common/js/TimeRangeReflection.js"></script>
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
				<h2 align="center" class="head2">案件アサイン</h2>
				<form action="MatterAssignmentServlet" method="post">
					<div align="center">
		                <table class="table1">
                            <tr class="table1_tr1">
                                <td class="table1_tr1_td1" align="right" width="110">コード</td>
                                <td class="table1_tr1_td2">
                                    <input type="text" name="matter_code" size="5" maxlength="5">
                                </td>
                                <td class="table1_tr1_td3" align="right" width="110">案件名</td>
                                <td class="table1_tr1_td4">
                                    <input type="text" name="matter_name" size="30" maxlength="30">
                                </td>
                                <td class="table1_tr1_td5" width="400">
                                    <input type="hidden" name="process" value="検索処理">
                                    <button type="submit" style="background-color: lightskyblue; border-color: lightskyblue; border-radius: 5px;">
                                        <i class="fas fa-search"></i> 検索
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5" align="center" style="color: red; font-weight: 700;">※コード検索および案件名検索ともに、部分一致検索となります。</td>
                            </tr>
                        </table>
		            </div>
		        </form>
		        <form action="MatterAssignmentServlet" method="post">
		        	<div align="center">
                        <table class="table2">
                            <tr class="table2_tr1" align="center">
                                <th class="table2_tr1_th1" width="30">
                                    <input type="checkbox" name="checkbox_name" id="checkbox_id">
                                </th>
                                <th class="table2_tr1_th2" width="100">案件コード</th>
                                <th class="table2_tr1_th3" width="500">案件名</th>
                            </tr>
                    	</table>
                    </div>
		        </form>
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