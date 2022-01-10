<%@page import="Other.HolidayName"%>
<%@page import="Servlet.KinmuhyoServlet"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.DateFormat"%>
<%@page import="Bean.KinmuAllTimeBean"%>
<%@page import="Bean.KinmuTimeBean"%>
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
	
	String thisMonth = (String)session.getAttribute("thisMonth");
	Integer year = Integer.parseInt(thisMonth.substring(0, 4));
	Integer month = Integer.parseInt(thisMonth.substring(5, 7));
	
	
	KinmuhyoServlet ks = new KinmuhyoServlet();
	HolidayName hn = new HolidayName();
	
	Calendar thisMonthCaleneder = (Calendar) session.getAttribute("thisMonthCaleneder");
	if(thisMonthCaleneder == null) {
		response.sendRedirect("index.jsp");
	}
	
	Calendar cal = Calendar.getInstance();
	
	@SuppressWarnings("unchecked")
	ArrayList<KinmuAllTimeBean> kinmuTimeList = (ArrayList<KinmuAllTimeBean>) session.getAttribute("kinmuTimeList");
	
	String bumon = null, name = null, kinmu = null;
	Integer emp_num = null, authority = null;
	
	for(LoginBean lb : userlist) {
		name = lb.getName();
		emp_num = lb.getNumber();
		authority = lb.getAuthority();
	}

	//メールアドレスが「null」の場合は、ログイン画面に戻る
	if(emp_num == null){
		response.sendRedirect("index.jsp");
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>勤務表｜BlueDolphin</title>
		<link rel="stylesheet" type="text/css" href="common/css/kintai_view.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<script type="text/javascript" src="common/js/screenChange.js"></script>
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
			<div id="./KintaiViewServlet" class="tab-pane"></div>
			<div id="./KinmuhyoServlet" class="tab-pane active">
				<form action="KinmuhyoServlet" method="post" name="pulldownMonth">
					<select id="select" name="thisMonth" class="pulldownSubList">
						<% if(year == 2022) { %>
							<% for(int i=12; i>0; i--) { %>
								<% if(i >= 10) { %>
									<% if(i == month) { %>
										<option value="<%= year %>-<%= i %>" class="pulldownSubList" selected><%= year %>年<%= i %>月</option>
									<% } else { %>
										<option value="<%= year %>-<%= i %>" class="pulldownSubList"><%= year %>年<%= i %>月</option>
									<% } %>
								<% } else { %>
									<% if(i < 10 && i == month) { %>
										<option value="<%= year %>-0<%= i %>" class="pulldownSubList" selected><%= year %>年<%= i %>月</option>
									<% } else if(i < 10 && i != month){ %>
										<option value="<%= year %>-0<%= i %>" class="pulldownSubList"><%= year %>年<%= i %>月</option>
									<% } %>
								<% } %>
							<% } %>
							<% for(int i=12; i>9; i--) { %>
								<% if(i >= 10) { %>
									<% if(i == month) { %>
										<% if(year != 2022) { %>
											<option value="<%= year - 1 %>-<%= i %>" class="pulldownSubList" selected><%= year - 1 %>年<%= i %>月</option>
										<% } else { %>
											<option value="<%= year - 1 %>-<%= i %>" class="pulldownSubList"><%= year - 1 %>年<%= i %>月</option>
										<% } %>
									<% } else if(i != month) { %>
										<option value="<%= year - 1 %>-<%= i %>" class="pulldownSubList"><%= year - 1 %>年<%= i %>月</option>
									<% } %>
								<% } else if(i < 10) { %>
									<% if(i == month) { %>
										<option value="<%= year - 1 %>-0<%= i %>" class="pulldownSubList" selected><%= year - 1 %>年<%= i %>月</option>
									<% } else if(i != month) { %>
										<option value="<%= year - 1 %>-0<%= i %>" class="pulldownSubList"><%= year - 1 %>年<%= i %>月</option>
									<% } %>
								<% } %>
							<% } %>
						<% } %>
						<% if(year == 2021) { %>
							<% for(int i=12; i>0; i--) { %>
								<% if(i >= 10) { %>
									<% if(i == month) { %>
										<option value="<%= year + 1 %>-<%= i %>" class="pulldownSubList" selected><%= year + 1 %>年<%= i %>月</option>
									<% } else { %>
										<option value="<%= year + 1 %>-<%= i %>" class="pulldownSubList"><%= year + 1 %>年<%= i %>月</option>
									<% } %>
								<% } else if(i < 10) { %>
									<% if(i == month) { %>
										<option value="<%= year + 1 %>-0<%= i %>" class="pulldownSubList" selected><%= year + 1 %>年<%= i %>月</option>
									<% } else if(i != month){ %>
										<option value="<%= year + 1 %>-0<%= i %>" class="pulldownSubList"><%= year + 1 %>年<%= i %>月</option>
									<% } %>
								<% } %>
							<% } %>
							<% for(int i=12; i>9; i--) { %>
								<% if(i >= 10) { %>
									<% if(i == month) { %>
										<% if(year != 2021) { %>
											<option value="<%= year %>-<%= i %>" class="pulldownSubList"><%= year %>年<%= i %>月</option>
										<% } else { %>
											<option value="<%= year %>-<%= i %>" class="pulldownSubList" selected><%= year %>年<%= i %>月</option>
										<% } %>
									<% } else { %>
										<option value="<%= year %>-<%= i %>" class="pulldownSubList"><%= year %>年<%= i %>月</option>
									<% } %>
								<% } else { %>
									<% if(i < 10 && i == month) { %>
										<option value="<%= year %>-0<%= i %>" class="pulldownSubList" selected><%= year %>年<%= i %>月</option>
									<% } else { %>
										<option value="<%= year %>-0<%= i %>" class="pulldownSubList"><%= year %>年<%= i %>月</option>
									<% } %>
								<% } %>
							<% } %>
						<% } %>
					</select>
				</form>
				<form action="LastNextMonthServlet" name="LastMonth" method="post">
					<% session.setAttribute("thisMonth", thisMonth); %>
					<input type="hidden" name="ChoiceMonth" value="前月処理">
					<a href="javascript:document.LastMonth.submit()" class="lastMonth">前月</a>
				</form>
				<form action="LastNextMonthServlet" name="NextMonth" method="post">
					<% session.setAttribute("thisMonth", thisMonth); %>
					<input type="hidden" name="ChoiceMonth" value="翌月処理">
					<a href="javascript:document.NextMonth.submit()" class="nextMonth">翌月</a>
				</form>
				
				<div class="currently" align="left">
					<table border="1">
						<tr>
							<th width="130">日付</th><th width="50">出勤</th><th width="50">退勤</th><th width="50	">工数</th>
							<th width="50">実動</th><th width="50">残業</th><th>備考</th>
						</tr>
							<%
								thisMonthCaleneder.add(Calendar.MONTH, -1);
								int dayOfMonth = thisMonthCaleneder.getActualMaximum(Calendar.DAY_OF_MONTH);
								thisMonthCaleneder.add(Calendar.MONTH, 1);
								DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("HH時mm分");
								for(int i=1; i<=dayOfMonth; i++) {
									Boolean chkDateFlag = false;
									cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i);
							%>
							<% if(thisMonth.substring(5, 7).equals("12")) { %>
								<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>	
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else { %>
									<tr>
								<% } %>
							<% } else { %>
								<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
								<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
									<tr style="background-color: #E0FFFF; color: blue;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else if(hn.getHolidayName(cal) != "平日") { %>
									<tr style="background-color: #FFDAB9; color: red;">
								<% } else { %>
									<tr>
								<% } %>
							<% } %>
							
								<!-- 日付 -->
								<td align="center">
								<% if(year == 2022) { %>
									<% if(thisMonth.substring(5, 7).equals("12")) { %>
										<% if(i < 10) { %>
											<%= thisMonth.substring(5, 7) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) %>
										<% } else if(i >= 10){ %>
											<%= thisMonth.substring(5, 7) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) %>
										<% } %>
									<% } else { %>
										<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10){ %>
											<form action="AttendanceUpdateViewServlet" method="post">
												<input type="hidden" name="date" value="<%= year+"-0"+thisMonth.substring(5, 7)+"-"+i %>">
											</form>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
										<% } %>
									<% } %>
								<% } else if(year == 2021) { %>
									<% if(thisMonth.substring(5, 7).equals("12")) { %>
										<% if(i < 10) { %>
											<%= thisMonth.substring(5, 7) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) %>
										<% } else if(i >= 10){ %>
											<%= thisMonth.substring(5, 7) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) %>
										<% } %>
									<% } else { %>
										<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10){ %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10) { %>
											<%= thisMonthCaleneder.get(Calendar.MONTH) + "月" + i + "日" + ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
										<% } %>
									<% } %>
								<% } %>
								</td>
								
								<% for(KinmuAllTimeBean katb : kinmuTimeList) {
										String syukkinDate = katb.getWorkDate().format(DateTimeFormatter.ofPattern("d"));
										if(Integer.parseInt(syukkinDate) == i) {
								%>
										<!-- 出勤 -->
										<td align="center">
											<% if(katb.getStartTime() != null) { %>
												<form action="AttendanceUpdateViewServlet" method="post">
													<input type="hidden" name="Day" value="<%= i %>">
													<input type="submit" size="5" name="startTime" class="StartTime" value="<%= katb.getStartTime() %>">
												</form>
											<% } else { %>
												<form action="AttendanceUpdateViewServlet" method="post">
													<input type="hidden" name="Day" value="<%= i %>">
													<input type="submit" name="startTime" size="5" value="         " class="StartTime">
												</form>
											<% } %>
										</td>
										<!-- 退勤 -->
										<td align="center">
											<% if(katb.getEndTime() != null) { %>
												<form action="AttendanceUpdateViewServlet" method="post">
													<input type="hidden" name="Day" value="<%= i %>">
													<input type="submit" size="5" name="endTime" class="EndTime" value="<%= katb.getEndTime() %>">
												</form>
											<% } else { %>
												<form action="AttendanceUpdateViewServlet" method="post">
													<input type="hidden" name="Day" value="<%= i %>">
													<input type="submit" name="endTime" size="5" value="         " class="EndTime">
												</form>
											<% } %>
										</td>
										<!-- 工数 -->
										<td align="center">
											<!-- 出退勤打刻を行わないと工数登録は行えない -->
											<% if(katb.getStartTime() != null && katb.getEndTime() != null) { %>
												<form action="ManHourViewServlet" method="post">
													<input type="hidden" name="Day" value="<%= i %>">
													<input type="image" src="common/images/plus.png" class="kosu_plus">
												</form>
											<% } %>
										</td>
										<!-- 実動 -->
										<td align="center">
											<% if(katb.getEndTime() != null) { %>
												<%= katb.getActualTime() %>
											<% } else {} %>
										</td>
										<!-- 残業 -->
										<td align="center">
											<% if(katb.getEndTime() != null) { %>
												<%= katb.getZangyoTime() %>
											<% } else {} %>
										</td >
										<!-- 備考 -->
										<% if(katb.getRemarks() != null && !katb.getRemarks().equals("NULL")) { %>
											<td align="left"><%= katb.getRemarks() %></td>
										<% } else if(katb.getRemarks() != null && katb.getRemarks().equals("NULL")) { %>
											<td align="left"></td>
										<% } else {} %>
									<% 
										chkDateFlag = true;
										break;} 
									%>
								<% 
									}
									if(!chkDateFlag) {
								%>
										<!-- 出勤 -->
										<td align="center">
										<% if(year == 2022) { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="startTime" size="5" value="         " class="StartTime">
													</form>
												<% } %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="startTime" size="5" value="         " class="StartTime">
													</form>
												<% } %>
											<% } %>
										<% } else { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="startTime" size="5" value="         " class="StartTime">
													</form>
												<% } %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="startTime" size="5" value="         " class="StartTime">
													</form>
												<% } %>
											<% } %>
										<% } %>
										</td>
										<!-- 退勤 -->
										<td align="center">
										<% if(year == 2022) { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="endTime" size="5" value="         " class="EndTime">
													</form>
												<% } %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="endTime" size="5" value="         " class="EndTime">
													</form>
												<% } %>
											<% } %>
										<% } else { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="endTime" size="5" value="         " class="EndTime">
													</form>
												<% } %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") { %>
												<% } else { %>
													<form action="AttendanceUpdateViewServlet" method="post">
														<input type="hidden" name="Day" value="<%= i %>">
														<input type="submit" name="endTime" size="5" value="         " class="EndTime">
													</form>
												<% } %>
											<% } %>
										<% } %>
										</td>
										<!-- 工数 -->
										<td align="center">
										<% if(year == 2022) { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") { %>
												<% } else {} %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") {} %>
											<% } %>
										<% } else { %>
											<% if(thisMonth.substring(5, 7).equals("12")) { %>
												<% if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（土）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（土）") { %>
												<% } else if(i < 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-0" + i) == "（日）") { %>
												<% } else if(i >= 10 && ks.getYobi(year + "-" + thisMonth.substring(5, 7) + "-" + i) == "（日）") {} %>
											<% } else { %>
												<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
												<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（土）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && ks.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "（日）") { %>
												<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && ks.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "（日）") { %>
												<% } else if(hn.getHolidayName(cal) != "平日") {} %>
											<% } %>
										<% } %>
										</td>
										<td></td><td></td><td></td></tr>
								<% } %>
							<% } %>
					</table>
				</div>
			</div>
			<div id="tab3" class="tab-content"></div>
			<div id="tab4" class="tab-content"></div>
			<div id="tab5" class="tab-content"></div>
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