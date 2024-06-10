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
@Table (name ="aluno")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedNativeQuery(name = "Aluno.findAll", query = "SELECT * FROM v_listar_alunos", resultClass = Aluno.class)

@NamedNativeQuery(name = "Aluno.findByRA", query = "SELECT * FROM fn_consultar_aluno_RA(?1)", resultClass = Aluno.class)

@NamedNativeQuery(name = "Aluno.findByCPF", query = "SELECT * FROM aluno WHERE CPF=?1", resultClass = Aluno.class)

@NamedNativeQuery(name = "Aluno.findconstruirCabecalhoHistorico", query = "SELECT * FROM fn_cabecalho_historico(?1)", resultClass = Aluno.class)

@NamedStoredProcedureQuery(name = "Aluno.sp_iud_aluno", procedureName = "sp_iud_aluno ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "CPF", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "RA", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "anoIngresso", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataConclusao2Grau", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataNascimento", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "emailCorporativo", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "emailPessoal", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "instituicaoConclusao2Grau", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nomeSocial", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "pontuacaoVestibular", type = Float.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "posicaoVestibular", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "semestreAnoLimiteGraduacao", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "semestreIngresso", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "telefoneContato", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "curso", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

public class Aluno {

	@Id
	@Column(name = "CPF", length =11, nullable = false)
	private String CPF;
	
	@Column(name = "nome", length =100, nullable = false)
	private String nome;
	
	@Column(name = "nomeSocial", length =100, nullable = false)
	private String nomeSocial;
	
	@Column(name = "dataNascimento", nullable = false)
	private Date dataNascimento;
	
	@Column(name = "telefoneContato", length =20, nullable = false)
	private String telefoneContato;
	
	@Column(name = "emailPessoal", length =100, nullable = false)
	private String emailPessoal;
	
	@Column(name = "emailCorporativo", length =100, nullable = false)
	private String emailCorporativo;
	
	@Column(name = "dataConclusao2Grau", nullable = false)
	private Date dataConclusao2Grau;
	
	@Column(name = "instituicaoConclusao2Grau", length =100, nullable = false)
	private String instituicaoConclusao2Grau;
	
	@Column(name = "pontuacaoVestibular", nullable = false)
	private float pontuacaoVestibular;
	
	@Column(name = "posicaoVestibular", nullable = false)
	private int posicaoVestibular;
	
	@Column(name = "anoIngresso", nullable = false)
	private int anoIngresso;
	
	@Column(name = "semestreIngresso", nullable = false)
	private int semestreIngresso;
	
	@Column(name = "semestreAnoLimiteGraduacao", nullable = false)
	private Date semestreAnoLimiteGraduacao;
	
	@Column(name = "RA", nullable = false)
	private int RA;
	
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "curso", nullable = false)	
	private Curso curso;
		
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula", referencedColumnName = "codigo", insertable = false, updatable = false)
	private Matricula matricula;
	
	
	@Override
	public String toString() {
		return nome;
	}
}
