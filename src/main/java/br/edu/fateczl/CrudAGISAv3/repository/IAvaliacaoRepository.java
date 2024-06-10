package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.Avaliacao;
import br.edu.fateczl.CrudAGISAv3.model.Telefone;



public interface IAvaliacaoRepository extends JpaRepository<Aluno, String> {
	
	@Query(name = "Avaliacao.findByDisciplina", nativeQuery = true)
	List<Avaliacao> findByDisciplina(int codigo);


}