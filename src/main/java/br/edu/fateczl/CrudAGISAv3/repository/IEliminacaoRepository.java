package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Eliminacao;



public interface IEliminacaoRepository extends JpaRepository<Eliminacao, Integer> {
	
	@Query(name = "Eliminacao.findAllDisciplinasRA", nativeQuery = true)
    List<Disciplina> findAllDisciplinasRA(int RA);
	
	@Query(name = "Eliminacao.findAllEliminacaoRA", nativeQuery = true)
    List<Eliminacao> findAllEliminacoesRA(int RA);
	
	
	@Query(name = "Eliminacao.findAllEliminacao", nativeQuery = true)
    List<Eliminacao> findAllEliminacoes();
	
	
	@Procedure(name = "Eliminacao.sp_inserir_eliminacao")
	String sp_inserir_eliminacao(
			@Param("codigoDisciplina") int codigoDisciplina,
			@Param("codigoMatricula") int codigoMatricula,
			@Param("nomeInstituicao") String nomeInstituicao
		);
	
	@Procedure(name = "Eliminacao.sp_update_eliminacao")
	String sp_update_eliminacao(
			@Param("acao") String acao,
			@Param("codigo") int codigo,
			@Param("codigoDisciplina") int codigoDisciplina,
			@Param("codigoMatricula") int codigoMatricula,
			@Param("dataEliminacao") Date dataEliminacao,
			@Param("nomeInstituicao") String nomeInstituicao,
			@Param("status") String status
		);
	
	
}