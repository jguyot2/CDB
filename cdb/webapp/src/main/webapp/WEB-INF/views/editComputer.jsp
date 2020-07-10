<%-- 
Attributs de requête nécessaires
 List<CompanyDTO> companyList : Liste des entreprises.
 ComputerDTO computer : L'instance d'ordinateur modifié
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%@ page isELIgnored="false"%>
<%@ page
	import="com.excilys.model.CompanyDto,java.util.List,com.excilys.model.ComputerDto,java.util.Objects"%>

<c:set var="companyList" value="${requestScope.companyList}" />
<c:set var="computer" value="${requestScope.computer}" />


<!DOCTYPE html>
<html>
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
<link rel="shortcut icon" href="http://icons.iconarchive.com/icons/musett/coffee-shop/64/Croissant-icon.png">

</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> <spring:message
					code="cdb.appName" />
			</a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">id:
						${computer.id}</div>
					<h1>Edit Computer</h1>

					<form:form action="editComputer" method="POST">
						<input type="hidden" value="${computer.id}" id="id" name="id" />
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="cdb.computerName" /></label> <input type="text"
									class="form-control" id="computerName" name="computerName"
									value="${computer.name}">
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="cdb.introduced" /></label> <input type="date"
									class="form-control" id="introduced" name="introduced"
									value="${computer.introduced}">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="cdb.discontinued" /></label> <input type="date"
									class="form-control" id="discontinued" name="discontinued"
									value="${computer.discontinued}">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="cdb.company" /></label> <select class="form-control"
									id="companyId" name="companyId">
									<c:if test="${computer.company != null}">
										<option value="${computer.company.id}">
											${computer.company.name}</option>
									</c:if>
									<option value="0">--</option>
									<c:forEach items="${companyList}" var="company">
										<option value="${company.id}">${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Edit" class="btn btn-primary">
							or <a href="page" class="btn btn-default">Cancel</a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>