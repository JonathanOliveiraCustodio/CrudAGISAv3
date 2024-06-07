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
<title>Analisar Eliminações</title>
<script>
	function validarBusca() {
		var codigo = document.getElementById("codigo").value;
		if (codigo.trim() === "") {
			alert("Por favor, insira um codigo.");
			return false;
		}
		return true;
	}

	function editarEliminacao(codigo) {
		window.location.href = 'analisarEliminacao?cmd=alterar&codigo='
				+ codigo;
	}
</script>
	
</head>
<body>
	<div>
		<jsp:include page="headerSecretaria.jsp" />
	</div>
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Analisar Eliminações</h1>
				<div class="d-flex gap-2 justify-content-center py-2">

					<form action="analisarEliminacao" method="post" class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">Código
							Eliminação:</label>
						<div class="col-md-2">
							<input class="form-control" type="number" min="0" step="1"
								id="codigo" name="codigo" placeholder="Codigo Disciplina"
								value='<c:out value="${analisarEliminacao.codigo }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao"
								class="btn btn-primary" value="Buscar"
								onclick="return validarBusca()">
						</div>
						<label for="data" class="form-label col-md-1">Nome Aluno:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeAluno"
								name="nomeAluno" placeholder="Nome Aluno"
								value='<c:out value="${analisarEliminacao.aluno.nome }"></c:out>'>
						</div>

						<label for="status" class="form-label col-md-1">Status:</label>
						<div class="col-md-3">
							<select class="form-select" id="status" name="status">
								<option value="">Escolha um Status</option>
								<option value="D"
									<c:if test="${analisarEliminacao.status eq 'D'}">selected</c:if>>D</option>
								<option value="Em análise"
									<c:if test="${analisarEliminacao.status eq 'Em análise'}">selected</c:if>>Em
									análise</option>
								<option value="Negado"
									<c:if test="${analisarEliminacao.status eq 'Negado'}">selected</c:if>>Negado</option>

							</select>
						</div>
						<label for="data" class="form-label col-md-1">Nome Instituição:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeInstituicao"
								name="nomeInstituicao" placeholder="Nome Instituição"
								value='<c:out value="${analisarEliminacao.nomeInstituicao }"></c:out>'>
						</div>

						<label for="data" class="form-label col-md-1">Nome Curso:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeCurso"
								name="nomeCurso" placeholder="Nome Curso"
								value='<c:out value="${analisarEliminacao.curso.nome }"></c:out>'>
						</div>
						
						<label for="dataEliminacao" class="form-label col-md-1">Data Solicitação:</label>
						<div class="col-md-3">
							<input class="form-control" type="date" id="dataEliminacao"
								name="dataEliminacao" placeholder="Data Solicitação"
								value='<c:out value="${analisarEliminacao.dataEliminacao }"></c:out>'>
						</div>

						<div class="col-md-3"></div>
						<br />
						
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Alterar"
								class="btn btn-success">
						</div>
						
						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Listar"
								class="btn btn-primary">
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
	<div class="container py-4 text-center d-flex justify-content-center"
		align="center">
		<c:if test="${not empty eliminacoes }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="10"
							style="text-align: center; font-size: 23px;">Lista de
							Eliminações</th>
					</tr>
					<tr>
						<th>Selecionar</th>
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
							<td><input type="radio" name="opcao" value="${e.codigo}"
								onclick="editarEliminacao(this.value)"
								${e.codigo eq codigoEdicao ? 'checked' : ''} /></td>
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
