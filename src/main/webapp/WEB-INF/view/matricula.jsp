<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>Matricula</title>
</head>
<script>
	function validarBusca() {
		var cpf = document.getElementById("RA").value;
		if (cpf.trim() === "") {
			alert("Por favor, insira um RA.");
			return false;
		}
		return true;
	}
</script>
<body>
	<div>
		<jsp:include page="headerAluno.jsp" />
	</div>
	<br />
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Matrícula</h1>
		        <div class="d-flex gap-2 justify-content-center py-2">
		       	 <c:if test="${periodoValido == true}">
		        	<form action="matricula" method="post" class="row g-3 mt-3">
		        		<div class="col-md-1"></div>
			        	<label for="CPF" class="form-label col-md-1">RA:</label>
			        	<div class="col-md-2">
				        	<input
							class="form-control" type="number" id="RA" name="RA"
							placeholder="RA" value='<c:out value="${aluno.RA }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao" value="Buscar" class="btn btn-primary mb-3" onclick="return validarBusca()">
						</div>
						<div class="col-md-1">
							<h6>Aluno: </h6>
						</div>
						<div class="col-md-2">
				        	<input
							class="form-control" type="text" id="nome" name="nome"
							placeholder="Aluno" value='<c:out value="${aluno.nome }"></c:out>' class="form-control" disabled>
						</div>
						<div class="col-md-1">
							<h6>Semestre: </h6>
						</div>
						<div class="col-md-1">
				        	<input
							class="form-control" type="number" id="semestre" name="semestre" value='<c:out value="${matricula.semestre}"></c:out>' class="form-control" disabled>
						</div>
						<div class="container">
						  <div class="row">
						<div class="col table-responsive">
							<table class="table table-light">
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Segunda-Feira</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Segunda-feira')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]" id="${disciplina.codigo}"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col table-responsive">
							<table class="table table-light" >
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Terça-Feira</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Terça-feira')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} id="${disciplina.codigo}" value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col table-responsive">
							<table class="table table-light">
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Quarta-Feira</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Quarta-feira')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} id="${disciplina.codigo}" value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col table-responsive">
							<table class="table table-light">
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Quinta-Feira</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Quinta-feira')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} id="${disciplina.codigo}" value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col table-responsive">
							<table class="table table-light">
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Sexta-Feira</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Sexta-feira')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} id="${disciplina.codigo}" value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div class="col table-responsive">
							<table class="table table-light">
								<thead>
									<tr>
										<th class="titulo-tabela" colspan="5"
											style="text-align: center; font-size: 21px;">Sábado</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="disciplina" items="${disciplinas.get('Sábado')}">
										<tr>
											
											<td><div class="col py-2 px-1 border border-secondary rounded mb-1 bg-secondary-subtle text-secondary-emphasis"><input type="checkbox" name="disciplinaCheckbox[${disciplina.codigo}]"
												 ${disciplinasMatriculadas.contains(disciplina.codigo) ? 'checked disabled' : ''} id="${disciplina.codigo}" value="${disciplina.codigo}">${disciplina.codigo} - ${disciplina.nome}
												<br>
												${disciplina.horaInicio} - ${disciplina.horasSemanais} horas
												<br>
												${disciplina.semestre}º semestre
											</div></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<c:if test="${not empty aluno }">
							<div class="d-flex gap-2 justify-content-center py-2">
								<input type="submit" id="botao" name="botao" value="Matricular" class="btn btn-success mb-3">
							</div>
						</c:if>
							</div>
							</div>
						</form>
						</c:if>
						<c:if test="${periodoValido == false}">
							<h3>
								<b>Fora do período de matrícula</b>
							</h3>
						</c:if>
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
