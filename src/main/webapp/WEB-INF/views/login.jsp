<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户登录</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css"
	href="/static/css/libs/bootstrap/3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="/static/style/css/login.css" />
</head>
<body class="student-login" onkeydown="enterlogin();">
	<div class="login-body mod">
		<div class="row">
			<div class="col-md-6">
				<div class="banner center-block"></div>
			</div>
			<div class="col-md-6">
				<div class="form-box">
					<h4 class="skin">登录</h4>
					<div class="form-body">
						<form role="form" action="" method="post" class="login-form"
							id="loginform">
							<p class="error"></p>
							<div class="form-group">
								<label class="sr-only" for="form-username">用户名</label> <i
									class="glyphicon glyphicon-user skin"></i> <input type="text"
									name="username" id="username" placeholder="请输入用户名或邮箱"
									class="form-username form-control">
							</div>
							<div class="form-group">
								<label class="sr-only" for="form-password">密码</label> <i
									class="glyphicon glyphicon-lock skin"></i> <input
									type="password" name="password" id="password"
									placeholder="请输入密码" class="form-password form-control">
							</div>
							<div class="checkbox">
								<label> <input type="checkbox"> 自动登录
								</label> <a href="" class="foget pull-right">忘记密码？</a>
							</div>
							<button type="submit" class="btn" onclick="submitForm()">登&nbsp;&nbsp;录</button>
							<div class="ft">
								<%-- <span class="pull-left">还没有帐号？</span> <a href=""
									class="pull-right skin">立即注册 >></a>--%>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/static/js/jquery-2.2.4.min.js"
		charset="utf-8"></script>
	<script type="text/javascript"
		src="/static/easyui/jquery.easyui.min.js" charset="utf-8"></script>
	<script type="text/javascript" src="/static/login.js" charset="utf-8"></script>
</body>

</html>
