package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import br.edu.fateczl.CrudAGISAv3.model.Conteudo;


public interface IConteudoRepository extends JpaRepository<Conteudo, Integer> {
		
	@Query(name = "Conteudo.findAll", nativeQuery = true)
    List<Conteudo> findAllConteudos();
	
	@Procedure(name = "Conteudo.sp_iud_conteudo")
	String sp_iud_conteudo(
			@Param("acao") String acao,
		    @Param("codigo") int codigo,
		    @Param("descricao") String descricao,
		    @Param("nome") String nome,
		    @Param("codigoDisciplina") int codigoDisciplina
		);

}