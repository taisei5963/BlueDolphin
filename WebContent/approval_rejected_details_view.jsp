<%@page import="Bean.ApprovalApplicationDetailsBean"%>
<%@page import="Bean.ApprovalApplicationBean"%>
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
	ArrayList<ApprovalApplicationDetailsBean> dateList = (ArrayList<ApprovalApplicationDetailsBean>)session.getAttribute("dateList");
	@SuppressWarnings("unchecked")
	ArrayList<ApprovalApplicationBean> appList = (ArrayList<ApprovalApplicationBean>)session.getAttribute("appList");
	
	String rejected = (String)session.getAttribute("rejected");
	
	//ユーザリストのNULLチェック
	if(userlist == null || dateList == null || appList == null){
		response.sendRedirect("LogoutServlet");
	}
	
	String classification = null;
	String date1 = "-", date2 = "-", date3 = "-", date4 = "-", date5 = "-", 
			date6 = "-", date7 = "-", date8 = "-", date9 = "-", date10 = "-";
	for(ApprovalApplicationDetailsBean adb: dateList){
		date1 = adb.getDate1();
		date2 = adb.getDate2();
		date3 = adb.getDate3();
		date4 = adb.getDate4();
		date5 = adb.getDate5();
		date6 = adb.getDate6();
		date7 = adb.getDate7();
		date8 = adb.getDate8();
		date9 = adb.getDate9();
		date10 = adb.getDate10();
		classification = adb.getClassification();
	}
	
	String title = null;
	for(ApprovalApplicationBean ab: appList){
		title = ab.getApplication_title();
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
		<title>承認・却下｜BlueDolphin</title>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/approval_rejected_details_view.css">
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
						<a href="./various_applications_view.jsp" class="nav-link">各種申請</a>
					</li>
					<li class="nav-item">
						<a href="./WorkRemotelyViewServlet" class="nav-link">在宅勤務申請</a>
					</li>
					<% if(authority == 1) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link active">承認・却下</a>
						</li>
					<% } %>
					<% if(authority == 2) { %>
						<li class="nav-item">
							<a href="./ApprovalRejectedViewServlet" class="nav-link active">承認・却下</a>
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
			<div id="./various_applications_view.jsp" class="tab-content"></div>
			<div id="./WorkRemotelyViewServlet" class="tab-content"></div>
			<% if(authority == 1) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content active">
					<div class="div" align="center">
						<form action="ApprovalRejectedDetailsServlet" method="post">
		                    <h2 class="context">申請内容詳細</h2>
		                    <% if(rejected != null) { %>
		                    	<p class="content1">却下理由を記載のうえ、登録ボタンを押下してください。</p>
		                    	<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <td class="table1_tr1_td1">申請番号</td>
		                                <td class="table1_tr1_td2">申請内容</td>
		                                <% if("単日数申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } else if("範囲指定申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">開始日</td>
		                                	<td class="table1_tr1_td4">終了日</td>
		                                <% } else if("複数日申請".equals(classification)) { %>
	                                		<% if(date1 != null || !date1.equals("null")) { %>
		                                		<td class="table1_tr1_td3">申請日１</td>
		                                		<% if(date2 != null && !date2.equals("null")) { %>
													<td class="table1_tr1_td4">申請日２</td>
													<% if(date3 != null && !date3.equals("null")) { %>
														<td class="table1_tr1_td3">申請日３</td>
														<% if(date4 != null && !date4.equals("null")) { %>
															<td class="table1_tr1_td4">申請日４</td>
															<% if(date5 != null && !date5.equals("null")) { %>
																<td class="table1_tr1_td3">申請日５</td>
																<% if(date6 != null && !date6.equals("null")) { %>
																	<td class="table1_tr1_td4">申請日６</td>
																	<% if(date7 != null && !date7.equals("null")) { %>
																		<td class="table1_tr1_td3">申請日７</td>
																		<% if(date8 != null && !date8.equals("null")) { %>
																			<td class="table1_tr1_td4">申請日８</td>
																			<% if(date9 != null && !date9.equals("null")) { %>
																				<td class="table1_tr1_td3">申請日９</td>
																				<% if(date10 != null && !date10.equals("null")) { %>
																					<td class="table1_tr1_td4">申請日10</td>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
											<% } else {} %>
										<% } else { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } %>
		                                <% if("休暇申請".equals(title)) { %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td5">総稼働時間</td>
		                                	<td class="table1_tr1_td6">総残業時間</td>
		                                <% } %>
		                                <td class="table1_tr1_td7">備考</td>
		                                <td class="table1_tr1_td8">却下理由</td>
		                            </tr>
		                            <% for(ApprovalApplicationDetailsBean adb : dateList) { %>
		                            	<tr class="table1_tr2" align="center">
		                            		<!-- 申請番号 -->
			                                <td class="table1_tr2_td1"><%= adb.getApplicationNumber() %></td>
			                                <!-- 申請内容 -->
			                                <td class="table1_tr2_td2"><%= adb.getApplicationName() %></td>
			                                <!-- 申請日１ -->
			                                <% if("単日数申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } else if("範囲指定申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                	<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
			                                <% } else if("複数日申請".equals(classification)) { %>
		                                		<% if(date1 != null && !date1.equals("null")) { %>
			                                		<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                		<% if(date2 != null && !date2.equals("null")) { %>
														<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
														<% if(date3 != null && !date3.equals("null")) { %>
															<td class="table1_tr2_td3"><%= adb.getDate3() %></td>
															<% if(date4 != null && !date4.equals("null")) { %>
																<td class="table1_tr2_td4"><%= adb.getDate4() %></td>
																<% if(date5 != null && !date5.equals("null")) { %>
																	<td class="table1_tr2_td3"><%= adb.getDate5() %></td>
																	<% if(date6 != null && !date6.equals("null")) { %>
																		<td class="table1_tr2_td4"><%= adb.getDate6() %></td>
																		<% if(date7 != null && !date7.equals("null")) { %>
																			<td class="table1_tr2_td3"><%= adb.getDate7() %></td>
																			<% if(date8 != null && !date8.equals("null")) { %>
																				<td class="table1_tr2_td4"><%= adb.getDate8() %></td>
																				<% if(date9 != null && !date9.equals("null")) { %>
																					<td class="table1_tr2_td3"><%= adb.getDate9() %></td>
																					<% if(date10 != null && !date10.equals("null")) { %>
																						<td class="table1_tr2_td4"><%= adb.getDate10() %></td>
																					<% } else {} %>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
			                                <% } else { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } %>
			                                <!-- 総稼働時間 -->
				                            <td class="table1_tr2_td5"><%= adb.getAllActualHours() %></td>
				                            <!-- 総残業時間 -->
				                            <td class="table1_tr2_td6"><%= adb.getAllOvertimeHours() %></td>
			                                <!-- 備考 -->
			                                <td class="table1_tr2_td7"><%= adb.getRemarks() %></td>
			                                <!-- 却下理由 -->
			                                <td class="table1_tr2_td8"><%= adb.getRejectionReason() %></td>
			                            </tr>
		                            <% } %>
		                        </table>
		                    <% } else { %>
		                    	<p class="content1">承認もしくは、却下の対応をお願いいたします。</p>
	                    		<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <td class="table1_tr1_td1">申請番号</td>
		                                <td class="table1_tr1_td2">申請内容</td>
		                                <% if("単日数申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } else if("範囲指定申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">開始日</td>
		                                	<td class="table1_tr1_td4">終了日</td>
		                                <% } else if("複数日申請".equals(classification)) { %>
	                                		<% if(date1 != null || !date1.equals("null")) { %>
		                                		<td class="table1_tr1_td3">申請日１</td>
		                                		<% if(date2 != null && !date2.equals("null")) { %>
													<td class="table1_tr1_td4">申請日２</td>
													<% if(date3 != null && !date3.equals("null")) { %>
														<td class="table1_tr1_td3">申請日３</td>
														<% if(date4 != null && !date4.equals("null")) { %>
															<td class="table1_tr1_td4">申請日４</td>
															<% if(date5 != null && !date5.equals("null")) { %>
																<td class="table1_tr1_td3">申請日５</td>
																<% if(date6 != null && !date6.equals("null")) { %>
																	<td class="table1_tr1_td4">申請日６</td>
																	<% if(date7 != null && !date7.equals("null")) { %>
																		<td class="table1_tr1_td3">申請日７</td>
																		<% if(date8 != null && !date8.equals("null")) { %>
																			<td class="table1_tr1_td4">申請日８</td>
																			<% if(date9 != null && !date9.equals("null")) { %>
																				<td class="table1_tr1_td3">申請日９</td>
																				<% if(date10 != null && !date10.equals("null")) { %>
																					<td class="table1_tr1_td4">申請日10</td>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
											<% } else {} %>
										<% } else { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } %>
		                                <% if("休暇申請".equals(title)) { %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td5">総稼働時間</td>
		                                	<td class="table1_tr1_td6">総残業時間</td>
		                                <% } %>
		                                <td class="table1_tr1_td7">備考</td>
		                                <td class="table1_tr1_td8">却下理由</td>
		                            </tr>
		                            <% for(ApprovalApplicationDetailsBean adb : dateList) { %>
		                            	<tr class="table1_tr2" align="center">
		                            		<!-- 申請番号 -->
			                                <td class="table1_tr2_td1"><%= adb.getApplicationNumber() %></td>
			                                <!-- 申請内容 -->
			                                <td class="table1_tr2_td2"><%= adb.getApplicationName() %></td>
			                                <!-- 申請日 -->
			                                <% if("単日数申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } else if("範囲指定申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                	<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
			                                <% } else if("複数日申請".equals(classification)) { %>
		                                		<% if(date1 != null && !date1.equals("null")) { %>
			                                		<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                		<% if(date2 != null && !date2.equals("null")) { %>
														<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
														<% if(date3 != null && !date3.equals("null")) { %>
															<td class="table1_tr2_td3"><%= adb.getDate3() %></td>
															<% if(date4 != null && !date4.equals("null")) { %>
																<td class="table1_tr2_td4"><%= adb.getDate4() %></td>
																<% if(date5 != null && !date5.equals("null")) { %>
																	<td class="table1_tr2_td3"><%= adb.getDate5() %></td>
																	<% if(date6 != null && !date6.equals("null")) { %>
																		<td class="table1_tr2_td4"><%= adb.getDate6() %></td>
																		<% if(date7 != null && !date7.equals("null")) { %>
																			<td class="table1_tr2_td3"><%= adb.getDate7() %></td>
																			<% if(date8 != null && !date8.equals("null")) { %>
																				<td class="table1_tr2_td4"><%= adb.getDate8() %></td>
																				<% if(date9 != null && !date9.equals("null")) { %>
																					<td class="table1_tr2_td3"><%= adb.getDate9() %></td>
																					<% if(date10 != null && !date10.equals("null")) { %>
																						<td class="table1_tr2_td4"><%= adb.getDate10() %></td>
																					<% } else {} %>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
			                                <% } else { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } %>
			                                <!-- 総稼働時間 -->
				                            <td class="table1_tr2_td5"><%= adb.getAllActualHours() %></td>
				                            <!-- 総残業時間 -->
				                            <td class="table1_tr2_td6"><%= adb.getAllOvertimeHours() %></td>
			                                <!-- 備考 -->
			                                <td class="table1_tr2_td7"><%= adb.getRemarks() %></td>
			                                <!-- 却下理由 -->
			                                <td class="table1_tr2_td8"><%= adb.getRejectionReason() %></td>
			                            </tr>
		                            <% } %>
		                        </table>
		                    <% } %>
	                        <!-- 却下ボタンが押下された場合のみ表示 -->
	                        <% if(rejected != null) { %>
	                        	<table class="table2">
	                        		<tr class="table2_tr1">
                                		<td class="table2_tr1_td1">承認却下理由：</td>
                                		<td class="table2_tr1_td2">
                                    		<textarea name="rejection_reason" class="table2_tr1_td2_textarea" cols="100" rows="1" placeholder="例：出勤日数不足のため等"></textarea>
                                		</td>
                            		</tr>
	                        	</table>
	                        <% } %>
	                        <button type="submit" name="process" class="button_back" value="戻り処理">戻る</button>
	                        <!-- 却下ボタンが押下された場合 -->
	                        <% if(rejected != null) { %>
	                        	<button type="submit" name="process" class="submit_register" value="登録処理">登録</button>
	                        <% } else { %>
	                        	<button type="submit" name="process" class="submit_rejection" value="却下処理">却下</button>
                        		<button type="submit" name="process" class="submit_approval" value="承認処理">承認</button>
	                        <% } %>
						</form>
	                </div>
				</div>
			<% } %>
			<% if(authority == 2) { %>
				<div id="./ApprovalRejectedViewServlet" class="tab-content active">
					<div class="div" align="center">
						<form action="ApprovalRejectedDetailsServlet" method="post">
		                    <h2 class="context">申請内容詳細</h2>
	                        <% if(rejected != null) { %>
		                    	<p class="content1">却下理由を記載のうえ、登録ボタンを押下してください。</p>
		                    	<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <td class="table1_tr1_td1">申請番号</td>
		                                <td class="table1_tr1_td2">申請内容</td>
		                                <% if("単日数申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } else if("範囲指定申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">開始日</td>
		                                	<td class="table1_tr1_td4">終了日</td>
		                                <% } else if("複数日申請".equals(classification)) { %>
		                                	<% if(date1 != null || !date1.equals("null")) { %>
		                                		<td class="table1_tr1_td3">申請日１</td>
		                                		<% if(date2 != null && !date2.equals("null")) { %>
													<td class="table1_tr1_td4">申請日２</td>
													<% if(date3 != null && !date3.equals("null")) { %>
														<td class="table1_tr1_td3">申請日３</td>
														<% if(date4 != null && !date4.equals("null")) { %>
															<td class="table1_tr1_td4">申請日４</td>
															<% if(date5 != null && !date5.equals("null")) { %>
																<td class="table1_tr1_td3">申請日５</td>
																<% if(date6 != null && !date6.equals("null")) { %>
																	<td class="table1_tr1_td4">申請日６</td>
																	<% if(date7 != null && !date7.equals("null")) { %>
																		<td class="table1_tr1_td3">申請日７</td>
																		<% if(date8 != null && !date8.equals("null")) { %>
																			<td class="table1_tr1_td4">申請日８</td>
																			<% if(date9 != null && !date9.equals("null")) { %>
																				<td class="table1_tr1_td3">申請日９</td>
																				<% if(date10 != null && !date10.equals("null")) { %>
																					<td class="table1_tr1_td4">申請日10</td>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
											<% } else {} %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } %>
		                                <% if("休暇申請".equals(title)) { %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td5">総稼働時間</td>
		                                	<td class="table1_tr1_td6">総残業時間</td>
		                                <% } %>
		                                <td class="table1_tr1_td7">備考</td>
		                               <td class="table1_tr1_td8">却下理由</td>
		                            </tr>
		                            <% for(ApprovalApplicationDetailsBean adb : dateList) { %>
		                            	<tr class="table1_tr2" align="center">
		                            		<!-- 申請番号 -->
			                                <td class="table1_tr2_td1"><%= adb.getApplicationNumber() %></td>
			                                <!-- 申請内容 -->
			                                <td class="table1_tr2_td2"><%= adb.getApplicationName() %></td>
			                                <!-- 申請日 -->
			                                <% if("単日数申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } else if("範囲指定申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                	<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
			                                <% } else if("複数日申請".equals(classification)) { %>
			                                	<% if(date1 != null && !date1.equals("null")) { %>
			                                		<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                		<% if(date2 != null && !date2.equals("null")) { %>
														<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
														<% if(date3 != null && !date3.equals("null")) { %>
															<td class="table1_tr2_td3"><%= adb.getDate3() %></td>
															<% if(date4 != null && !date4.equals("null")) { %>
																<td class="table1_tr2_td4"><%= adb.getDate4() %></td>
																<% if(date5 != null && !date5.equals("null")) { %>
																	<td class="table1_tr2_td3"><%= adb.getDate5() %></td>
																	<% if(date6 != null && !date6.equals("null")) { %>
																		<td class="table1_tr2_td4"><%= adb.getDate6() %></td>
																		<% if(date7 != null && !date7.equals("null")) { %>
																			<td class="table1_tr2_td3"><%= adb.getDate7() %></td>
																			<% if(date8 != null && !date8.equals("null")) { %>
																				<td class="table1_tr2_td4"><%= adb.getDate8() %></td>
																				<% if(date9 != null && !date9.equals("null")) { %>
																					<td class="table1_tr2_td3"><%= adb.getDate9() %></td>
																					<% if(date10 != null && !date10.equals("null")) { %>
																						<td class="table1_tr2_td4"><%= adb.getDate10() %></td>
																					<% } else {} %>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
			                                <% } else { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } %>
			                                <% if("休暇申請".equals(title)) { %>
			                                <% } else { %>
			                                	<!-- 総稼働時間 -->
				                                <td class="table1_tr2_td5"><%= adb.getAllActualHours() %></td>
				                                <!-- 総残業時間 -->
				                                <td class="table1_tr2_td6"><%= adb.getAllOvertimeHours() %></td>
			                                <% } %>
			                                <!-- 備考 -->
			                                <td class="table1_tr2_td7"><%= adb.getRemarks() %></td>
			                                <!-- 却下理由 -->
											<td class="table1_tr2_td8"><%= adb.getRejectionReason() %></td>
			                            </tr>
		                            <% } %>
		                        </table>
		                    <% } else { %>
		                    	<p class="content1">承認もしくは、却下の対応をお願いいたします。</p>
		                    	<table class="table1" border="1">
		                            <tr class="table1_tr1" align="center">
		                                <td class="table1_tr1_td1">申請番号</td>
		                                <td class="table1_tr1_td2">申請内容</td>
		                                <% if("単日数申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } else if("範囲指定申請".equals(classification)) { %>
		                                	<td class="table1_tr1_td3">開始日</td>
		                                	<td class="table1_tr1_td4">終了日</td>
		                                <% } else if("複数日申請".equals(classification)) { %>
		                                	<% if(date1 != null || !date1.equals("null")) { %>
		                                		<td class="table1_tr1_td3">申請日１</td>
		                                		<% if(date2 != null && !date2.equals("null")) { %>
													<td class="table1_tr1_td4">申請日２</td>
													<% if(date3 != null && !date3.equals("null")) { %>
														<td class="table1_tr1_td3">申請日３</td>
														<% if(date4 != null && !date4.equals("null")) { %>
															<td class="table1_tr1_td4">申請日４</td>
															<% if(date5 != null && !date5.equals("null")) { %>
																<td class="table1_tr1_td3">申請日５</td>
																<% if(date6 != null && !date6.equals("null")) { %>
																	<td class="table1_tr1_td4">申請日６</td>
																	<% if(date7 != null && !date7.equals("null")) { %>
																		<td class="table1_tr1_td3">申請日７</td>
																		<% if(date8 != null && !date8.equals("null")) { %>
																			<td class="table1_tr1_td4">申請日８</td>
																			<% if(date9 != null && !date9.equals("null")) { %>
																				<td class="table1_tr1_td3">申請日９</td>
																				<% if(date10 != null && !date10.equals("null")) { %>
																					<td class="table1_tr1_td4">申請日10</td>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
											<% } else {} %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td3">申請日</td>
		                                <% } %>
		                                <% if("休暇申請".equals(title)) { %>
		                                <% } else { %>
		                                	<td class="table1_tr1_td5">総稼働時間</td>
		                                	<td class="table1_tr1_td6">総残業時間</td>
		                                <% } %>
		                                <td class="table1_tr1_td7">備考</td>
		                               <td class="table1_tr1_td8">却下理由</td>
		                            </tr>
		                            <% for(ApprovalApplicationDetailsBean adb : dateList) { %>
		                            	<tr class="table1_tr2" align="center">
		                            		<!-- 申請番号 -->
			                                <td class="table1_tr2_td1"><%= adb.getApplicationNumber() %></td>
			                                <!-- 申請内容 -->
			                                <td class="table1_tr2_td2"><%= adb.getApplicationName() %></td>
			                                <!-- 申請日１ -->
			                                <% if("単日数申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } else if("範囲指定申請".equals(classification)) { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                	<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
			                                <% } else if("複数日申請".equals(classification)) { %>
			                                	<% if(date1 != null && !date1.equals("null")) { %>
			                                		<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                		<% if(date2 != null && !date2.equals("null")) { %>
														<td class="table1_tr2_td4"><%= adb.getDate2() %></td>
														<% if(date3 != null && !date3.equals("null")) { %>
															<td class="table1_tr2_td3"><%= adb.getDate3() %></td>
															<% if(date4 != null && !date4.equals("null")) { %>
																<td class="table1_tr2_td4"><%= adb.getDate4() %></td>
																<% if(date5 != null && !date5.equals("null")) { %>
																	<td class="table1_tr2_td3"><%= adb.getDate5() %></td>
																	<% if(date6 != null && !date6.equals("null")) { %>
																		<td class="table1_tr2_td4"><%= adb.getDate6() %></td>
																		<% if(date7 != null && !date7.equals("null")) { %>
																			<td class="table1_tr2_td3"><%= adb.getDate7() %></td>
																			<% if(date8 != null && !date8.equals("null")) { %>
																				<td class="table1_tr2_td4"><%= adb.getDate8() %></td>
																				<% if(date9 != null && !date9.equals("null")) { %>
																					<td class="table1_tr2_td3"><%= adb.getDate9() %></td>
																					<% if(date10 != null && !date10.equals("null")) { %>
																						<td class="table1_tr2_td4"><%= adb.getDate10() %></td>
																					<% } else {} %>
																				<% } else {} %>
																			<% } else {} %>
																		<% } else {} %>
																	<% } else {} %>
																<% } else {} %>
															<% } else {} %>
														<% } else {} %>
													<% } else {} %>
												<% } else {} %>
			                                <% } else { %>
			                                	<td class="table1_tr2_td3"><%= adb.getDate1() %></td>
			                                <% } %>
			                                <% if("休暇申請".equals(title)) { %>
			                                <% } else { %>
			                                	<!-- 総稼働時間 -->
				                                <td class="table1_tr2_td5"><%= adb.getAllActualHours() %></td>
				                                <!-- 総残業時間 -->
				                                <td class="table1_tr2_td6"><%= adb.getAllOvertimeHours() %></td>
			                                <% } %>
			                                <!-- 備考 -->
			                                <td class="table1_tr2_td7"><%= adb.getRemarks() %></td>
			                                <!-- 却下理由 -->
											<td class="table1_tr2_td8"><%= adb.getRejectionReason() %></td>
			                            </tr>
		                            <% } %>
		                        </table>
		                    <% } %>
	                        <!-- 却下ボタンが押下された場合のみ表示 -->
	                        <% if(rejected != null) { %>
	                        	<table class="table2">
	                        		<tr class="table2_tr1">
                                		<td class="table2_tr1_td1">承認却下理由：</td>
                                		<td class="table2_tr1_td2">
                                    		<textarea name="rejection_reason" class="table2_tr1_td2_textarea" cols="100" rows="1" placeholder="例：出勤日数不足のため等"></textarea>
                                		</td>
                            		</tr>
	                        	</table>
	                        <% } %>
	                        <button type="submit" name="process" class="button_back" value="戻り処理">戻る</button>
	                        <!-- 却下ボタンが押下された場合 -->
	                        <% if(rejected != null) { %>
	                        	<button type="submit" name="process" class="submit_register" value="登録処理">登録</button>
	                        <% } else { %>
	                        	<button type="submit" name="process" class="submit_rejection" value="却下処理">却下</button>
                        		<button type="submit" name="process" class="submit_approval" value="承認処理">承認</button>
	                        <% } %>
						</form>
	                </div>
				</div>
				<div id="./add_account_view.jsp" class="tab-pane"></div>
				<div id="./delete_account_search.jsp" class="tab-pane"></div>
			<% } %>
		</div>
	</body>
</html>