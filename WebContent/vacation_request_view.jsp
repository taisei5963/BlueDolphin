<%@page import="Bean.PaidHolidayBean"%>
<%@page import="Bean.VacationRequestBean"%>
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
	ArrayList<VacationRequestBean> nameList = (ArrayList<VacationRequestBean>)session.getAttribute("nameList");
	@SuppressWarnings("unchecked")
	ArrayList<PaidHolidayBean> phList = (ArrayList<PaidHolidayBean>)session.getAttribute("phList");
	
	String vacation_name = (String)session.getAttribute("vacation_name");
	String classification = (String)session.getAttribute("classification");
	
	
	//セッションにて取得したリストのNULLチェック
	if(userlist == null || nameList == null || phList == null){
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
		<title>休暇申請｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/vacation_request_view.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"/>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.css"/>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js" defer></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" defer></script>
        <script src="https://code.jquery.com/jquery-3.4.1.min.js" ntegrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-multidatespicker/1.6.6/jquery-ui.multidatespicker.min.js" defer></script>
        <script type="text/javascript" src="common/js/vacationChange.js"></script>
        <script type="text/javascript" src="common/js/multiDatesPicker.js"></script>
        
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
						<a href="./various_applications_view.jsp" class="nav-link  active">各種申請</a>
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
			<div id="./transportation_expenses_view.jsp" class="tab-content"></div>
			<div id="./various_applications_view.jsp" class="tab-content active">
				 <div class="div" align="center">
					 <form action="VacationRequestProcessServlet" method="post" name="vacateForm">
	                	<h2 class="context">休暇申請</h2>
	                	<p class="content1">以下の内容を入力のうえ、「承認申請」ボタンを押下してください。</p>
	                    <table class="table1">
	                        <tr class="table1_tr1">
	                            <td class="table1_tr1_td1" align="right">申請内容：</td>
	                            <td class="table1_tr1_td2">
	                            	<select name="vacation_name" id="vacation" class="table1_tr1_td2_select" required>
	                            		<% for(VacationRequestBean vrb : nameList) { %>
	                            			<% if(vrb.getVacationName().equals(vacation_name)) { %>
	                            				<option value="<%= vrb.getVacationName() %>" selected>
	                            					<%= vrb.getVacationName() %>
	                            				</option>
	                            			<% } else { %>
	                            				<option value="<%= vrb.getVacationName() %>">
	                            					<%= vrb.getVacationName() %>
	                            				</option>
	                            			<% } %>
	                            		<% } %>
	                            	</select>
	                            </td>
	                        </tr>
	                        <% if("年次有給休暇".equals(vacation_name)) { %>
	                        	<tr class="table1_tr2">
	                                <td class="table1_tr2_td1" align="center">申請方法</td>
	                                <td class="table1_tr2_td2" align="left">
	                                	<% if("複数日申請".equals(classification)) { %>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio1" value="単日数申請" onclick="document.vacateForm.submit();">単日数申請<br>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio2" value="複数日申請" onclick="document.vacateForm.submit();" checked>複数日申請<br>
	                                    	<input type="radio" name="classification" class="table1_tr2_td2_radio3" value="範囲指定申請" onclick="document.vacateForm.submit();">範囲指定申請
	                                	<% } else if("範囲指定申請".equals(classification)) { %>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio1" value="単日数申請" onclick="document.vacateForm.submit();">単日数申請<br>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio2" value="複数日申請" onclick="document.vacateForm.submit();">複数日申請<br>
	                                    	<input type="radio" name="classification" class="table1_tr2_td2_radio3" value="範囲指定申請" onclick="document.vacateForm.submit();" checked>範囲指定申請
	                                	<% } else if("単日数申請".equals(classification)) { %>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio1" value="単日数申請" onclick="document.vacateForm.submit();" checked>単日数申請<br>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio2" value="複数日申請" onclick="document.vacateForm.submit();">複数日申請<br>
	                                    	<input type="radio" name="classification" class="table1_tr2_td2_radio3" value="範囲指定申請" onclick="document.vacateForm.submit();">範囲指定申請
	                                	<% } else { %>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio1" value="単日数申請" onclick="document.vacateForm.submit();">単日数申請<br>
	                                		<input type="radio" name="classification" class="table1_tr2_td2_radio2" value="複数日申請" onclick="document.vacateForm.submit();">複数日申請<br>
	                                    	<input type="radio" name="classification" class="table1_tr2_td2_radio3" value="範囲指定申請" onclick="document.vacateForm.submit();">範囲指定申請
	                                	<% } %>
	                                </td>
	                            </tr>
	                        <% } %>
	                        <% if(!"特別時間休暇".equals(vacation_name)) { %>
	                        	<tr class="table1_tr3">
		                        	<td class="table1_tr3_td1" align="center">申請日</td>
		                        	<% if("年次有給休暇".equals(vacation_name) && "複数日申請".equals(classification)) { %>
		                        		<td class="table1_tr3_td2">
		                                    <p class="table1_tr3_td2_p">注：一度に申請できる日数は、10日分までです。</p>
		                                    <div type="text" id="js-datepicker">
		                                        <input type="hidden" name="" id="datepickerValue">
		                                    </div>
		                                    <textarea readonly="" id="result" class="table1_tr3_td2_textarea" name="application_date"></textarea>
		                                </td>
		                        	<% } else if("年次有給休暇".equals(vacation_name) && "範囲指定申請".equals(classification)) { %>
		                        		<td class="table1_tr3_td2">
		                        			開始日：<input type="date" name="date1" class="table1_tr3_td2_date">　〜　
		                        			終了日：<input type="date" name="date2" class="table1_tr3_td2_date">
		                        		</td>
		                        	<% } else { %>
		                        		<td class="table1_tr3_td2">
		                        			<input type="date" name="date1" class="table1_tr3_td2_date">
		                        		</td>
		                        	<% } %>
		                        </tr>
	                        <% } else {} %>
	                        <% if("特別時間休暇".equals(vacation_name)) { %>
	                        	<tr class="table1_tr4">
	                        		<td class="table1_tr4_td1" align="center">申請時間</td>
	                        		<td class="table1_tr4_td2">
	                                    <input type="time" class="table1_tr4_td2_time1" value="00:00" name="start_time">　〜　
	                                    <input type="time" class="table1_tr4_td2_time2" value="00:00" name="end_time">
	                                </td>
	                        	</tr>
	                        <% } %>
	                        <tr class="table1_tr5">
	                            <td class="table1_tr5_td1" align="center">備考</td>
	                            <td class="table1_tr5_td2">
                                    <textarea name="remarks" class="table1_tr5_td2_textarea" cols="50" rows="2"></textarea>
                                </td>
                            </tr>
	                        <% if("年次有給休暇".equals(vacation_name) || "特別時間休暇".equals(vacation_name)) { %>
		                        <tr class="table1_tr4">
			                        <td class="table1_tr4_td1" colspan="2">
			                        	<% for(PaidHolidayBean phb : phList) { %>
			                        		<% if(phb.getPeriodUse() == null || phb.getPeriodUse().equals("")) { %>
			                        			<div class="table1_tr4_td1_div">取得できる日数が、付与されておりません。</div>
			                        		<% } else { %>
			                        			<div class="table1_tr4_td1_div">取得可能日数：<%= phb.getUndigestionDays() %>日</div>
			                        		<% } %>
			                        	<% } %>
			                        </td>
			                    </tr>
			                <% } %>
	                    </table>
	                    <button type="button" onclick="location.href='./various_applications_view.jsp'" class="button_button">戻る</button>
                    	<button type="submit" name="process" value="承認申請処理" class="button_submit">承認申請</button>
                    </form>
                </div>
			</div>
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