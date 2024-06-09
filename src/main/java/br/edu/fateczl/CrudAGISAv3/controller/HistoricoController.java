package br.edu.fateczl.CrudAGISAv3.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.ListaChamada;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IListaChamadaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IProfessorRepository;


@Controller
public class HistoricoController {
	
	


	@Autowired
	IAlunoRepository aRep;
	
	@Autowired
	IDisciplinaRepository dRep;
	
	@Autowired
	IProfessorRepository pRep;
	
	@Autowired
	IListaChamadaRepository lcRep;
	
	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.GET)
	public ModelAndView historicoGet(ModelMap model) {
		return new ModelAndView("historico");
	}



	
	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.POST)
	public ModelAndView historicoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cpf = allRequestParam.get("CPF");
		String erro = null;
		
		Matricula m = new Matricula();
		Professor p = new Professor();
		Aluno a = new Aluno();
		List<ListaChamada> listaChamadas = new ArrayList<>();
		a.setCPF(cpf);
		try {
			a = construirCabecalho(a);
			if (a == null) {
				erro = "Nenhum aluno encontrado com o CPF especificado.";
				a = null;
				listaChamadas = null;
			} else {
				
				listaChamadas = construirCorpo(a);
			}
		} catch (Exception e) {
			erro = e.getMessage();
		}
		model.addAttribute("erro", erro);
		model.addAttribute("aluno", a);
		model.addAttribute("matricula", m);
		model.addAttribute("professor", p);
		model.addAttribute("listaChamadas", listaChamadas);
		return new ModelAndView("historico");
	}

	private List<ListaChamada> construirCorpo(Aluno a) throws ClassNotFoundException, SQLException {
	    List<ListaChamada> listaChamadas = lcRep.findConstruirCorpo(a.getCPF());
	    
	    for (ListaChamada chamada : listaChamadas) {
	        Disciplina disciplina = chamada.getDisciplina();
	        if (disciplina != null) {
	            Professor professor = disciplina.getProfessor();
	            if (professor != null) {
	                chamada.setNomeProfessor(professor.getNome());
	            }
	        }
	    }
	    
	    return listaChamadas;
	}

	private Aluno construirCabecalho(Aluno a) throws ClassNotFoundException, SQLException {
		a = aRep.findconstruirCabecalhoHistorico(a.getCPF());
		return a;
	}

	
	

}
