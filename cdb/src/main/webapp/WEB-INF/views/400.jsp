<!DOCTYPE html>
<%--
	Attributs (optionnels) :
		errorCause : Description de l'erreur 
 --%>

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
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
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
	<img class="centered" src="<%= request.getContextPath() %>/imgs/400.jpeg" alt="error 400"/>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
			<c:if test="${not empty requestScope.errorCause}">
				<br /> Cause(s) : ${requestScope.errorCause} <br />
				</c:if>
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>