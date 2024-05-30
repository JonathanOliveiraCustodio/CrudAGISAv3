package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Eliminacao;



public interface ISolicitarEliminacaoRepository extends JpaRepository<Eliminacao, Integer> {
	
	@Query(name = "Eliminacao.findAllDisciplinasRA", nativeQuery = true)
    List<Disciplina> findAllDisciplinasRA(int RA);
	
	@Query(name = "Eliminacao.findAllEliminacaoRA", nativeQuery = true)
    List<Eliminacao> findAllEliminacoesRA(int RA);
	
	
	@Procedure(name = "Eliminacao.sp_inserir_eliminacao")
	String sp_inserir_eliminacao(
			@Param("codigoDisciplina") int codigoDisciplina,
			@Param("codigoMatricula") int codigoMatricula,
			@Param("nomeInstituicao") String nomeInstituicao
		);

}