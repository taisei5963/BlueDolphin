<%@page import="java.time.LocalDate"%>
<%@page import="Other.DayOfWeek"%>
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
	
	String display_date = (String)session.getAttribute("display_date");
	String total = (String)session.getAttribute("total");
	
	LocalDate now = LocalDate.now();
	//セッション情報が NULLの場合は、現在の日付を設定
	if(display_date == null) {
		if(now.getMonthValue() < 10) {
			display_date = String.valueOf(now.getYear()) + "-0" + String.valueOf(now.getMonthValue());
		} else {
			display_date = String.valueOf(now.getYear()) + "-" + String.valueOf(now.getMonthValue());
		}
	}
	
	Integer year = Integer.parseInt(display_date.substring(0, 4));
	Integer month = Integer.parseInt(display_date.substring(5, 7));
	
	DayOfWeek week = DayOfWeek.getInstance();
	HolidayName hn = HolidayName.getInstance();
	
	Calendar thisMonthCaleneder = (Calendar) session.getAttribute("thisMonthCaleneder");
	if(thisMonthCaleneder == null) {
		response.sendRedirect("index.jsp");
	}
	
	String total_actual = (String)session.getAttribute("total_actual");
	String total_overtime = (String)session.getAttribute("total_overtime");
	int monthValue = (int)session.getAttribute("monthValue");
	
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
		<title>印刷用画面｜BlueDolphin</title>
		<link rel="stylesheet" type="text/css" href="common/css/time_and_attendace_request_print.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<script type="text/javascript" src="common/js/thisMonthChange.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	</head>
	<body class="body">
		
		<div class="div" align="center">
			<form action="AttendanceRequestProcessServlet" method="post" name="display_date_form">
				<h2 class="context"><%= monthValue %>月度　勤怠報告書</h2>
				<p class="content1">印刷を行う場合は、「印刷」ボタンを押下してください。</p>
				<table border="1" class="table1">
					<tr>
						<th class="table1_tr1_th1" width="100">日にち</th><th class="table1_tr1_th2" width="80">出勤</th>
						<th class="table1_tr1_th3" width="80">退勤</th><th class="table1_tr1_th4" width="80">実動</th>
						<th class="table1_tr1_th5" width="80">残業</th><th class="table1_tr1_th6">備考</th>
					</tr>
						<%
							thisMonthCaleneder.add(Calendar.MONTH, -1);
							int dayOfMonth = thisMonthCaleneder.getActualMaximum(Calendar.DAY_OF_MONTH);
							thisMonthCaleneder.add(Calendar.MONTH, 1);
							for(int i=1; i<=dayOfMonth; i++) {
								Boolean chkDateFlag = false;
								cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i);
						%>
						<% if(display_date.substring(5, 7).equals("12")) { %>
							<% if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(日)") { %>	
								<tr style="background-color: lightgray; color: red;">
							<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(日)") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else { %>
								<tr>
							<% } %>
						<% } else { %>
							<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
							<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
								<tr style="background-color: lightgray; color: blue;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else if(hn.getHolidayName(cal) != "平日") { %>
								<tr style="background-color: lightgray; color: red;">
							<% } else { %>
								<tr>
							<% } %>
						<% } %>
						
							<!-- 日付 -->
							<td align="center">
							<% if(display_date.substring(5, 7).equals("12")) { %>
								<% if(i < 10) { %>
									<%= i + "日　" + week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) %>
								<% } else if(i >= 10){ %>
									<%= i + "日　" + week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) %>
								<% } %>
							<% } else { %>
								<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10) { %>
									<%= i + "日　" + week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10){ %>
									<%= i + "日　" + week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10) { %>
									<%= i + "日　" + week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) %>
								<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10) { %>
									<%= i + "日　" + week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) %>
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
											<%= katb.getStartTime() %>
										<% } else {} %>
									</td>
									<!-- 退勤 -->
									<td align="center">
										<% if(katb.getEndTime() != null) { %>
											<%= katb.getEndTime() %>
										<% } else {} %>
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
									<td align="center">
										<% if(katb.getRemarks() != null && !katb.getRemarks().equals("NULL")) { %>
											<%= katb.getRemarks() %>
										<% } else {} %>
									</td >
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
									<% if(display_date.substring(5, 7).equals("12")) { %>
										<% if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(土)") { %>
										<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(土)") { %>
										<% } else if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(日)") { %>
										<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(日)") { %>
										<% } else {} %>
									<% } else { %>
										<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
										<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
										<% } else if(hn.getHolidayName(cal) != "平日") { %>
										<% } else {} %>
									<% } %>
									</td>
									<!-- 退勤 -->
									<td align="center">
									<% if(display_date.substring(5, 7).equals("12")) { %>
										<% if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(土)") { %>
										<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(土)") { %>
										<% } else if(i < 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-0" + i) == "(日)") { %>
										<% } else if(i >= 10 && week.getYobi(year + "-" + display_date.substring(5, 7) + "-" + i) == "(日)") { %>
										<% } else {} %>
									<% } else { %>
										<% cal.set(year, thisMonthCaleneder.get(Calendar.MONTH), i); %>
										<% if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(土)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i < 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) < 10 && i >= 10 && week.getYobi(year + "-0" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i < 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-0" + i) == "(日)") { %>
										<% } else if(thisMonthCaleneder.get(Calendar.MONTH) >= 10 && i >= 10 && week.getYobi(year + "-" + thisMonthCaleneder.get(Calendar.MONTH) + "-" + i) == "(日)") { %>
										<% } else if(hn.getHolidayName(cal) != "平日") { %>
										<% } else {} %>
									<% } %>
									</td>
									<td></td><td></td><td></td></tr>
							<% } %>
						<% } %>
				</table>
				<table class="table2">
					<tr class="table2_tr1">
						<th class="table2_tr1_th1">合計労働時間</th>
						<% if(total_actual == null) { %>
							<td class="table2_tr1_td1"><%= "00:00" %></td>
						<% } else { %>
							<td class="table2_tr1_td1"><%= total_actual.substring(0, 6) %></td>
						<% } %>
					</tr>
					<tr class="table2_tr2">
						<th class="table2_tr2_th1">合計残業時間</th>
						<% if(total_overtime == null) { %>
							<td class="table2_tr1_td1"><%= "00:00" %></td>
						<% } else { %>
							<td class="table2_tr1_td1"><%= total_overtime.substring(0, 5) %></td>
						<% } %>
					</tr>
					<tr class="table2_tr3">
						<th class="table2_tr1_th1">所定総労働時間</th>
						<td class="table2_tr1_td1"><%= total %></td>
					</tr>
				</table>
				<button type="button" class="submit_back" onclick="location.href='./KinmuhyoServlet'">戻る</button>
                <button type="button" class="submit_print" onclick="window.print();">印刷</button>
			</form>
		</div>
	</body>
</html>