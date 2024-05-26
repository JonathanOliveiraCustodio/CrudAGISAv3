package br.edu.fateczl.CrudAGISAv3.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "matriculaDisciplina")

@IdClass(MatriculaDisciplinaId.class)
public class MatriculaDisciplina {
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Matricula.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoMatricula", nullable = false)
	private int codigoMatricula;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoDisciplina",  nullable = false)
	private int codigoDisciplina;
	
	@Column(name = "situacao", length = 20,  nullable = false)
	private String situacao;
	
	@Column(name = "notaFinal",  nullable = false)
	private double notaFinal;
	
	@Column(name = "totalFaltas",  nullable = true)
	private  int totalFaltas;

}
