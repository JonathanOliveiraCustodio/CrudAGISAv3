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
@Table (name ="conteudo")
@Inheritance(strategy = InheritanceType.JOINED)

@NamedNativeQuery(name = "Conteudo.findAll", query = "SELECT * FROM v_listar_conteudos", resultClass = Conteudo.class)

@NamedStoredProcedureQuery(name = "Conteudo.sp_iud_conteudo", procedureName = "sp_iud_conteudo ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "descricao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),	
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

public class Conteudo {
	
	@Id
	@Column(name = "codigo", nullable = false)
	private int codigo;			
	
	@Column(name = "nome", length =100, nullable = false)
	private String nome;	
	
	@Column(name = "descricao", length =100, nullable = false)
	private String descricao;		
	
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoDisciplina", nullable = false)	
	private Disciplina disciplina;
	
	@Override
	public String toString() {
		return nome;
	}

}



