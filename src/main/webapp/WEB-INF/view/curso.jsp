<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<title>Curso</title>

<script>
	function validarBusca() {
		var cpf = document.getElementById("codigo").value;
		if (cpf.trim() === "") {
			alert("Por favor, insira um Código.");
			return false;
		}
		return true;
	}
	
	function editarCurso(codigo) {
		window.location.href = 'curso?cmd=alterar&codigo=' + codigo;
	}
	function excluirCurso(codigo) {
		if (confirm("Tem certeza que deseja excluir esse Curso?")) {
			window.location.href = 'curso?cmd=excluir&codigo=' + codigo;
		}
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
				<h1 class="display-5 fw-bold">Manutenção de Curso</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="curso" method="post" class="row g-3 mt-3">
						<label for="codigo" class="form-label col-md-1">Código:</label>
						<div class="col-md-2">
							<input class="form-control" type="number" min="0" step="1"
								id="codigo" name="codigo" placeholder="Código"
								value='<c:out value="${curso.codigo }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao"
								class="btn btn-primary mb-3" value="Buscar"
								onclick="return validarBusca()">
						</div>
						<label for="nome" class="form-label col-md-1">Nome:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nome" name="nome"
								placeholder="Nome"
								value='<c:out value="${curso.nome }"></c:out>'>
						</div>
						<label for="cargaHoraria" class="form-label col-md-1">Carga
							Horaria:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" min="0" step="1"
								id="cargaHoraria" name="cargaHoraria"
								placeholder="Carga Horária"
								value='<c:out value="${curso.cargaHoraria }"></c:out>'>
						</div>
						<label for="sigla" class="form-label col-md-1">Sigla:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="sigla" name="sigla"
								placeholder="Sigla"
								value='<c:out value="${curso.sigla }"></c:out>'>
						</div>
						<label for="ultimaNotaENADE" class="form-label col-md-1">Nota
							ENADE:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="ultimaNotaENADE"
								name="ultimaNotaENADE" placeholder="Última Nota ENADE"
								oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1'); if(parseFloat(this.value) > 5.0) this.value = '5.0';"
								value='<c:out value="${curso.ultimaNotaENADE}"></c:out>'>
						</div>
						<label for="turno" class="form-label col-md-1">Turno:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="turno" name="turno"
								placeholder="Turno"
								value='<c:out value="${curso.turno }"></c:out>'>
						</div>
						<br />
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Cadastrar"
								class="btn btn-success">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Alterar"
								class="btn btn-success">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao" value="Excluir"
								class="btn btn-danger">
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
		<c:if test="${not empty cursos }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="8"
							style="text-align: center; font-size: 23px;">Lista de Cursos</th>
					</tr>
					<tr>
						<th>Selecionar</th>
						<th>Código</th>
						<th>Nome</th>
						<th>Carga Horária</th>
						<th>Sigla</th>
						<th>Última Nota ENADE</th>
						<th>Turno</th>
						<th>Excluir</th>
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="c" items="${cursos }">
						<tr>
							<td><input type="radio" name="opcao" value="${c.codigo}"
								onclick="editarCurso(this.value)"
								${c.codigo eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${c.codigo }" /></td>
							<td><c:out value="${c.nome }" /></td>
							<td><c:out value="${c.cargaHoraria }" /></td>
							<td><c:out value="${c.sigla }" /></td>
							<td><c:out value="${c.ultimaNotaENADE }" /></td>
							<td><c:out value="${c.turno }" /></td>
							<td><button class="btn btn-danger" onclick="excluirCurso('${c.codigo}')">EXCLUIR</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>
