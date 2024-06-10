package br.edu.fateczl.CrudAGISAv3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.NotaParcial;



public interface INotaRepository extends JpaRepository<Aluno, String> {
	
	@Procedure(name = "NotaParcial.sp_iud_nota")
	void sp_iud_nota(
			@Param("avaliacaoCodigo") int avaliacaoCodigo,
		    @Param("codigoDisciplina") int codigoDisciplina,
		    @Param("codigoMatricula") int codigoMatricula,
		    @Param("nota") Double nota
		);
	
	@Procedure(name = "NotaParcial.sp_buscar_nota_aluno")
	void sp_buscar_nota_aluno(
			@Param("avaliacaoCodigo") int avaliacaoCodigo,
		    @Param("codigoDisciplina") int codigoDisciplina,
		    @Param("codigoMatricula") int codigoMatricula
		);
	
	@Query(name = "NotaParcial.findNota", nativeQuery = true)
	NotaParcial findNota(int avaliacaoCodigo, int codigoDisciplina, int codigoMatricula);
	
}