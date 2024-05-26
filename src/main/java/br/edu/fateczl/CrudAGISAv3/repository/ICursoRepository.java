package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Curso;


public interface ICursoRepository extends JpaRepository<Curso, Integer> {
		
	@Query(name = "Curso.findAll", nativeQuery = true)
    List<Curso> findAllCursos();
	
	@Procedure(name = "Curso.sp_iud_curso")
	String sp_iud_curso(
			@Param("acao") String acao,
		    @Param("codigo") int codigo,
		    @Param("nome") String nomeProduto,
		    @Param("cargaHoraria") int cargaHoraria,
		    @Param("sigla") String sigla,
		    @Param("ultimaNotaENADE") float ultimaNotaENADE,	    
		    @Param("turno") String turno
		);

}