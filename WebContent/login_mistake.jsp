<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="common/css/login_mistake.css">
		<title>ログインエラー｜BlueDolphin</title>
	</head>

	<body>
		<div class="body_div1" align="center">
		<img class="body_img" src="common/images/BlueDolphin.png">
		<p class="body_title">BlueDolphin</p>
		<h5 class="body_error_title">UserIDもしくは、パスワードが間違っております。</h5>
		<form action="LoginServlet" method="POST">
			<table class="body_table">
	            <tr class="body_table_tr1">
	                <td class="body_table_tr1_td"><span class="body_table_tr1_td_span">User ID</span></td>
	            </tr>
	            <tr class="body_table_tr2">
	                <td class="body_table_tr2_td"><input class="body_table_tr2_td_input" type="email" name="mail" maxlength="40" size="40"></td>
	            </tr>
	            <tr class="body_table_tr3">
	                <td class="body_table_tr3_td"><span class="body_table_tr3_td_span">password</span></td>
	            </tr>
	            <tr class="body_table_tr4">
	                <td class="body_table_tr4_td"><input class="body_table_tr4_td_input" type="password" name="pass" maxlength="20" size="40"></td>
	            </tr>
	            <tr class="body_table_tr5">
	                <td class="body_table_tr5_td">
	                    <input class="body_table_tr5_td_input" type="checkbox" name="checkbox"><span class="body_table_tr5_td_span">ログイン情報の保持</span>
	                </td>
	            </tr>
	            <tr class="body_table_tr6">
	                <td class="body_table_tr6_td" align="center"><input class="body_table_tr6_td_input" type="submit" value="ログイン"></td>
	            </tr>
	    	</table>
		</form>
		<hr color="#000">
        <a href="change_password.jsp" class="body_a">Forget your password？</a>
	</div>
</body>
	</body>
</html>