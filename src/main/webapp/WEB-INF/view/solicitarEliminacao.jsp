<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<title>Solicitar Eliminação</title>

<script>
	function validarBusca() {
		var RA = document.getElementById("RA").value;
		if (RA.trim() === "") {
			alert("Por favor, insira um RA.");
			return false;
		}
		return true;
	}
</script>


</head>
<body>
	<div>
		<jsp:include page="headerSecretaria.jsp" />
	</div>
	<br />
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Solicitar Eliminação</h1>
				<div class="d-flex gap-2 justify-content-center py-2">

					<form action="solicitarEliminacao" method="post"
						class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">RA:</label>
						<div class="col-md-2">
							<input class="form-control" type="number" min="0" step="1"
								id="RA" name="RA" placeholder="RA"
								value='<c:out value="${aluno.RA }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao"
								class="btn btn-primary" value="Buscar"
								onclick="return validarBusca()">
						</div>

						<label for="data" class="form-label col-md-1">Disciplina:</label>
						<div class="col-md-3">
							<select class="form-select" id="disciplina" name="disciplina">
								<option value="0">Escolha uma Disciplina</option>
								<c:forEach var="d" items="${disciplinas }">
									<c:if
										test="${(empty solicitarEliminacao) || (d.codigo ne solicitarEliminacao.disciplina.codigo) }">
										<option value="${d.codigo }"><c:out value="${d }" /></option>
									</c:if>
									<c:if test="${d.codigo eq solicitarEliminacao.disciplina.codigo }">
										<option value="${d.codigo }" selected="selected"><c:out
												value="${d }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<label for="data" class="form-label col-md-1">Nome
							Instituição:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeInstituicao"
								name="nomeInstituicao" placeholder="Nome Instituição"
								value='<c:out value="${solicitarEliminacao.nomeInstituicao }"></c:out>'>
						</div>


						<div></div>



						<div class="col-md-2"></div>
						<br />
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Solicitar"
								class="btn btn-success">
						</div>


						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Listar"
								onclick="return validarBusca()" class="btn btn-primary">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Limpar"
								class="btn btn-secondary">
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
	<br />
	<div class="container py-4 text-center d-flex justify-content-center"
		align="center">
		<c:if test="${not empty eliminacoes }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="7"
							style="text-align: center; font-size: 23px;">Minhas
							Solicitações</th>
					</tr>
					<tr>
						<th>Codigo</th>
						<th>Nome Aluno</th>
						<th>Curso</th>
						<th>Disciplina</th>
						<th>Data</th>
						<th>Status</th>
						<th>Nome Instituição</th>

					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="e" items="${eliminacoes}">
						<tr>
							<td><c:out value="${e.codigo}" /></td>
							<td><c:out value="${e.aluno.nome}" /></td>
							<td><c:out value="${e.curso.nome}" /></td>
							<td><c:out value="${e.disciplina.nome}" /></td>
							<td><fmt:formatDate value="${e.dataEliminacao}"
									pattern="dd/MM/yyyy" /></td>
							<td><c:out value="${e.status}" /></td>
							<td><c:out value="${e.nomeInstituicao}" /></td>

						</tr>
					</c:forEach>
				</tbody>

			</table>
		</c:if>
	</div>
</body>
</html>