package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import br.edu.fateczl.CrudAGISAv3.model.Professor;



public interface IProfessorRepository extends JpaRepository<Professor, Integer> {
	
	@Query(name = "Professor.findAll", nativeQuery = true)
    List<Professor> findAllProfessores();
	
	@Procedure(name = "Professor.sp_iud_professor")
	String sp_iud_professor(
			@Param("acao") String acao,
			@Param("codigo") int codigo,
			@Param("nome") String nome,
			@Param("titulacao") String titulacao
		);

}