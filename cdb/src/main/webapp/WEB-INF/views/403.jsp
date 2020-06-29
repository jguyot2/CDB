<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<!--
	Attributs (optionnels) :
		errorCause : Description de l'erreur rencontrée
 -->
<html>
<head>
	<title>Computer Database</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapStyle" />
<spring:url value="/resources/css/font-awesome.css" var="fontAweSomeStyle" />
<spring:url value="/resources/css/main.css" var="mainCss" />

<link href="${bootstrapStyle}" rel="stylesheet" media="screen">
<link href="${fontAweSomeStyle}" rel="stylesheet" media="screen">
<link href="${mainCss}" rel="stylesheet" media="screen">

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 403: Access denied!
				
				<br/>
				<!-- stacktrace -->
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