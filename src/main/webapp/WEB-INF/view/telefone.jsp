<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>Telefones do Aluno</title>
</head>
<body>
	<div>
		<jsp:include page="headerSecretaria.jsp" />
	</div>
	<br />
	<div class="container py-4">
		<div class="p-5 mb-4 bg-body-tertiary rounded-3 text-center shadow">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold">Telefones</h1>
		        <div class="d-flex gap-2 justify-content-center py-2">
					<form action="telefone" method="post" class="row g-3 mt-3">
					
						<input type="hidden" id="aluno" name="aluno" value='<c:out value="${aluno.CPF }"></c:out>'>
							<label for="numero" class="form-label col-md-2">Número:</label>
							<div class="col-md-3">
								<input class="form-control" type="text" id="numero"
									name="numero" placeholder="Número" value='<c:out value="${telefone.numero }"></c:out>'>
							</div>
							<label for="tipo"  class="form-label col-md-2">Tipo:</label>
							<div class="col-md-3">
								<select class="form-select" id="tipo" name="tipo">
										<option value="Residencial">Residencial</option>
										<option value="Comercial">Comercial</option>
										<option value="Celular">Celular</option>
								</select>
							</div>
							<div class="col-md-2 d-grid text-center">
							</div>
							<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Cadastrar" class="btn btn-success">
							</div>
							<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Alterar" class="btn btn-success">
							</div>
							<div class="col-md-2 d-grid text-center">
							</div>
							<div class="col-md-2 d-grid text-center">
							</div>
							<div class="col-md-2 d-grid text-center">
							<input type="submit" id="botao" name="botao"
								value="Excluir" class="btn btn-danger">
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
	<div class="container py-4 text-center d-flex justify-content-center" align="center">
		<c:if test="${not empty telefones}">
			<table class="table table-striped">
				<thead>
					<tr>
						<th class="titulo-tabela" colspan="7"
							style="text-align: center; font-size: 23px;">Lista de
							Telefones</th>
					</tr>
					<tr>
						<th>CPF do Aluno</th>
						<th>Número</th>
						<th>Tipo</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="t" items="${telefones}">
						<tr>
							<td><c:out value="${t.aluno.CPF}" /></td>
							<td><c:out value="${t.numero}" /></td>
							<td><c:out value="${t.tipo}" /></td>
							<td>
								<form action="editar-telefone" method="post">
									<input type="hidden" name="aluno" value="${t.aluno.CPF}">
									<input type="hidden" name="numero" value="${t.numero}">
									<input type="hidden" name="tipo" value="${t.tipo}">
								</form>
							</td>
							<td>
								<form action="telefone" method="post">
									<input type="hidden" name="aluno" value="${t.aluno.CPF}">
									<input type="hidden" name="numero" value="${t.numero}">
									<input type="hidden" name="tipo" value="${t.tipo}"> 
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div>