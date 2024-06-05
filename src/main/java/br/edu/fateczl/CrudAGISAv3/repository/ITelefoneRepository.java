package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Telefone;
import br.edu.fateczl.CrudAGISAv3.model.TelefoneId;

public interface ITelefoneRepository extends JpaRepository<Telefone, TelefoneId> {
	
	@Query(name = "Telefone.findAll", nativeQuery = true)
    List<Telefone> findAllTelefones(String cpfAluno);
		
	@Procedure(name = "Telefone.sp_iud_telefone")
	String sp_iud_telefone(
			@Param("acao") String acao,
			@Param("aluno") String aluno,
			@Param("numero") String numero,
			@Param("tipo") String tipo
		);
	
	
	



}
