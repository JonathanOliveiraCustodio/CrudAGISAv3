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
<title>Relatório Aluno</title>
<script>
	function validarBusca() {
		var codigo = document.getElementById("codigo").value;
		if (codigo.trim() === "") {
			alert("Por favor, insira um codigo.");
			return false;
		}
		return true;
	}

	function editarAluno(CPF) {
		window.location.href = 'relatorioHistoricoAluno?cmd=alterar&CPF=' + CPF;
	}
	
	function abrirEmNovaAba(actionUrl) {
	    const form = document.getElementById('mainForm');
	    const formData = new FormData(form);

	    // Create a new form element
	    const newForm = document.createElement('form');
	    newForm.method = 'post';
	    newForm.action = actionUrl;
	    newForm.target = '_blank'; // Open in a new tab

	    // Append form data to the new form
	    for (const [key, value] of formData.entries()) {
	        const hiddenField = document.createElement('input');
	        hiddenField.type = 'hidden';
	        hiddenField.name = key;
	        hiddenField.value = value;
	        newForm.appendChild(hiddenField);
	    }

	    // Append the new form to the body and submit it
	    document.body.appendChild(newForm);
	    newForm.submit();
	    document.body.removeChild(newForm);
	}

	function limparCampos() {
	    document.getElementById('CPF').value = '';
	    document.getElementById('nomeAluno').value = '';
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
				<h1 class="display-5 fw-bold">Relatório Histórico Aluno</h1>
				<div class="d-flex gap-2 justify-content-center py-2">

					<form action="relatorioHistoricoAluno" method="post"
						class="row g-3 mt-3" id="mainForm">
						<label for="data" class="form-label col-md-1">CPF Aluno:</label>
						<div class="col-md-2">
							<input class="form-control" type="number" min="0" step="1"
								id="CPF" name="CPF" placeholder="CPF Aluno"
								value='<c:out value="${relatorioHistoricoAluno.CPF }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="buscarBotao" name="botao"
								class="btn btn-primary" value="Buscar"
								onclick="return validarBusca()">
						</div>
						<label for="data" class="form-label col-md-1">Nome Aluno:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeAluno"
								name="nomeAluno" placeholder="Nome Aluno"
								value='<c:out value="${relatorioHistoricoAluno.nome }"></c:out>'>
						</div>

						<div class="col-md-3"></div>
						<br />

						<div class="col-md-2 d-grid text-center">
							<button type="button" id="gerarRelatorioBotao"
								class="btn btn-success"
								onclick="abrirEmNovaAba('relatorioHistoricoAlunoGerar')">
								Gerar Relatório</button>
						</div>

						<div class="col-md-2 d-grid text-center"></div>
						<div class="col-md-2 d-grid text-center">
							<input type="submit" id="listarBotao" name="botao" value="Listar"
								class="btn btn-primary">
						</div>
						<div class="col-md-2 d-grid text-center">
							<button type="button" id="limparBotao" class="btn btn-secondary"
								onclick="limparCampos()">Limpar</button>
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
		<c:if test="${not empty alunos }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="14"
							style="text-align: center; font-size: 23px;">Lista de Alunos</th>
					</tr>
					<tr>
						<th>Selecionar</th>
						<th>Nome</th>
						<th>CPF</th>
						<th>Curso</th>					
						<th>Dt Nascimento</th>
						<th>Tel Contato</th>
						<th>Dt Con. 2Grau</th>
						<th>Inst. 2Grau</th>
						<th>Pont. Vest.</th>
						<th>Pos. Vest.</th>
						<th>Ano Ingresso</th>
						<th>Sem. Ingresso</th>
						<th>Sem. Limite</th>
						<th>RA</th>

					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="a" items="${alunos }">
						<tr>
							<td><input type="radio" name="opcao" value="${a.CPF}"
								onclick="editarAluno(this.value)"
								${a.CPF eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${a.CPF }" /></td>
							<td><c:out value="${a.nome }" /></td>
							<td><c:out value="${a.curso.nome }" /></td>
							<td><fmt:formatDate value="${a.dataNascimento}"
									pattern="dd/MM/yyyy" /></td>
							<td>
								<button onclick="window.location.href='telefone?aluno=${a.CPF}'"
									class="btn btn-primary">Telefones</button>
							</td>
							<td><fmt:formatDate value="${a.dataConclusao2Grau}"
									pattern="dd/MM/yyyy" /></td>
							<td><c:out value="${a.instituicaoConclusao2Grau }" /></td>
							<td><c:out value="${a.pontuacaoVestibular }" /></td>
							<td><c:out value="${a.posicaoVestibular }" /></td>
							<td><c:out value="${a.anoIngresso }" /></td>
							<td><c:out value="${a.semestreIngresso }" /></td>
							<td><fmt:formatDate value="${a.semestreAnoLimiteGraduacao}"
									pattern="dd/MM/yyyy" /></td>
							<td><c:out value="${a.RA }" /></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>