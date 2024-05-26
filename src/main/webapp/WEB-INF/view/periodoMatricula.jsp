<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
	<div>
		<jsp:include page="headerSecretaria.jsp" />
	</div>
	<br />
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Período de Matrícula</h1>
		        <div class="d-flex gap-2 justify-content-center py-2">
					<form action="periodoMatricula" method="post" class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">Início:</label> 
						<div class="col-md-3">	
							<input class="form-control" type="date" id="periodoMatriculaInicio"
							name="periodoMatriculaInicio"
							value='<c:out value="${curso.periodoMatriculaInicio }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Fim:</label> 
						<div class="col-md-3">	
							<input class="form-control" type="date" id="periodoMatriculaFim"
							name="periodoMatriculaFim"
							value='<c:out value="${curso.periodoMatriculaFim }"></c:out>'>
						</div>
						<br>
						<div class="col-md-4 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Cadastrar" class="btn btn-success">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty erro }">
			<h2>
				<b><c:out value="${erro }" /></b>
			</h2>
		</c:if>
	</div>

	<br />
	<div align="center">
		<c:if test="${not empty saida }">
			<h3>
				<b><c:out value="${saida }" /></b>
			</h3>
		</c:if>
	</div>
</body>
</html>
