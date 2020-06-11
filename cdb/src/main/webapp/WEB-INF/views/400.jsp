<!DOCTYPE html>
<%--
	Attributs (optionnels) :
		errorCause : Description de l'erreur 
 --%>
<%response.setStatus(00); %>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> Application - Computer
				Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
			<img src="<%=request.getContextPath() %>/imgs/400.jpeg" alt="error 400">
			<br/>	Cause(s) :
				<% String cause = (String) request.getAttribute("errorCause"); %>
				<%= (cause == null ? "" : cause) %>
				<br />
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>