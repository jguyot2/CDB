<%-- 
Attributs de requête nécessaires
 List<CompanyDTO> companyList : Liste des entreprises.
 ComputerDTO computer : L'instance d'ordinateur modifié
--%>


<%@ page
	import="com.excilys.model.CompanyDTO,java.util.List,com.excilys.model.ComputerDTO,java.util.Objects"%>
<%
    List<CompanyDTO> companyList = (List<CompanyDTO>) request.getAttribute("companyList");
    ComputerDTO computer = (ComputerDTO) request.getAttribute("computer");
%>

<!DOCTYPE html>
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
			<a class="navbar-brand" href="page"> Application -
				Computer Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">id: 0</div>
					<h1>Edit Computer</h1>

					<form action="editComputer" method="POST">
						<input type="hidden" value="<%=computer.getId()%>" id="id" name="id" />
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" id="computerName"
									name="computerName"
									value="<%=computer.getName()%>"
								>
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced" name="introduced"
									value="<%=Objects.toString(computer.getIntroductionDate(), "")%>">
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued" name="discontinued"
									value="<%=Objects.toString(computer.getDiscontinuationDate(), "")%>">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<%
									    if (computer.getCompany() != null) {
									%>
									<option value="<%=computer.getCompany().getId()%>">
										<%=computer.getCompany().getName()%>
									</option>
									<%
									    companyList.remove(computer.getCompany());
									    }
									%>

									<option value="0">--</option>
									<%
									    for (CompanyDTO company : companyList) {
									%>
									<option value="<%=company.getId()%>"><%=company.getName()%></option>
									<%
									    }
									%>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Edit" class="btn btn-primary">
							or <a href="page" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>