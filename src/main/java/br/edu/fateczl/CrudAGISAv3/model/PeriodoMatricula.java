package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table (name ="periodoMatricula")

@NamedNativeQuery(name = "PeriodoMatricula.findConsultaPeriodoMatricula", query = "SELECT * FROM v_periodoMatricula", resultClass = PeriodoMatricula.class)


@NamedStoredProcedureQuery(name = "PeriodoMatricula.sp_u_periodomatricula", procedureName = "sp_u_periodomatricula ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "periodo_matricula_fim", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "periodo_matricula_inicio", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

@IdClass(PeriodoMatriculaId.class)
public class PeriodoMatricula {
	@Id
	@Column(name = "periodo_matricula_inicio",  nullable = true)
	private Date periodoMatriculaInicio;
	
	@Id
	@Column(name = "periodo_matricula_fim",  nullable = true)
	private Date periodoMatriculaFim;
    
}
