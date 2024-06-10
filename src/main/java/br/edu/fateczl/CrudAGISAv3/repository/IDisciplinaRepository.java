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
	
	@Query(name = "Disciplina.findListarParaMatricula", nativeQuery = true)
    List<Disciplina> findListarParaMatricula(int codigoCurso,String codigoAluno);
   
	@Query(name = "Disciplina.findListarDisciplinasCursadas", nativeQuery = true)
    List<Disciplina> findListarDisciplinasCursadas(int codigo);
	
	@Query(name = "Disciplina.findDisciplinasParametro", nativeQuery = true)
    List<Disciplina> findDisciplinasParametro(String tipoPesquisa, String valorPesquisa);
	
//	@Query("SELECT d FROM Disciplina d WHERE d.nome LIKE %:nome%")
//    List<Disciplina> findDisciplinasByNome(String nome);
//
//    @Query("SELECT d FROM Disciplina d WHERE d.diaSemana LIKE %:diaSemana%")
//    List<Disciplina> findDisciplinasByDiaSemana(String diaSemana);
//
//    @Query("SELECT d FROM Disciplina d WHERE d.curso.nome LIKE %:curso%")
//    List<Disciplina> findDisciplinasByCurso(String curso);
//
//    @Query("SELECT d FROM Disciplina d WHERE d.professor.nome LIKE %:professor%")
//    List<Disciplina> findDisciplinasByProfessor(String professor);
	
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

	//List<Disciplina> findListarDisciplinasCursadas(int codigo);

}