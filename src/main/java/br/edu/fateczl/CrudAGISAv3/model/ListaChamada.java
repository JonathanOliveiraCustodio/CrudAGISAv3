package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;
import java.text.SimpleDateFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listaChamada")

public class ListaChamada {
	
	@Id
    @Column(name = "codigo", nullable = false)
	private int codigo;	
	
	@Column(name = "dataChamada", nullable = false)
	private Date dataChamada;	
	
	@Column(name = "presenca1", nullable = false)
	private int presenca1;
	
	@Column(name = "presenca2", nullable = false)
	private int presenca2;
	
	@Column(name = "presenca3", nullable = false)
	private int presenca3;
	
	@Column(name = "presenca4", nullable = false)
	private int presenca4;
	
	@Column(name = "codigoMatricula", nullable = false)
	private int codigoMatricula;
	
	@Column(name = "codigoDisciplina", nullable = false)
	private int codigoDisciplina;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "codigoMatricula", referencedColumnName = "codigoMatricula", insertable = false, updatable = false),
        @JoinColumn(name = "codigoDisciplina", referencedColumnName = "codigoDisciplina", insertable = false, updatable = false)
    })
    private MatriculaDisciplina matriculaDisciplina1;
	
	@Transient
	private Aluno aluno;
	
	@Transient
	private Disciplina disciplina;
	
	@Transient
	private Professor professor;
	
	@Transient
	private MatriculaDisciplina matriculaDisciplina;
	
	public String toString() {
        if (dataChamada != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(dataChamada);
        } else {
            return "null";
        }
    }
}


