package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;



public interface IAlunoRepository extends JpaRepository<Aluno, String> {
	
	@Query(name = "Aluno.findAll", nativeQuery = true)
    List<Aluno> findAllAlunos();
	
	@Query(name = "Aluno.findByRA", nativeQuery = true)
	Aluno findByRA(int RA);
	
	@Query(name = "Aluno.findconstruirCabecalhoHistorico", nativeQuery = true)
	Aluno findconstruirCabecalhoHistorico(String cpf);
	
	@Procedure(name = "Aluno.sp_iud_aluno")
	String sp_iud_aluno(
			@Param("acao") String acao,
			@Param("CPF") String CPF,
			@Param("RA") int RA,
			@Param("anoIngresso") int anoIngresso,
			@Param("dataConclusao2Grau") Date dataConclusao2Grau,
			@Param("dataNascimento") Date dataNascimento,
			@Param("emailCorporativo") String emailCorporativo,
			@Param("emailPessoal") String emailPessoal,
			@Param("instituicaoConclusao2Grau") String instituicaoConclusao2Grau,
			@Param("nome") String nome,
			@Param("nomeSocial") String nomeSocial,
			@Param("pontuacaoVestibular") float pontuacaoVestibular,
			@Param("posicaoVestibular") int posicaoVestibular,
			@Param("semestreAnoLimiteGraduacao") Date semestreAnoLimiteGraduacao,
			@Param("semestreIngresso") int semestreIngresso,
			@Param("telefoneContato") String telefoneContato,
			@Param("curso") int curso
		);

}