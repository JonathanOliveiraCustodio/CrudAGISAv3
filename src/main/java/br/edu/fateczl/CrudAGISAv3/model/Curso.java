package br.edu.fateczl.CrudAGISAv3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table (name ="curso")

@NamedNativeQuery(name = "Curso.findAll", query = "SELECT * FROM v_listar_cursos", resultClass = Curso.class)
@NamedNativeQuery(name = "Curso.findConsultaPeriodoMatricula", query = "SELECT * FROM v_periodoMatricula", resultClass = Curso.class)

@NamedStoredProcedureQuery(name = "Curso.sp_iud_curso", procedureName = "sp_iud_curso ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "cargaHoraria", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "sigla", type = String.class),	
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "ultimaNotaENADE", type = Float.class),	
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "turno", type = String.class),	
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

public class Curso {
	
	@Id
	@Column(name = "codigo", nullable = false)	
	private int codigo;
	
	@Column(name = "nome", length = 100,  nullable = false)
	private String nome;
	
	@Column(name = "cargaHoraria",  nullable = false)
	private int cargaHoraria;	
	
	@Column(name = "sigla",  nullable = false)
	private String sigla;
	
	@Column(name = "ultimaNotaENADE",  nullable = false)
	private float ultimaNotaENADE;
	
	@Column(name = "turno", length = 20,  nullable = false)
	private String turno;
	  
    @Override
	public String toString() {
		return nome + " " + turno;
	}

}
