package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
@Table(name = "eliminacoes")

@NamedNativeQuery(name = "Eliminacao.findAllDisciplinasRA", query = "SELECT * FROM fn_listar_disciplinas_por_ra(?1)", resultClass = Disciplina.class)

@NamedNativeQuery(name = "Eliminacao.findAllEliminacaoRA", query = "SELECT * FROM fn_lista_eliminacoes_por_RA(?1)", resultClass = Eliminacao.class)

@NamedNativeQuery(name = "Eliminacao.findAllEliminacao", query = "SELECT * FROM vw_lista_eliminacoes", resultClass = Eliminacao.class)

@NamedStoredProcedureQuery(name = "Eliminacao.sp_inserir_eliminacao", procedureName = "sp_inserir_eliminacao", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nomeInstituicao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

@NamedStoredProcedureQuery(name = "Eliminacao.sp_update_eliminacao", procedureName = "sp_update_eliminacao", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataEliminacao", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nomeInstituicao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

public class Eliminacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private int codigo;

    @Column(name = "codigoMatricula", nullable = false)
    private int codigoMatricula;

    @Column(name = "codigoDisciplina", nullable = false)
    private int codigoDisciplina;

    @Column(name = "dataEliminacao", nullable = false)
    private Date dataEliminacao;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "Em análise";

    @Column(name = "nomeInstituicao", length = 255, nullable = false)
    private String nomeInstituicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "codigoMatricula", referencedColumnName = "codigoMatricula", insertable = false, updatable = false),
        @JoinColumn(name = "codigoDisciplina", referencedColumnName = "codigoDisciplina", insertable = false, updatable = false)
    })
    private MatriculaDisciplina matriculaDisciplina;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigoAluno", referencedColumnName = "RA")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigoCurso", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigoDisciplina", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Disciplina disciplina;

}