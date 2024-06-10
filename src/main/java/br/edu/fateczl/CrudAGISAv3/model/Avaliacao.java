package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
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
@Table(name = "avaliacao")

@NamedNativeQuery(name = "Avaliacao.findByDisciplina", query = "SELECT * FROM fn_avaliacao(?1)", resultClass = Avaliacao.class)

public class Avaliacao {
	@Id
	@Column(name = "codigo",  nullable = true)
	private int codigo;
	
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoDisciplina", nullable = false)
	private Disciplina disciplina;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "peso", nullable = false)
	private double peso;
}
