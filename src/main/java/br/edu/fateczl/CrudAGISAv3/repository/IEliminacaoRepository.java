package br.edu.fateczl.CrudAGISAv3.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import br.edu.fateczl.CrudAGISAv3.model.Eliminacao;



public interface IEliminacaoRepository extends JpaRepository<Eliminacao, Integer> {
	
//	@Query(name = "Eliminacao.findAll", nativeQuery = true)
//    List<Eliminacao> findAllEliminacoes();
//	
//	@Procedure(name = "Aluno.sp_iud_aluno")
//	String sp_iud_aluno(
//			@Param("acao") String acao,
//			@Param("CPF") String CPF,
//			@Param("RA") int RA,
//			@Param("anoIngresso") int anoIngresso,
//			@Param("dataConclusao2Grau") Date dataConclusao2Grau,
//			@Param("dataNascimento") Date dataNascimento,
//			@Param("emailCorporativo") String emailCorporativo,
//			@Param("emailPessoal") String emailPessoal,
//			@Param("instituicaoConclusao2Grau") String instituicaoConclusao2Grau,
//			@Param("nome") String nome,
//			@Param("nomeSocial") String nomeSocial,
//			@Param("pontuacaoVestibular") float pontuacaoVestibular,
//			@Param("posicaoVestibular") int posicaoVestibular,
//			@Param("semestreAnoLimiteGraduacao") Date semestreAnoLimiteGraduacao,
//			@Param("semestreIngresso") int semestreIngresso,
//			@Param("telefoneContato") String telefoneContato,
//			@Param("curso") int curso
//		);

}