package br.edu.fateczl.CrudAGISAv3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fateczl.CrudAGISAv3.model.MatriculaDisciplina;
import br.edu.fateczl.CrudAGISAv3.model.MatriculaDisciplinaId;

public interface IDisciplinasMatriculadasRepotitory extends JpaRepository<MatriculaDisciplina, MatriculaDisciplinaId> {

}
