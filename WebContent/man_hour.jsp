<%@page import="Other.DayOfWeek"%>
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
	@SuppressWarnings("unchecked")
	ArrayList<Integer> manhourcodeList = (ArrayList<Integer>)session.getAttribute("manhourcodeList");
	@SuppressWarnings("unchecked")
	ArrayList<String> manhournameList = (ArrayList<String>)session.getAttribute("manhournameList");
	@SuppressWarnings("unchecked")
	ArrayList<MatterBean> matterlist = (ArrayList<MatterBean>)session.getAttribute("matterList");
	
	String thisMonth = (String)session.getAttribute("thisMonth");
	String thisMonthForm = (String)session.getAttribute("thisMonthForm");
	String actual = (String)session.getAttribute("actual");
	
	DayOfWeek week = DayOfWeek.getInstance();
	
	//ユーザリストのNULLチェック
	if(userlist == null){
		response.sendRedirect("LogoutServlet");
	}
	//工数リストのNULLチェック
	if(manhournameList == null || manhourcodeList == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String name = null, yobi = null;
	Integer authority = null, emp_num = null;
	for(LoginBean lb : userlist) {
		name = lb.getName();
		authority = lb.getAuthority();
		emp_num = lb.getNumber();
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>工数実績登録｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/man_hour.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<script type="text/javascript" src="common/js/FiveMinutesUpDownTime.js"></script>
		<script type="text/javascript" src="common/js/TimeRangeReflection.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
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
			<div id="./KinmuhyoServlet" class="tab-pane active">
				<form action="ManHourRegisterServlet" method="post">
					<div align="center">
		                <table class="table1">
		                    <tr class="table1_tr1">
		                        <td class="table1_tr1_td1" colspan="7">工数実績登録</td>
		                    </tr>
		                    <tr class="table1_tr2">
		                        <td class="table1_tr2_td1" colspan="7"><%= thisMonth %><%= week.getYobi(thisMonthForm) %>　
		                        実動時間 <%= actual.substring(0, 5) %>
		                        </td>
		                    </tr>
		                    <tr class="table1_tr3">
		                        <th class="table1_tr3_th1" rowspan="2" width="30">No</th>
		                        <th class="table1_tr3_th3" rowspan="2" width="750">タスク名</th>
		                        <th class="table1_tr3_th4" width="750">作業時間</th>
		                        <th class="table1_tr3_th5" rowspan="2" colspan="2">工数</th>
		                        <th class="table1_tr3_th6" rowspan="2" width="80">削除</th>
		                    </tr>
		                    <tr class="table1_tr4">
		                    	<th class="table1_tr4_th1">
		                    		<div class="slider_div">
                                        <input type="hidden" class="single-slider" value="0" />
                                        <script src="common/js/jquery_range.js"></script>
                                    </div>
		                    	</th>
		                    </tr>
		                    <% for(int i=0; i<manhourcodeList.size(); i++) { %>
		                    	<tr class="table1_tr5">
		                    		<td class="table1_tr5_td1" align="center"><%= i+1 %></td>
		                    		<td class="table1_tr5_td1" align="left"><%= manhournameList.get(i) %></td>
		                    		<td class="table1_tr5_td1">
		                    			<button type="button" class="range_minus" id="minus_id_<%= i+1 %>" onclick="FiveMinutesDown();">
		                    				<span class="span_range_minus">◀︎</span>
		                    			</button>
		                            	<input type="range" id="range_id_<%= i+1 %>" value="0" min="0" max="600" class="table1_tr5_td1_range"
		                            	step="5" name="range_name_<%= i+1 %>" list="datalist">
		                                <datalist id="datalist">
		                                    <option value="0">0</option>
		                                    <option value="60">1</option>
		                                    <option value="120">2</option>
		                                    <option value="180">3</option>
		                                    <option value="240">4</option>
		                                    <option value="300">5</option>
		                                    <option value="360">6</option>
		                                    <option value="420">7</option>
		                                    <option value="480">8</option>
		                                    <option value="540">9</option>
		                                    <option value="600">10</option>
		                                </datalist>
		                            	<button type="button" class="range_plus" id="plus_id_<%= i+1 %>" onclick="FiveMinutesUp();">
		                            		<span class="span_range_plus">▶︎</span>
		                            	</button>
		                    		</td>
		                    		<td class="table1_tr5_td1">
		                    			<input type="time" id="time_id_<%= i+1 %>" value="00:00" name="time_name_<%= i+1 %>" class="kosu1">
		                    			<script type="text/javascript" src="common/js/TimeRangeOnChange.js"></script>
		                    		</td>
		                    		<td class="table1_tr5_td6">
		                    			<button type="button" class="table1_tr5_td6_button" id="button_id_<%= i+1 %>" name="button_name_<%= i+1 %>"
		                    			value="<%= actual.substring(0, 5) %>">
		                                	<i class="fa fa-clock-o" style="font-size: 13pt;"></i>
		                            	</button>
		                    		</td>
		                    		<% if((i+1) < 11) { %>
		                    			<td class="table1_tr5_td1" align="center"></td>
		                    		<% } else { %>
		                    			<td class="table1_tr5_td1" align="center">
		                    				<% session.setAttribute("manhourName", manhournameList.get(i)); %>
		                    				<button name="process" value="削除処理" class="delete">削除</button>
		                    			</td>
		                    		<% } %>
		                    	</tr>
		                    <% } %>
		                </table>
		                <input type="button" value="戻る" class="button_back" onclick="location.href='./KinmuhyoServlet'">
	                	<input type="button" value="項目追加" class="button_addition" onclick="location.href='./matter_assignment_view.jsp'">
	                	<% session.setAttribute("emp_num", emp_num); %>
	                	<button name="process" value="登録処理" class="submit_register">工数登録</button>
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