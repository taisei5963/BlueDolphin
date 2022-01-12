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
	String name = null, yobi = null, mail = null;
	Integer authority = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		mail = lb.getEmail();
		authority = lb.getAuthority();
	}
	String syukkin = (String)session.getAttribute("syukkin");
	String taikin = (String)session.getAttribute("taikin");
	
	//メールアドレスが「null」の場合は、ログイン画面に戻る
	if(mail == null){
		response.sendRedirect("index.jsp");
	}
	
	Calendar cal = Calendar.getInstance();
	switch(cal.get(Calendar.DAY_OF_WEEK)){
	case Calendar.SUNDAY:
		yobi = "日";
		break;
	case Calendar.MONDAY:
		yobi = "月";
		break;
	case Calendar.TUESDAY:
		yobi = "火";
		break;
	case Calendar.WEDNESDAY:
		yobi = "水";
		break;
	case Calendar.THURSDAY:
		yobi = "木";
		break;
	case Calendar.FRIDAY:
		yobi = "金";
		break;
	case Calendar.SATURDAY:
		yobi = "土";
		break;
	default:
		break;
	}
	LocalDateTime now = LocalDateTime.now();
<<<<<<< HEAD
	HolidayName holidayName = HolidayName.getInstance();
=======
	HolidayName holidayName = new HolidayName();
>>>>>>> 80cffd47c5175d81e090e931f673087ded55d4bc
	cal.set(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
%>
<!DOCTYPE html>
<html>
	<head>
		<title>勤怠打刻エラー｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/kintai_input_error.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<script type="text/javascript" src="common/js/dclock.js"></script>
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
						<a href="./KinmuhyoServlet" class="nav-link active">勤務表</a>
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
							<a href="./delete_account_search.jsp" class="nav-link">アカウント削除</a>
						</li>
					<% } %>
				</ul>
			</main>
		</div>
		<div class="tab-content">
			<div id="./KintaiViewServlet" class="tab-pane active">
				<div class="currently" align="center">
					<table>
						<tr>
							<td class="td1" colspan="2">
								<p id="CurrentDate">
									<% if("平日".equals(holidayName.getHolidayName(cal)) || 
											"土曜日".equals(holidayName.getHolidayName(cal)) || 
											"日曜日".equals(holidayName.getHolidayName(cal))) { %>
										<%= now.format(DateTimeFormatter.ofPattern("yyyy年M月d日")) %>（<%= yobi %>）
									<% } else { %>
										<%= now.format(DateTimeFormatter.ofPattern("yyyy年M月d日")) %>（<%= yobi %>） <%= holidayName.getHolidayName(cal) %>
									<% } %>
								</p>
							</td>
						</tr>
						<tr>
							<td colspan="2" valign="middle">
								<p id="CurrentRealTime"></p>
							</td>
						</tr>
						<tr>
						<% if(syukkin == null) { %>
							<form action="KintaiServlet" method="post">
								<td class="td3" align="center">
									<input type="hidden" name="attendance" value="出勤処理">
									<input type="submit" name="submit" value="出社" id="StartWorkTimeButton" class="Submit">
								</td>
							</form>
						<% } else { %>
							<form action="KintaiServlet" method="post">
								<td class="td3" align="center">
									<input type="hidden" name="attendance" value="出勤処理">
									<input type="submit" name="submit" value="出社" 
									disabled="disabled" id="StartWorkTimeButton" class="Submit2">
								</td>
							</form>
						<% } %>
						<!-- 退社ボタンが押せなくなる条件 -->
						<% if(taikin != null || syukkin == null) { %>
							<form action="KintaiServlet" method="post">
								<td class="td3" align="center">
									<input type="hidden" name="attendance" value="退勤処理">
									<input type="submit" name="submit" value="退社"
										disabled="disabled" id="FinishWorkTimeButton" class="Submit">
								</td>
							</form>
						<% } else { %>
							<form action="KintaiServlet" method="post">
								<td class="td3" align="center">
									<input type="hidden" name="attendance" value="退勤処理">
									<input type="submit" name="submit" value="退社" id="FinishWorkTimeButton" class="Submit">
								</td>
							</form>
						<% } %>
						</tr>
						<tr>
							<td colspan="2" valign="middle">
								<h4 class="kintai_error">勤怠打刻でエラーが発生しました。</h4>
							</td>
						</tr>
					</table>
				</div>
			</div>
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
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>