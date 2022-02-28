<%@page import="Bean.BumonNameBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="common/css/change_password_error.css">
<title>パスワード再設定エラー｜BlueDolphin</title>
</head>
<body>
	<div id="header">
		<img class="img1" src="common/images/BlueDolphin.png">
		<p class="title">BlueDolphin</p>
		<ul class="head_li">
			<li><a href="#" style="text-decoration: none;">アカウト登録</a></li>
			<li><a href="./index.jsp" style="text-decoration: none;">ログイン画面</a></li>
		</ul>
	</div>

	<div class="sub1" align="center">
		<h2>Change your password</h2>
		<h5>以下項目を入力し、変更ボタンを押してください</h5>
		<form action="ChangePasswordServlet" method="post">
			<table style="margin-top: -5mm;">
				<tr>
					<td>メールアドレス</td>
					<td><input type="email" name="email" maxlength="30" size="30" required></td>
				</tr>
				<tr>
					<td>現在のパスワード</td>
					<td><input type="password" name="old_password" maxlength="30" size="30" required></td>
				</tr>
				<tr>
					<td>新規のパスワード</td>
					<td><input type="password" name="new_password1" maxlength="30" size="30" required></td>
				</tr>
				<tr>
					<td>新規のパスワード（確認用）</td>
					<td><input type="password" name="new_password2" maxlength="30" size="30" required></td>
				</tr>
			</table>
			<p class="error1" align="center">アカウントが存在しません。</p>
			<div><input type="reset" value="修正" class="resetButton"></div>
			<div><input type="submit" value="変更" class="submitButton"></div>
		</form>
	</div>
</body>
</html>