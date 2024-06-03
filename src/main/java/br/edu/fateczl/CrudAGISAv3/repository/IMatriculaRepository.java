package br.edu.fateczl.CrudAGISAv3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.model.Telefone;


public interface IMatriculaRepository extends JpaRepository<Matricula, Integer> {
	
	@Query(name = "Matricula.findAllBuscarMatricula", nativeQuery = true)
	Matricula findAllBuscarMatricula(int RA);
		
	@Query(name = "Telefone.findAll", nativeQuery = true)
    List<Telefone> findAllTelefones(String cpfAluno);
		

}