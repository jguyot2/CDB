<!DOCTYPE html>
<%--
	Attributs (optionnels) :
		errorCause : Description de l'erreur 
 --%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%
    response.setStatus(400);
%>
<html>
<head>
<title>r u retarded ?</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<spring:url value="/resources/css/bootstrap.min.css"
	var="bootstrapStyle" />
<spring:url value="/resources/css/font-awesome.css"
	var="fontAweSomeStyle" />
<spring:url value="/resources/css/main.css" var="mainCss" />

<link href="${bootstrapStyle}" rel="stylesheet" media="screen">
<link href="${fontAweSomeStyle}" rel="stylesheet" media="screen">
<link href="${mainCss}" rel="stylesheet" media="screen">

<style>
.centered {
	display: block;
	margin-left: auto;
	margin-right: auto;
	border-radius: 10%;
	width: 40%;
}
</style>

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> Application - Computer
				Database </a>
		</div>
	</header>
	

<spring:url value="/resources/imgs/400.jpeg" var="err400Img" />
	
	<img class="centered" src="${err400Img}" alt="error 400" />

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				<c:if test="${not empty requestScope.errorCause}">
					<br /> Cause(s) : ${requestScope.errorCause} <br />
				</c:if>
			</div>
		</div>
	</section>
	<spring:url value="/resources/js/jquery.min.js" var="jqueryMinJS" />
	<spring:url value="/resources/js/bootstrap.min.js" var="bootsrapJS" />
	<spring:url value="/resources/js/dashboard.js" var="dashboardJS" />

	<script src="${jqueryMinJS}"></script>
	<script src="${bootsrapJS}"></script>
	<script src="${dashboardJS}"></script>

</body>
</html>