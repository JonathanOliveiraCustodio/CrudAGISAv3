package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Disciplina;


public interface IDisciplinaRepository extends JpaRepository<Disciplina, Integer> {
		
	@Query(name = "Disciplina.findAll", nativeQuery = true)
    List<Disciplina> findAllDisciplinas();
	
	//@Query(name = "Disciplina.FindListarParaMatricula", nativeQuery = true)
   // List<Disciplina> FindListarParaMatricula(String codigoAluno, int codigoCurso);
//	/
	
	
	@Procedure(name = "Disciplina.sp_iud_disciplina")
	String sp_iud_disciplina(
			@Param("acao") String acao,
		    @Param("codigo") int codigo,
		    @Param("codigoCurso") int codigoCurso,
		    @Param("codigoProfessor") int codigoProfessor,
		    @Param("horasSemanais") int horasSemanais,
		    @Param("semestre") int semestre,
		    @Param("horarioInicio") String horarioInicio,
		    @Param("diaSemana") String diaSemana,	    
		    @Param("nome") String nome
		);

}