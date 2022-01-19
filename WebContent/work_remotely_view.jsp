<%@page import="Bean.WorkRemotelyBean"%>
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
	ArrayList<WorkRemotelyBean> reasonList = (ArrayList<WorkRemotelyBean>)session.getAttribute("reasonList");
	
	/* 新規申請者名*/
	String other_name = (String)session.getAttribute("other_name");
	String applicant = (String)session.getAttribute("applicant");
	/* 開始日 */
	String start_application_date = (String)session.getAttribute("start_application_date");
	/* 終了日 */
	String end_application_date = (String)session.getAttribute("end_application_date");
	/* 開始時間 */
	String start_application_time = (String)session.getAttribute("start_application_time");
	/* 終了時間 */
	String end_application_time = (String)session.getAttribute("end_application_time");
	/* 申請理由 */
	String reason_for_application = (String)session.getAttribute("reason_for_application");
	/* 作業内容 */
	String work_content = (String)session.getAttribute("work_content");
	/* 連絡先 */
	String contact_address = (String)session.getAttribute("contact_address");
	
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
		<title>在宅勤務申請｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/work_remotely_view.css">
		<link rel="stylesheet" href="common/css/jquery_range.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.min.css">
		<script type="text/javascript" src="common/js/FiveMinutesUpDownTime.js"></script>
		<script type="text/javascript" src="common/js/TimeRangeReflection.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
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
						<a href="#tab4" class="nav-link">各種申請</a>
					</li>
					<li class="nav-item">
						<a href="./WorkRemotelyViewServlet" class="nav-link active">在宅勤務申請</a>
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
			<div id="./KinmuhyoServlet" class="tab-pane"></div>
			<div id="./transportation_expenses_view.jsp" class="tab-content"></div>
			<div id="tab4" class="tab-content"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-content active">
				<h4 class="head4">在宅勤務申請【入力】</h4>
                <p class="content1">以下の項目を入力の上、「内容確認」ボタンを押してください。</p>
                <form action="WorkRemotelyCheckServlet" method="post">
                    <table class="table1">
                        <tr class="table1_tr1">
                            <td class="table1_tr1_td1" colspan="2">在宅勤務申請</td>
                        </tr>
                        <tr class="table1_tr2">
                            <td class="table1_tr2_td1">申請者</td>
                            <td class="table1_tr2_td2"><span class="table1_tr2_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr3">
                            <td class="table1_tr3_td1" colspan="2">
                            <% if(other_name == null || other_name.equals("")){ %>
                            	<input type="text" name="applicant" maxlength="10" class="table1_tr3_td1_text" 
                                value="<%= name %>" required>
                                <button name="process" value="参照処理" class="table1_tr3_td1_submit">参照</button>
                            <% } else { %>
                                <input type="text" name="applicant" maxlength="10" class="table1_tr3_td1_text" 
                                value="<%= other_name %>" required>
                                <button name="process" value="参照処理" class="table1_tr3_td1_submit">参照</button>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr4">
                            <td class="table1_tr4_td1">開始日</td>
                            <td class="table1_tr4_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr5">
                            <td class="table1_tr5_td1" colspan="2">
                            <% if(start_application_date == null || start_application_date.equals("")){ %>
                                <input type="text" name="start_application_date" class="text_class" value="" required>
                                <div class="appendDatepicker1"></div>
                                <script src="common/js/datepicker_modified_version.js"></script>
                            <% } else { %>
                            	<input type="text" name="start_application_date" class="text_class" 
                            	value="<%= start_application_date %>" required>
                                <div class="appendDatepicker1"></div>
                                <script src="common/js/datepicker_modified_version.js"></script>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr4">
                            <td class="table1_tr4_td1">終了日</td>
                            <td class="table1_tr4_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr5">
                            <td class="table1_tr5_td1" colspan="2">
                            <% if(end_application_date == null || end_application_date.equals("")){ %>
                                <input type="text" name="end_application_date" class="text_class" value="" required>
                                <div class="appendDatepicker2"></div>
                                <script src="common/js/datepicker_modified_version.js"></script>
                            <% } else { %>
                            	<input type="text" name="end_application_date" class="text_class" 
                            	value="<%= end_application_date %>" required>
                                <div class="appendDatepicker2"></div>
                                <script src="common/js/datepicker_modified_version.js"></script>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr6">
                            <td class="table1_tr6_td1">開始時間（予定）</td>
                            <td class="table1_tr6_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr7">
                            <td class="table1_tr7_td1" colspan="2">
                            <% if(start_application_time == null || start_application_time.equals("")){ %>
                                <input type="text" name="start_application_time" class="table1_tr7_td1_text" value="" required>
                            <% } else { %>
                            	<input type="text" name="start_application_time" class="table1_tr7_td1_text" 
                            	value="<%= start_application_time %>" required>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr6">
                            <td class="table1_tr6_td1">終了時間（予定）</td>
                            <td class="table1_tr6_td2"><span class="table1_tr4_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr7">
                            <td class="table1_tr7_td1" colspan="2">
                            <% if(end_application_time == null || end_application_time.equals("")){ %>
                                <input type="text" name="end_application_time" class="table1_tr7_td1_text" value="" required>
                            <% } else { %>
                            	<input type="text" name="end_application_time" class="table1_tr7_td1_text" 
                            	value="<%= end_application_time %>" required>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr8">
                            <td class="table1_tr8_td1">申請理由</td>
                            <td class="table1_tr8_td2"><span class="table1_tr8_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr9">
                            <td class="table1_tr9_td1" colspan="2">
                            <% if(reason_for_application == null || reason_for_application.equals("")){ %>
                                <select name="reason_for_application" class="table1_tr9_td1_select" required>
                                    <option value="">--- 選択してください ---</option>
                                    <% for(WorkRemotelyBean wrbean : reasonList) { %>
                                    	<option value="<%= wrbean.getReason() %>"><%= wrbean.getReason() %></option>
                                    <% } %>
                                </select>
                            <% } else { %>
                            	<select name="reason_for_application" class="table1_tr9_td1_select" required>
                                    <option value="">--- 選択してください ---</option>
                                    <% for(WorkRemotelyBean wrbean : reasonList) { %>
                                    	<% if((reason_for_application.equals(wrbean.getReason()))){ %>
                                    		<option value="<%= wrbean.getReason() %>" selected><%= wrbean.getReason() %></option>
                                    	<% } else { %>
                                    		<option value="<%= wrbean.getReason() %>"><%= wrbean.getReason() %></option>
                                    	<% } %>
                                    <% } %>
                                </select>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr10">
                            <td class="table1_tr10_td1">作業内容（予定）</td>
                            <td class="table1_tr10_td2"><span class="table1_tr10_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr11">
                            <td class="table1_tr11_td1" colspan="2">
                            <% if(work_content == null || work_content.equals("")){ %>
                                <textarea name="work_content" cols="100" rows="5" required></textarea>
                            <% } else { %>
                            	<textarea name="work_content" cols="100" rows="5" required>
                            		<%= work_content %>
                            	</textarea>
                            <% } %>
                            </td>
                        </tr>
                        <tr class="table1_tr12">
                            <td class="table1_tr12_td1">連絡先電話番号（携帯 or 固定）<br><span style="font-size: 15px;">※BP社社員の方は、営業担当者様の携帯電話番号を記載してください。</span></td>
                            <td class="table1_tr12_td2"><span class="table1_tr12_td2_span">必須</span></td>
                        </tr>
                        <tr class="table1_tr13">
                            <td class="table1_tr13_td1" colspan="2">
                            <% if(contact_address == null || contact_address.equals("")){ %>
                                <input type="text" name="contact_address" maxlength="13" class="table1_tr13_td1_text" value="" required>
                            <% } else { %>
                            	<input type="text" name="contact_address" maxlength="13" class="table1_tr13_td1_text" 
                            	value="<%= contact_address %>" required>
                            <% } %>
                            </td>
                        </tr>
                    </table>
                    <button type="reset" class="button_reset">リセット</button>　
                    <button type="submit" name="process" value="内容確認処理" class="button_submit">内容確認</button>
                </form>
			</div>
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