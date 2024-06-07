package br.edu.fateczl.CrudAGISAv3.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.ListaChamada;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IListaChamadaRepository;

@Controller
public class HistoricoController {

	@Autowired
	IAlunoRepository aRep;
	
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
		List<ListaChamada> listaChamadas = new ArrayList<>();
		listaChamadas = lcRep.findConstruirCorpo(a.getCPF());	
		return listaChamadas;
	}

	private Aluno construirCabecalho(Aluno a) throws ClassNotFoundException, SQLException {
		a = aRep.findconstruirCabecalhoHistorico(a.getCPF());
		return a;
	}
	
	

}
