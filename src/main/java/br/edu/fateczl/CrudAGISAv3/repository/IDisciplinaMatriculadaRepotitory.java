package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import br.edu.fateczl.CrudAGISAv3.model.MatriculaDisciplina;
import br.edu.fateczl.CrudAGISAv3.model.MatriculaDisciplinaId;

public interface IDisciplinaMatriculadaRepotitory extends JpaRepository<MatriculaDisciplina, MatriculaDisciplinaId> {

	@Query(name = "MatriculaDisciplina.findBuscarCodigoDisciplinasMatriculadas", nativeQuery = true)
    List<Integer> findBuscarCodigoDisciplinasMatriculadas(int codigoMatricula);
	
	
	@Procedure(name = "MatriculaDisciplina.sp_matricular_disciplina")
	String sp_matricular_disciplina(
		    @Param("codigoDisciplina") int codigoDisciplina,
		    @Param("codigoMatricula") int codigoMatricula
		  
		);
}
