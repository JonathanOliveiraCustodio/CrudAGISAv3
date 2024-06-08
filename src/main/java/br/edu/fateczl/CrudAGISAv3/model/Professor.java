package br.edu.fateczl.CrudAGISAv3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name ="professor")

@NamedNativeQuery(name = "Professor.findAll", query = "SELECT * FROM v_listar_professores", resultClass = Professor.class)

@NamedNativeQuery(name = "Professor.findProfessorDisciplina", query = "SELECT * FROM fn_consultar_professor_disciplina(?1)", resultClass = Professor.class)

@NamedStoredProcedureQuery(name = "Professor.sp_iud_professor", procedureName = "sp_iud_professor ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "titulacao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })
public class Professor {
	
	@Id
	@Column(name = "codigo", nullable = false)
	 private int codigo;
	
	@Column(name = "nome", length = 100,  nullable = false)
	 private String nome;
	
	@Column(name = "titulacao", length = 50,  nullable = false)
	 private String titulacao;
	 
	@Override
	public String toString() {
		return nome;
	}
}
