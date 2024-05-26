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
<title>Aluno</title>
<script>
	function validarBusca() {
		var cpf = document.getElementById("CPF").value;
		if (cpf.trim() === "") {
			alert("Por favor, insira um CPF.");
			return false;
		}
		return true;
	}
</script>
<script>
	function editarAluno(CPF) {
		window.location.href = 'aluno?cmd=alterar&CPF=' + CPF;
	}
	function excluirAluno(CPF) {
		if (confirm("Tem certeza que deseja excluir este Aluno?")) {
			window.location.href = 'aluno?cmd=excluir&codigo=' + CPF;
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
				<h1 class="display-5 fw-bold">Manutenção de Aluno</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="aluno" method="post" class="row g-3 mt-3">
						<label for="CPF" class="form-label col-md-1">CPF:</label>
						<div class="col-md-2">
							<input type="number" id="CPF" name="CPF" class="form-control"
								placeholder="CPF" value='<c:out value="${aluno.CPF }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao"
								class="btn btn-primary mb-3" value="Buscar"
								onclick="return validarBusca()">
						</div>
						<label for="nome" class="form-label col-md-1">Nome:</label>
						<div class="col-md-3">
							<input type="text" id="nome" name="nome" class="form-control"
								placeholder="Nome"
								value='<c:out value="${aluno.nome }"></c:out>'>
						</div>
						<label for="nomeSocial" class="form-label col-md-1">Nome
							Social:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nomeSocial"
								name="nomeSocial" placeholder="Nome Social"
								value='<c:out value="${aluno.nomeSocial }"></c:out>'>
						</div>
						<label for="dataNascimento" class="form-label col-md-1">Data
							Nascimento:</label>
						<div class="col-md-3">
							<input class="form-control" type="date" id="dataNascimento"
								name="dataNascimento" placeholder="Data Nascimento"
								value='<c:out value="${aluno.dataNascimento }"></c:out>'>
						</div>
						<label for="telefoneContato" class="form-label col-md-1">Tel
							Contato:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="telefoneContato"
								name="telefoneContato" placeholder="Telefone Contato"
								value='<c:out value="${aluno.telefoneContato }"></c:out>'>
						</div>
						<label for="emailPessoal" class="form-label col-md-1">E-mail
							Pessoal:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="emailPessoal"
								name="emailPessoal" placeholder="E-mail Pessoal"
								value='<c:out value="${aluno.emailPessoal }"></c:out>'>
						</div>
						<label for="emailCorporativo" class="form-label col-md-1">E-mail
							Corporativo:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="emailCorporativo"
								name="emailCorporativo" placeholder="E-mail Corporativo"
								value='<c:out value="${aluno.emailCorporativo }"></c:out>'>
						</div>
						<label for="dataConclusao2Grau" class="form-label col-md-1">Data
							Conclusão 2Grau:</label>
						<div class="col-md-3">
							<input class="form-control" type="date" id="dataConclusao2Grau"
								name="dataConclusao2Grau" placeholder="Data Conclusao 2 Grau"
								value='<c:out value="${aluno.dataConclusao2Grau }"></c:out>'>
						</div>
						<label for="nome" class="form-label col-md-1">Instituição
							Conclusão 2Grau:</label>
						<div class="col-md-3">
							<input class="form-control" type="text"
								id="instituicaoConclusao2Grau" name="instituicaoConclusao2Grau"
								placeholder="Instituição Conclusao 2 Grau"
								value='<c:out value="${aluno.instituicaoConclusao2Grau }"></c:out>'>
						</div>
						<label for="pontuacaoVestibular" class="form-label col-md-1">Pontuação
							Vestibular:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="pontuacaoVestibular"
								name="pontuacaoVestibular" placeholder="Pontuação Vestibular"
								value='<c:out value="${aluno.pontuacaoVestibular }"></c:out>'
								onkeypress="return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46;">
						</div>
						<label for="posicaoVestibular" class="form-label col-md-1">Posicao
							Vestibular:</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="posicaoVestibular"
								name="posicaoVestibular" placeholder="Posição Vestibular"
								value='<c:out value="${aluno.posicaoVestibular }"></c:out>'>
						</div>
						<label for="anoIngresso" class="form-label col-md-1">Ano
							Ingresso:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" id="anoIngresso"
								name="anoIngresso" placeholder="Ano Ingresso"
								value='<c:out value="${aluno.anoIngresso }"></c:out>'>
						</div>
						<label for="semestreIngresso" class="form-label col-md-1">Semestre
							Ingresso:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" id="semestreIngresso"
								name="semestreIngresso" placeholder="Semestre Ingresso"
								value='<c:out value="${aluno.semestreIngresso }"></c:out>'>
						</div>
						<label for="RA" class="form-label col-md-1">RA:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" id="RA" name="RA"
								placeholder="RA" value="${aluno.RA == null ? '0' : aluno.RA}"
								readonly onkeypress="showAlert()">
						</div>
						<label for="semestreAnoLimiteGraduacao"
							class="form-label col-md-1"> Limite Graduação:</label>
						<div class="col-md-3">
							<input class="form-control" type="date"
								id="semestreAnoLimiteGraduacao"
								name="semestreAnoLimiteGraduacao"
								placeholder="Semestre Limite Gradução"
								value='<c:choose><c:when test="${empty aluno.semestreAnoLimiteGraduacao}">2020-01-01</c:when><c:otherwise><c:out value="${aluno.semestreAnoLimiteGraduacao}"/></c:otherwise></c:choose>'>
						</div>


						<label for="data" class="form-label col-md-1">Curso:</label>
						<div class="col-md-4">
							<select class="form-select" id="curso" name="curso">
								<option value="0">Escolha um Curso</option>
								<c:forEach var="c" items="${cursos }">
									<c:if
										test="${(empty curso) || (c.codigo ne aluno.curso.codigo) }">
										<option value="${c.codigo }">
											<c:out value="${c }" />
										</option>
									</c:if>
									<c:if test="${c.codigo eq aluno.curso.codigo}">
										<option value="${c.codigo }" selected="selected"><c:out
												value="${c }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-6"></div>
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
		<c:if test="${not empty alunos }">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="14"
							style="text-align: center; font-size: 23px;">Lista de Alunos</th>
					</tr>
					<tr>
				    	<th>Selecionar</th>
						<th>CPF</th>
						<th>Nome</th>
						<th>Nome Social</th>
						<th>Dt Nascimento</th>
						<th>Tel Contato</th>

						<!-- 
						<th>E-mail Pessoal</th>
						<th>E-mail Corporativo</th>
						 -->
						<th>Dt Con. 2Grau</th>
						<th>Inst. 2Grau</th>
						<th>Pont. Vest.</th>
						<th>Pos. Vest.</th>
						<th>Ano Ingresso</th>
						<th>Sem. Ingresso</th>
						<th>Sem. Limite</th>
						<th>RA</th>
						<th>Curso</th>
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
							<td><c:out value="${a.nomeSocial }" /></td>
							<td><fmt:formatDate value="${a.dataNascimento}"
									pattern="dd/MM/yyyy" /></td>
							<td>
								<button onclick="window.location.href='telefone?aluno=${a.CPF}'"
									class="btn btn-primary">Telefones</button>
							</td>
							<!--
							<td><c:out value="${a.emailPessoal }" /></td>
							<td><c:out value="${a.emailCorporativo }" /></td>
							-->
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
							<td><c:out value="${a.curso.nome }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>