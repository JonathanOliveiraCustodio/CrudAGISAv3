package br.edu.fateczl.CrudAGISAv3.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;


@Controller
public class HorariosController {
	
	
	@Autowired
	IMatriculaRepository mRep;
	
	@Autowired
	IAlunoRepository aRep;
	
	@Autowired
	IDisciplinaRepository dRep;

	@RequestMapping(name = "horarios", value = "/horarios", method = RequestMethod.GET)
	public ModelAndView horariosGet(ModelMap model) {
		return new ModelAndView("horarios");
	}

	@RequestMapping(name = "horarios", value = "/horarios", method = RequestMethod.POST)
	public ModelAndView horariosPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		
		String RA = allRequestParam.get("RA");
		Aluno a = new Aluno();
		Matricula m = new Matricula();
		String erro = null;
		String saida = null;
		Map<String, List<Disciplina>> disciplinasMatriculadas = new HashMap<String, List<Disciplina>>();
		
		try {
			a.setRA(Integer.parseInt(RA));
			a = buscarAluno(a);
			if (a == null) {
				saida = "Nenhum aluno encontrado com o RA especificado.";
				a = null;
			} else {
				m = buscarMatriculaAtual(a);
				disciplinasMatriculadas = buscarDisciplinasAluno(m);				
			}
		} catch (Exception e) {
			erro = e.getMessage();
		}
		
		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("aluno", a);
		model.addAttribute("matricula", m);
		model.addAttribute("disciplinas", disciplinasMatriculadas);
		
		return new ModelAndView("horarios");
		
	}
	
	private Map<String, List<Disciplina>> buscarDisciplinasAluno(Matricula m) throws ClassNotFoundException, SQLException {
		List<Disciplina> disciplinas = new ArrayList<>();
		Map<String, List<Disciplina>> disciplinasPorSemana = new HashMap<String, List<Disciplina>>();
		
	 	int matricula = m.getCodigo();
	 	
		disciplinas = dRep.findListarDisciplinasCursadas(matricula);
		for(Disciplina d : disciplinas){
		    List<Disciplina> temp = disciplinasPorSemana.get(d.getDiaSemana());

		    if(temp == null){
		        temp = new ArrayList<Disciplina>();
		        disciplinasPorSemana.put(d.getDiaSemana(), temp);
		    }
		    temp.add(d);
		}
		return disciplinasPorSemana;
	}


	private Matricula buscarMatriculaAtual(Aluno a) throws ClassNotFoundException, SQLException {
		Matricula m = new Matricula();
		m = mRep.findBuscarMatriculaAtualAluno(a.getRA());
		return m;
	}


	private Aluno buscarAluno(Aluno a) throws ClassNotFoundException, SQLException {
		a = aRep.findByRA(a.getRA());
		return a;
	}

}
