<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>Professor</title>
<script>
	function validarBusca() {
		var codigo = document.getElementById("codigo").value;
		if (codigo.trim() === "") {
			alert("Por favor, insira um Código.");
			return false;
		}
		return true;
	}
	function editarProfessor(codigo) {
		window.location.href = 'professor?cmd=alterar&codigo=' + codigo;
	}
	function excluirProfessor(codigo) {
		if (confirm("Tem certeza que deseja excluir esse Professor?")) {
			window.location.href = 'professor?cmd=excluir&codigo=' + codigo;
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
				<h1 class="display-5 fw-bold">Manutenção de Professor</h1>
		        <div class="d-flex gap-2 justify-content-center py-2">
					<form action="professor" method="post" class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">Código:</label> 
						<div class="col-md-2">	
							<input
							class="form-control" type="number" min="0" step="1" id="codigo"
							name="codigo" placeholder="Codigo Conteudo"
							value='<c:out value="${professor.codigo }"></c:out>'>
						</div>
						<div class="col-md-1">	
							<input type="submit" id="botao" name="botao" class="btn btn-primary mb-3"
							value="Buscar" onclick="return validarBusca()">
						</div>
						<label for="data" class="form-label col-md-1">Nome:</label>
						<div class="col-md-3">	
							<input
							class="form-control" type="text" id="nome" name="nome"
							placeholder="Nome"
							value='<c:out value="${professor.nome }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Titulação:</label>
						<div class="col-md-3">	
							<input
							class="form-control" type="text" id="titulacao" name="titulacao"
							placeholder="Titulacao"
							value='<c:out value="${professor.titulacao }"></c:out>'>
						</div>
						<br>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Cadastrar" class="btn btn-success">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Alterar" class="btn btn-success">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Excluir" class="btn btn-danger">
						</div>
						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Listar" class="btn btn-primary">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Limpar" class="btn btn-secondary">
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
	<div class="container py-4 text-center d-flex justify-content-center" align="center">
		<c:if test="${not empty professores }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="5"
							style="text-align: center; font-size: 23px;">Lista de Professores</th>
					</tr>
					<tr>
					    <th>Selecionar</th>
						<th>Codigo</th>
						<th>Nome</th>
						<th>Titulação</th>
						<th>Excluir</th>
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="p" items="${professores }">
						<tr>
						<td><input type="radio" name="opcao" value="${p.codigo}"
								onclick="editarProfessor(this.value)"
								${p.codigo eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${p.codigo }" /></td>
							<td><c:out value="${p.nome }" /></td>
							<td><c:out value="${p.titulacao }" /></td>
							<td><button class="btn btn-danger" onclick="excluirProfessor('${p.codigo}')">EXCLUIR</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>