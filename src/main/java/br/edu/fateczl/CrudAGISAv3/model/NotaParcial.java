package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notaParcial")

@NamedStoredProcedureQuery(name = "NotaParcial.sp_buscar_nota_aluno", procedureName = "sp_buscar_nota_aluno ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "avaliacaoCodigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class) })

@NamedNativeQuery(name = "NotaParcial.findNota", query = "SELECT TOP 1 * FROM notaParcial WHERE avaliacaoCodigo=?1 AND codigoDisciplina=?2 AND codigoMatricula=?3", resultClass = NotaParcial.class)

@IdClass(NotaParcialId.class)
public class NotaParcial {
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Avaliacao.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "avaliacaoCodigo", nullable = false)
	private Avaliacao avaliacaoCodigo;
	
	@Id
	private int codigoMatricula;
	
	@Id
	private int codigoDisciplina;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = MatriculaDisciplina.class, fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "codigoMatricula", referencedColumnName = "codigoMatricula", insertable = false, updatable = false),
        @JoinColumn(name = "codigoDisciplina", referencedColumnName = "codigoDisciplina", insertable = false, updatable = false)
    })
    private MatriculaDisciplina matriculaDisciplina;

	@Column(name = "dataDeLancamento", nullable = false)
	private Date dataDeLancamento;

	@Column(name = "nota", nullable = false)
	private double nota;

}
