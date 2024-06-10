package br.edu.fateczl.CrudAGISAv3.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matriculaDisciplina")

@NamedNativeQuery(name = "MatriculaDisciplina.findBuscarCodigoDisciplinasMatriculadas", query = "SELECT * FROM matriculaDisciplina WHERE codigoMatricula = ?1", resultClass = MatriculaDisciplina.class)

@NamedNativeQuery(name = "MatriculaDisciplina.findBuscarCodigoMatriculas", query = "SELECT * FROM matriculaDisciplina WHERE codigoDisciplina = ?1 AND situacao='Em Curso'", resultClass = MatriculaDisciplina.class)

@NamedStoredProcedureQuery(name = "matriculaDisciplina.sp_matricular_disciplina", procedureName = "sp_matricular_disciplina", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

@IdClass(MatriculaDisciplinaId.class)
public class MatriculaDisciplina {
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Matricula.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoMatricula", nullable = false)
	private Matricula codigoMatricula;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoDisciplina",  nullable = false)
	private Disciplina codigoDisciplina;
	
	@Column(name = "situacao", length = 20,  nullable = false)
	private String situacao;
	
	@Column(name = "notaFinal",  nullable = false)
	private double notaFinal;
	
	@Column(name = "totalFaltas",  nullable = true)
	private Integer totalFaltas;
	
	@Transient
	private Matricula matricula;
	
	@Transient
	private NotaParcial N1;
	
	@Transient
	private NotaParcial N2;
	
	@Transient
	private NotaParcial N3;

}
