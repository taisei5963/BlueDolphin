<%@page import="Bean.BumonNameBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="common/css/change_password.css">
<title>パスワード再設定｜BlueDolphin</title>
</head>
<body>
	<div class="body_div1" align="center">
		<img class="body_img" src="common/images/BlueDolphin.png">
		<p class="body_title">BlueDolphin</p>
		
		<form action="ChangePasswordServlet" method="post">
			<table class="body_table">
				<tr class="body_table_tr1">
					<td class="body_table_tr1_td1">メールアドレス</td>
					<td class="body_table_tr1_td2"><input type="email" name="email" maxlength="30" size="30" required></td>
				</tr>
				<tr class="body_table_tr2">
					<td class="body_table_tr2_td1">新規のパスワード</td>
					<td class="body_table_tr2_td2"><input type="password" name="new_password1" maxlength="30" size="30" required></td>
				</tr>
				<tr class="body_table_tr3">
					<td class="body_table_tr3_td1">新規のパスワード（確認用）</td>
					<td class="body_table_tr3_td2"><input type="password" name="new_password2" maxlength="30" size="30" required></td>
				</tr>
				<tr class="body_table_tr4">
					<td align="center"><input type="reset" value="修正" class="resetButton"></td>
					<td align="center"><input type="submit" value="登録" class="submitButton"></td>
				</tr>
			</table>
		</form>
		<hr color="#000">
        <a href="index.jsp" class="body_a">Return to login</a>
	</div>
</body>
</html>