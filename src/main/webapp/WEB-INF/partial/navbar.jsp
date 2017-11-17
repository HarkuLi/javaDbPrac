<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">DbPrac</a>
		</div>
		<div class="collapse navbar-collapse" id="navbar">
			<ul class="nav navbar-nav">
				<li id="nav_user"><a href="/javaDbPrac/user/page"><spring:message code="user" text="user" /></a></li>
				<li id="nav_occupation"><a href="/javaDbPrac/occupation/page"><spring:message code="occupation" text="occupation" /></a></li>
				<li id="nav_interest"><a href="/javaDbPrac/interest/page"><spring:message code="interest" text="interest" /></a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li id="nav_sign_up"   class="hidden"><a href="/javaDbPrac/sign_up/page"><span class="glyphicon glyphicon-user"></span> <spring:message code="signUp" text="Sign Up" /></a></li>
				<li id="nav_sign_in"   class="hidden"><a href="/javaDbPrac/sign_in/page"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="signIn" text="Sign In" /></a></li>
				<li id="nav_user_name" class="hidden"><p class="navbar-text"></p></li>
				<li id="nav_sign_out"  class="hidden"><a href="/javaDbPrac/sign_out"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="signOut" text="Sign Out" /></a></li>
				<li id="nav_setting"><a href="/javaDbPrac/setting"><span class="glyphicon glyphicon-cog"></span></a></li>
			</ul>
		</div>
	</div>
</nav>

<script src="/javaDbPrac/js/nav.js"></script>