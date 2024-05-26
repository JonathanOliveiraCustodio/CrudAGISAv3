<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<title>Conteudo</title>
<script>
	function validarBusca() {
		var codigo = document.getElementById("codigo").value;
		if (codigo.trim() === "") {
			alert("Por favor, insira um codigo.");
			return false;
		}
		return true;
	}

	function editarConteudo(codigo) {
		window.location.href = 'conteudo?cmd=alterar&codigo=' + codigo;
	}
	function excluirConteudo(codigo) {
		if (confirm("Tem certeza que deseja excluir esse Conteúdo?")) {
			window.location.href = 'conteudo?cmd=excluir&codigo=' + codigo;
		}
	}
</script>

</head>
<body>
	<div>
		<jsp:include page="headerProfessor.jsp" />
	</div>
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Manutenção de Conteudos</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="conteudo" method="post" class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">Código
							Conteudo:</label>
						<div class="col-md-1">
							<input class="form-control" type="number" min="0" step="1"
								id="codigo" name="codigo" placeholder="Codigo Conteudo"
								value='<c:out value="${conteudo.codigo }"></c:out>'
								style="width: 80px;">
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao" value="Buscar"
								onclick="return validarBusca()" class="btn btn-primary mb-3">
						</div>
						<label for="data" class="form-label col-md-1">Nome :</label>
						<div class="col-md-4">
							<input class="form-control" type="text" id="nome" name="nome"
								placeholder="Nome"
								value='<c:out value="${conteudo.nome }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Disciplina:</label>
						<div class="col-md-3">
							<select class="form-select" id="disciplina" name="disciplina">
								<option value="0">Escolha uma Disciplina</option>
								<c:forEach var="d" items="${disciplinas }">
									<c:if
										test="${(empty conteudo) || (d.codigo ne conteudo.disciplina.codigo) }">
										<option value="${d.codigo }"><c:out value="${d }" /></option>
									</c:if>
									<c:if test="${d.codigo eq conteudo.disciplina.codigo }">
										<option value="${d.codigo }" selected="selected"><c:out
												value="${d }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<label for="data" class="form-label col-md-1">Descrição:</label>
						<div class="col-md-11">
							<textarea class="form-control col-md-1" type="text"
								id="descricao" name="descricao" placeholder="Descrição">${conteudo.descricao}</textarea>
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								class="btn btn-success" value="Cadastrar">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								class="btn btn-success" value="Alterar">
						</div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								class="btn btn-danger" value="Excluir">
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
		<c:if test="${not empty conteudos}">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="6"
							style="text-align: center; font-size: 23px;">Lista de
							Conteudo</th>
					</tr>
					<tr>
						<th>Selecionar</th>
						<th>Código</th>
						<th>Nome</th>
						<th>Descricao</th>
						<th>Disciplina</th>
						<th>Excluir</th>
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="c" items="${conteudos}">
						<tr>
							<td><input type="radio" name="opcao" value="${c.codigo}"
								onclick="editarConteudo(this.value)"
								${c.codigo eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${c.codigo}" /></td>
							<td><c:out value="${c.nome}" /></td>
							<td><c:out value="${c.descricao}" /></td>
							<td><c:out value="${c.disciplina.nome}" /></td>
							<td><button class="btn btn-danger"
									onclick="excluirConteudo('${c.codigo}')">EXCLUIR</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>
