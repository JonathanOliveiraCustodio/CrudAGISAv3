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
<title>CRUD Sistema AGIS</title>
</head>
<body>
	<div>
		<jsp:include page="headerSecretaria.jsp" />
	</div>
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Histórico do Aluno</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="historico" method="post" class="row g-3 mt-3">
						<!-- Primeira Linha do Cabeçalho -->
						<label for="data" class="form-label col-md-1">CPF:</label>
						<div class="col-md-2">
							<input class="form-control" type="text" id="CPF" name="CPF"
								placeholder="CPF" value='<c:out value="${aluno.CPF }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao" value="Buscar"
								class="btn btn-primary mb-3">
						</div>
						<label for="data" class="form-label col-md-1">Nome :</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nome" name="nome"
								placeholder="Nome" disabled
								value='<c:out value="${aluno.nome }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">RA:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" id="RA" name="RA"
								placeholder="RA" disabled
								value='<c:out value="${aluno.RA }"></c:out>'>
						</div>
						<!-- Segunda Linha do Cabeçalho -->
						<label for="data" class="form-label col-md-1">Curso:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="curso" name="curso"
								placeholder="curso" disabled
								value='<c:out value="${aluno.curso.nome }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Matrícula:</label>
						<div class="col-md-3">
							<input class="form-control" type="date" id="dataMatricula"
								name="dataMatricula" placeholder="Matricula" disabled
								value='<c:out value="${aluno.matricula.dataMatricula }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Pontuação
							Vestibular:</label>
						<div class="col-md-1">
							<input class="form-control" type="number" id="pontuacao"
								name="pontuacao" placeholder="Pontuação" disabled
								value='<c:out value="${aluno.pontuacaoVestibular }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Posição
							Vestibular:</label>
						<div class="col-md-1">
							<input class="form-control" type="number" id="posicao"
								name="posicao" placeholder="Posição" disabled
								value='<c:out value="${aluno.posicaoVestibular }"></c:out>'>
						</div>
						<hr class="hr" />

						<c:if test="${not empty erro }">
							<div align="center">
								<h2>
									<b><c:out value="${erro }" /></b>
								</h2>
							</div>
						</c:if>

						<!-- Corpo do Historico -->
						<c:if test="${not empty listaChamadas }">
							<table class="table table-striped">
								<thead>
									<tr>
										<th>Código</th>
										<th>Nome</th>
										<th>Professor</th>
										<th>Nota Final</th>
										<th>Faltas</th>
									</tr>
								</thead>
								<tbody class="table-group-divider">
									<c:forEach var="lc" items="${listaChamadas }">
										<tr>
											<td><c:out value="${lc.disciplina.codigo }" /></td>
											<td><c:out value="${lc.disciplina.nome }" /></td>
											<td><c:out value="${lc.professor.nome }" /></td>
											<td><c:out value="${lc.matriculaDisciplina.notaFinal }" /></td>
											<td><c:out
													value="${lc.matriculaDisciplina.totalFaltas }" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</c:if>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
