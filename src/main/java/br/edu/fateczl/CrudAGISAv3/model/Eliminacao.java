package br.edu.fateczl.CrudAGISAv3.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eliminacoes")

public class Eliminacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private int codigo;

    @Column(name = "codigoMatricula", nullable = false)
    private int codigoMatricula;

    @Column(name = "codigoDisciplina", nullable = false)
    private int codigoDisciplina;

    @Column(name = "dataEliminacao", nullable = false)
    private Date dataEliminacao;

    @Column(name = "status", length = 30, nullable = false)
    private String status = "Em análise";

    @Column(name = "nomeInstituicao", length = 255, nullable = false)
    private String nomeInstituicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "codigoMatricula", referencedColumnName = "codigoMatricula", insertable = false, updatable = false),
        @JoinColumn(name = "codigoDisciplina", referencedColumnName = "codigoDisciplina", insertable = false, updatable = false)
    })
    private MatriculaDisciplina matriculaDisciplina;


    @Transient
    private Aluno aluno;

    @Transient
    private Curso curso;

    @Transient
    private Disciplina disciplina;

}