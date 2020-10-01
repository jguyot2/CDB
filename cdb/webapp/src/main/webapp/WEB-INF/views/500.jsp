<%--
	Erreur 500.
	Attributs: 
		"exn" : Exception ayant causé l'erreur.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>

<html lang="${pageContext.request.locale.language}">
<head>
<title>Computer Database</title>
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

<link rel="shortcut icon" href="http://icons.iconarchive.com/icons/musett/coffee-shop/64/Croissant-icon.png">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 500: An error has occured! <br />
			</div>
		</div>
	</section>
 
	<spring:url value="/resources/js/jquery.min.js" var="jqueryMinJS" />
	<spring:url value="/resources/js/bootstrap.min.js" var="bootsrapJS" />
	<spring:url value="/resources/js/dashboard.js" var="dashboardJS" />

	<script src="${jqueryMinJS }"></script>
	<script src="${bootsrapJS }"></script>
	<script src="${dashboardJS }"></script>


</body>
</html>