package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import br.edu.fateczl.CrudAGISAv3.model.PeriodoMatricula;
import br.edu.fateczl.CrudAGISAv3.model.PeriodoMatriculaId;


public interface IPeriodoMatriculaRepository extends JpaRepository<PeriodoMatricula, PeriodoMatriculaId> {
		
	
	@Query(name = "PeriodoMatricula.findConsultaPeriodoMatricula", nativeQuery = true)
	PeriodoMatricula findConsultaPeriodoMatricula();
	
	@Procedure(name = "PeriodoMatricula.sp_u_periodomatricula")
	String sp_u_periodomatricula(	
		    @Param("periodo_matricula_fim") Date periodoMatriculaFim,
		    @Param("periodo_matricula_inicio") Date periodoMatriculaInicio
			);

}