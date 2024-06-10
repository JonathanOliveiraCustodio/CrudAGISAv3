<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<title>Manter Notas</title>
<script>
	function buscarProfessor() {
		document.getElementById("nota").submit();
	};
</script>


</head>
<body>
	<div>
		<jsp:include page="headerProfessor.jsp" />
	</div>
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Manter Notas</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="nota" method="post" class="row g-3 mt-3"
						id="nota">
						<label for="data" class="form-label col-md-1">Professor:</label>
						<div class="col-md-2">
							<select class="form-select" id="professor" name="professor"
								onChange="buscarProfessor()">
								<option value="0">Escolha um Professor</option>
								<c:forEach var="p" items="${professores}">
									<c:if
										test="${(empty listaNotas) || (p.codigo ne professor.codigo)}">
										<option value="${p.codigo}"><c:out value="${p}" /></option>
									</c:if>
									<c:if test="${p.codigo eq professor.codigo}">
										<option value="${p.codigo}" selected="selected"><c:out
												value="${p}" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<label for="data" class="form-label col-md-1">Disciplina:</label>
						<div class="col-md-3">
							<select class="form-select" id="disciplina" name="disciplina"
								onChange="buscarProfessor()">
								<option value="0">Escolha uma Disciplina</option>
								<c:forEach var="d" items="${disciplinas }">
									<c:if
										test="${(empty listaNotas) || (d.codigo ne disciplina.codigo) }">
										<option value="${d.codigo }"><c:out value="${d }" /></option>
									</c:if>
									<c:if test="${d.codigo eq disciplina.codigo }">
										<option value="${d.codigo }" selected="selected"><c:out
												value="${d }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>

						<div class="col-md-11"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Consultar"
								class="btn btn-primary">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								class="btn btn-success" value="Alterar">
						</div>
						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Limpar"
								class="btn btn-primary">
						</div>
						<div align="center" class="container"></div>
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
						<div
							class="container py-4 text-center d-flex justify-content-center"
							align="center">
							<c:if test="${not empty avaliacoes }">
								<table class="table table-striped">
									<thead>
										<tr>
										</tr>
										<tr>
												<th>Aluno</th>
											<c:forEach var="a" items="${avaliacoes }">
												<th><c:out value="${a.nome }" /></th>
											</c:forEach>
										</tr>
									</thead>
									<tbody class="table-group-divider">
										<c:forEach var="m" items="${matriculas }">
											<tr id="${m.codigoMatricula.codigo }">
												<td><c:out value="${m.n1.nota }" /></td>
												<td><c:out value="${m.n2.nota }" /></td>
												<td><c:out value="${m.n3.nota }" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:if>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
