<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	response.setStatus(404);
%>
<!DOCTYPE html>

<html lang="${pageContext.request.locale.language}" >
<head>
<title><spring:message code="cdb.appName" /></title>
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

<link rel="shortcut icon"
	href="http://icons.iconarchive.com/icons/musett/coffee-shop/64/Croissant-icon.png">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> <spring:message
					code="cdb.appName" />
			</a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">

				<spring:url value="/resources/imgs/404.jpeg" var="err404Img" />
				<img class="centered" src="${err404Img}" alt="error 404" />
			</div>
		</div>
	</section>

	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/dashboard.js"></script>

</body>
</html>