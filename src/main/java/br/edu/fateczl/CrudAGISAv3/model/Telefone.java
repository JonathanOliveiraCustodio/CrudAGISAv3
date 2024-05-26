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
@Table(name = "telefone")

@NamedNativeQuery(name = "Telefone.findAll", query = "SELECT * FROM fn_listar_telefones(?1)", resultClass = Telefone.class)

@NamedNativeQuery(name = "Telefone.fn_consultar_telefone_aluno", query = "SELECT * FROM fn_consultar_telefone_aluno(?1,2?)", resultClass = Telefone.class)

@NamedStoredProcedureQuery(name = "Telefone.sp_iud_telefone", procedureName = "sp_iud_telefone", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "aluno", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "numero", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "tipo", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })


@IdClass(TelefoneId.class)
public class Telefone{
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "aluno", nullable = false)
	private Aluno aluno;
	
	@Id
	@Column(name = "numero", length = 12,  nullable = false)
	private String numero;
	
	@Column(name = "tipo", length = 20,  nullable = false)
	private String tipo;
    

}
