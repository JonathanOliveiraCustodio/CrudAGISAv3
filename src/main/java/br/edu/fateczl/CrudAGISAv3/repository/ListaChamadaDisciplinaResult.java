package br.edu.fateczl.CrudAGISAv3.repository;

import java.sql.Date;

public interface ListaChamadaDisciplinaResult {
    Integer getCodigo();
    Integer getCodigoMatricula();
    Integer getCodigoDisciplina();
    Date getDataChamada();
    Integer getPresenca1();
    Integer getPresenca2();
    Integer getPresenca3();
    Integer getPresenca4();
    String getRA();
    String getCPF();
    String getNome();
    String getNomeDisciplina();
}
