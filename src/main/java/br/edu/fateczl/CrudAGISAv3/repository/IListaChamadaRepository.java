package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.ListaChamada;


public interface IListaChamadaRepository extends JpaRepository<ListaChamada, Integer> {
	
	@Query(name = "ListaChamada.findListarDatas", nativeQuery = true)
	List<ListaChamada> findListarDatas(int codigoDisciplina);
	
	@Query(name = "ListaChamada.findConsultarListaChamada", nativeQuery = true)
	List<ListaChamada> findConsultarListaChamada(int codigoDisciplina, Date dataChamada);
	
	@Query(name = "ListaChamada.findlistarDisciplinaProfessor", nativeQuery = true)
	List<Disciplina> findlistarDisciplinaProfessor(int codigoProfessor);
	
	@Query(name = "ListaChamada.findAllListaChamada", nativeQuery = true)
    List<ListaChamada> findAllListaChamada();
	
	@Query(name = "ListaChamada.findConstruirCorpo", nativeQuery = true)
    List<ListaChamada> findConstruirCorpo(String CPF);
	
	@Procedure(name = "ListaChamada.sp_iud_listaChamada")
	String sp_iud_listaChamada(
			@Param("acao") String acao,
		    @Param("codigo") int codigo,
		    @Param("codigoDisciplina") int codigoDisciplina,
		    @Param("codigoMatricula") int codigoMatricula,
		    @Param("dataChamada") Date dataChamada,
		    @Param("presenca1") int presenca1,
		    @Param("presenca2") int presenca2,
		    @Param("presenca3") int presenca3,
		    @Param("presenca4") int presenca4

		    
		);
	
	@Procedure(name = "ListaChamada.sp_nova_chamada")
	String sp_nova_chamada(
			@Param("codigoDisciplina") int disciplina		    
		);
		

}