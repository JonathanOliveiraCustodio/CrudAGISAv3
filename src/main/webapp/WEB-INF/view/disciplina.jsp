<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>Disciplina</title>

<script>
	function validarBusca() {
		var cpf = document.getElementById("codigo").value;
		if (cpf.trim() === "") {
			alert("Por favor, insira um código.");
			return false;
		}
		return true;
	}
</script>
<script>
	function editarDisciplina(codigo) {
		window.location.href = 'disciplina?cmd=alterar&codigo=' + codigo;
	}
	function excluirDisciplina(codigo, codigoProfessor, codigoCurso) {
		if (confirm("Tem certeza que deseja excluir essa Disciplina?")) {
	
			var url = 'disciplina?cmd=excluir&codigo=' + codigo
					+ '&codigoProfessor=' + codigoProfessor + '&codigoCurso='
					+ codigoCurso;
			window.location.href = url;
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
				<h1 class="display-5 fw-bold">Manutenção de Disciplina</h1>
				<div class="d-flex gap-2 justify-content-center py-2">
					<form action="disciplina" method="post" class="row g-3 mt-3">
						<label for="data" class="form-label col-md-1">Código
							Disciplina:</label>
						<div class="col-md-2">
							<input class="form-control" type="number" min="0" step="1"
								id="codigo" name="codigo" placeholder="Codigo Disciplina"
								value='<c:out value="${disciplina.codigo }"></c:out>'>
						</div>
						<div class="col-md-1">
							<input type="submit" id="botao" name="botao"
								class="btn btn-primary" value="Buscar"
								onclick="return validarBusca()">
						</div>
						<label for="data" class="form-label col-md-1">Nome :</label>
						<div class="col-md-3">
							<input class="form-control" type="text" id="nome" name="nome"
								placeholder="Nome"
								value='<c:out value="${disciplina.nome }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Horas
							Semanais:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" min="0" max="23"
								step="1" id="horasSemanais" name="horasSemanais"
								placeholder="Horas Semanais"
								value='<c:out value="${disciplina.horasSemanais }"></c:out>'>
						</div>
						<label for="horarioInicio" class="form-label col-md-1">Horário
							de Início:</label>
						<div class="col-md-3">
							<input class="form-control" type="time" id="horarioInicio"
								name="horarioInicio" placeholder="Horário de Início"
								value='<c:out value="${disciplina.horarioInicio }"></c:out>'>
						</div>
						<label for="data" class="form-label col-md-1">Semestre:</label>
						<div class="col-md-3">
							<input class="form-control" type="number" min="0" max="23"
								step="1" id="semestre" name="semestre" placeholder="Semestre"
								value='<c:out value="${disciplina.semestre }"></c:out>'>
						</div>
						<label for="diaSemana" class="form-label col-md-1">Dia da
							Semana:</label>
						<div class="col-md-3">
							<select class="form-select" id="diaSemana" name="diaSemana">
								<option value="">Escolha um Dia da Semana</option>
								<option value="Segunda-feira"
									<c:if test="${disciplina.diaSemana eq 'Segunda-feira'}">selected</c:if>>Segunda-feira</option>
								<option value="Terca-feira"
									<c:if test="${disciplina.diaSemana eq 'Terca-feira'}">selected</c:if>>Terca-feira</option>
								<option value="Quarta-feira"
									<c:if test="${disciplina.diaSemana eq 'Quarta-feira'}">selected</c:if>>Quarta-feira</option>
								<option value="Quinta-feira"
									<c:if test="${disciplina.diaSemana eq 'Quinta-feira'}">selected</c:if>>Quinta-feira</option>
								<option value="Sexta-feira"
									<c:if test="${disciplina.diaSemana eq 'Sexta-feira'}">selected</c:if>>Sexta-feira</option>
								<option value="Sabado"
									<c:if test="${disciplina.diaSemana eq 'Sabado'}">selected</c:if>>Sabado</option>
							</select>
						</div>
						<label for="data" class="form-label col-md-1">Professor:</label>
						<div class="col-md-3">
							<select class="form-select" id="professor" name="professor">
								<option value="0">Escolha um professor</option>
								<c:forEach var="p" items="${professores }">
									<c:if
										test="${(empty disciplina) || (p.codigo ne disciplina.professor.codigo) }">
										<option value="${p.codigo }">
											<c:out value="${p }" />
										</option>
									</c:if>
									<c:if test="${p.codigo eq disciplina.professor.codigo }">
										<option value="${p.codigo }" selected="selected"><c:out
												value="${p }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<label for="data" class="form-label col-md-1">Curso:</label>
						<div class="col-md-3">
							<select class="form-select" id="curso" name="curso">
								<option value="0">Escolha um Curso</option>
								<c:forEach var="c" items="${cursos }">
									<c:if
										test="${(empty disciplina) || (c.codigo ne disciplina.curso.codigo) }">
										<option value="${c.codigo }">
											<c:out value="${c }" />
										</option>
									</c:if>
									<c:if test="${c.codigo eq disciplina.curso.codigo }">
										<option value="${c.codigo }" selected="selected"><c:out
												value="${c }" /></option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-3"></div>
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
		<c:if test="${not empty disciplinas}">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="9"
							style="text-align: center; font-size: 23px;">Lista de
							Disciplinas</th>
					</tr>
					<tr>
						<th>Selecionar</th>
						<th>Código</th>
						<th>Nome</th>
						<th>Hora Semanais</th>
						<th>Hora Início</th>
						<th>Semestre</th>
						<th>Dia Semana</th>
						<th>Professor</th>
						<th>Curso</th>
				
					</tr>
				</thead>
				<tbody class="table-group-divider">
					<c:forEach var="d" items="${disciplinas}">
						<tr>
							<td><input type="radio" name="opcao" value="${d.codigo}"
								onclick="editarDisciplina(this.value)"
								${d.codigo eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${d.codigo}" /></td>
							<td><c:out value="${d.nome}" /></td>
							<td><c:out value="${d.horasSemanais}" /></td>
							<td><c:out value="${d.horarioInicio}" /></td>
							<td><c:out value="${d.semestre}" /></td>
							<td><c:out value="${d.diaSemana}" /></td>
							<td><c:out value="${d.professor.nome}" /></td>
							<td><c:out value="${d.curso.nome}" /></td>
						

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>
</body>
</html>
