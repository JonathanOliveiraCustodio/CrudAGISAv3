package br.edu.fateczl.CrudAGISAv3.model;

import java.io.Serializable;
import java.sql.Date;

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

public class PeriodoMatriculaId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date periodoMatriculaInicio;
	private Date periodoMatriculaFim;
}
