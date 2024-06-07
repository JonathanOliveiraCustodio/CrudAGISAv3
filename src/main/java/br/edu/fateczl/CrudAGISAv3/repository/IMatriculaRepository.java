package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.model.Telefone;


public interface IMatriculaRepository extends JpaRepository<Matricula, Integer> {
	
		
	@Query(name = "Matricula.findBuscarMatriculaAtual", nativeQuery = true)
	Matricula findBuscarMatriculaAtualAluno(int codigo);
	
	@Query(name = "Matricula.findAllBuscarMatricula", nativeQuery = true)
	Matricula findAllBuscarMatricula(int RA);
		
	@Query(name = "Telefone.findAll", nativeQuery = true)
    List<Telefone> findAllTelefones(String cpfAluno);
	
	
	@Query(name = "Matricula.findAllBuscarMatricula", nativeQuery = true)
	Matricula findBuscarMatriculaAluno(Aluno a, Date dataMatriculaInicio, Date dataMatriculaFim);

	@Procedure(name = "MatriculaDisciplina.sp_nova_matricula")
	String sp_nova_matricula(
		    @Param("codigoAluno") String codigoAluno	  
		);
		

}