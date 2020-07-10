
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page import="com.excilys.model.CompanyDto,java.util.List"%>
<%@ page isELIgnored="false" %>

<c:set value="${requestScope.companyList}" var="companyList" />

<!DOCTYPE html>
<html>
<head>
<title><spring:message code="cdb.appName"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->

<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapStyle" />
<spring:url value="/resources/css/font-awesome.css" var="fontAweSomeStyle" />
<spring:url value="/resources/css/main.css" var="mainCss" />

<link href="${bootstrapStyle}" rel="stylesheet" media="screen">
<link href="${fontAweSomeStyle}" rel="stylesheet" media="screen">
<link href="${mainCss}" rel="stylesheet" media="screen">

<link rel="shortcut icon" href="http://icons.iconarchive.com/icons/musett/coffee-shop/64/Croissant-icon.png">
<script>
// TODO : Validation front (vérifiant que les noms sont pas vides)
</script>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> <spring:message code="cdb.appName"/> </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1><spring:message code="cdb.addUser"/></h1>
					<form:form name="addUser" action="addUser" method="POST"
						onsubmit="return validateForm()">
						<fieldset>
							<div class="form-group">
								<label for="username"><spring:message code="cdb.username"/></label> <input
									type="text" class="form-control" id="username"
									name="username" placeholder="<spring:message code="cdb.username"/>">
							</div>
							<div class="form-group">
								<label for="password"><spring:message code="cdb.password"/></label> <input
									type="password" class="form-control" id="password"
								 	name="password" placeholder="<spring:message code="cdb.password"/>">
							</div>						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="<spring:message code="cdb.add"/>"class="btn btn-primary">
							<spring:message code="cdb.or"/> <a href="page" class="btn btn-default"><spring:message code="cdb.cancel"/></a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>