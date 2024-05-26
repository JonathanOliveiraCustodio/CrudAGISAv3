package br.edu.fateczl.CrudAGISAv3.model;

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
@Table (name ="disciplina")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedNativeQuery(name = "Disciplina.findAll", query = "SELECT * FROM v_listar_disciplinas", resultClass = Disciplina.class)



@NamedStoredProcedureQuery(name = "Disciplina.sp_iud_disciplina", procedureName = "sp_iud_disciplina ", parameters = {
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

public class Disciplina {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;
	
	@Column(name = "nome", length =100, nullable = false)
	private String nome;
	
	@Column(name = "horasSemanais", nullable = false)
	private int horasSemanais;
	
	@Column(name = "horarioInicio", length =10, nullable = false)
	private String horarioInicio;
	
	@Column(name = "semestre", nullable = false)
	private int semestre;
	
	@Column(name = "diaSemana", length =20, nullable = false)
	private String diaSemana;
	
	@ManyToOne(targetEntity = Professor.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoProfessor", nullable = false)	
	private Professor professor;
	
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoCurso", nullable = false)	
	private Curso curso;
	

	@Override
	public String toString() {
		return nome;
	} 

}
