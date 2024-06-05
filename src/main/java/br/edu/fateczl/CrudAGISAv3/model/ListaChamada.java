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
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
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

@NamedNativeQuery(name = "ListaChamada.findAllListaChamada", query = "SELECT * FROM vw_lista_chamada", resultClass = ListaChamada.class)
@NamedNativeQuery(name = "ListaChamada.findListarDatas", query = "SELECT * FROM fn_listar_lista_chamada_datas(?1)", resultClass = ListaChamada.class)
@NamedNativeQuery(name = "ListaChamada.findlistarDisciplinaProfessor", query = "SELECT * FROM fn_listar_disciplinas_professor(?1)", resultClass = Disciplina.class)
@NamedNativeQuery(name = "ListaChamada.findConsultarListaChamada", query = "SELECT * FROM fn_Lista_Chamada_Disciplina(?1,?2)", resultClass = ListaChamada.class)
@NamedNativeQuery(name = "ListaChamada.findConstruirCorpo", query = "SELECT * FROM fn_corpo_historico(?1)", resultClass = ListaChamada.class)

@NamedStoredProcedureQuery(name = "ListaChamada.sp_iud_listaChamada", procedureName = "sp_iud_listaChamada ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "acao", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigo", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoMatricula", type = Integer.class),	
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataChamada", type = Date.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "presenca1", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "presenca2", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "presenca3", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "presenca4", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class) })

@NamedStoredProcedureQuery(name = "ListaChamada.sp_nova_chamada", procedureName = "sp_nova_chamada ", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "codigoDisciplina", type = Integer.class)})

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
    private MatriculaDisciplina matriculaDisciplina;
	
	@Transient
	private Aluno aluno;
	
	@Transient
	private Disciplina disciplina;
	
	@Transient
	private Professor professor;
	
	
	public String toString() {
        if (dataChamada != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(dataChamada);
        } else {
            return "null";
        }
    }
}


