package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table (name ="matricula")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedNativeQuery(name = "Matricula.findAllBuscarMatricula", query = "SELECT * FROM fn_buscar_matricula(?1)", resultClass = Matricula.class)


@NamedStoredProcedureQuery(name = "Matricula.sp_iud_disciplina", procedureName = "sp_iud_disciplina ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoCurso", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoProfessor", type = Integer.class),	
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "horasSemanais", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "semestre", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "horarioInicio", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "diaSemana", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),		
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

public class Matricula {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoAluno", nullable = false)	
	private Aluno codigoAluno;
	
	@Column(name = "dataMatricula", nullable = false)
	private Date dataMatricula;
	
	@Column(name = "semestre", nullable = false)
	private int semestre;

}
