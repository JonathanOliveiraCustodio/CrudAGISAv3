package br.edu.fateczl.CrudAGISAv3.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import br.edu.fateczl.CrudAGISAv3.model.PeriodoMatricula;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IPeriodoMatriculaRepository;

@Controller
public class MatriculaController {
	
	@Autowired
	IDisciplinaRepository dRep;
	
	@Autowired
	IMatriculaRepository mRep;
	
	
	@Autowired
	IAlunoRepository aRep;
		
	@Autowired
	IPeriodoMatriculaRepository pmRep;
	
	@RequestMapping(name = "matricula", value = "/matricula", method = RequestMethod.GET)
	public ModelAndView matriculaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		
		String erro = "";
		boolean periodoValido = false;
		try {
			periodoValido = estaNoPeriodoDeMatricula();
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();

		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("periodoValido", periodoValido);

			}
		
		return new ModelAndView("matricula");
	}

	@RequestMapping(name = "matricula", value = "/matricula", method = RequestMethod.POST)
	public ModelAndView matriculaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		
		String cmd =  allRequestParam.get("botao");
		String RA =  allRequestParam.get("RA");
		boolean periodoValido = false;

		// saida
		String saida = "";
		String erro = "";
		Aluno a = new Aluno();
		Matricula m = new Matricula();
		Map<String, List<Disciplina>> disciplinasPorSemana = new HashMap<String, List<Disciplina>>();
		List<Integer> codigosDisciplinasMatriculadas = new ArrayList<>();

		try {
			periodoValido = estaNoPeriodoDeMatricula();
			a.setRA(Integer.parseInt(RA));
			if (cmd.contains("Buscar")) {
				a = buscarAluno(a);
				if (a == null) {
					saida = "Nenhum aluno encontrado com o RA especificado.";
					a = null;
				} else {
					disciplinasPorSemana = disciplinasDisponiveis(a);
					m = buscarMatriculaAtual(a);

					if (m.getCodigo() == 0) {
						m = novaMatricula(a);
					} else {
						codigosDisciplinasMatriculadas = buscarDisciplinasMatriculadasCodigos(m);
					}

				}
			} else {
				a = buscarAluno(a);
				m = buscarMatriculaAtual(a);
				for (String key : allRequestParam.keySet()) {
					if (key.contains("disciplinaCheckbox")) {
		                String c = allRequestParam.get(key);
						Disciplina d = new Disciplina();
						d.setCodigo(Integer.parseInt(c));
						d = buscarDisciplina(d);
						saida = realizarMatriculaDisciplina(d, m);
		            }
		        }
				if (erro != "") {
					saida = "";
				}
				a = null;
				m = null;
			}

		} catch (Exception e) {
			erro = e.getMessage();
		}

		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("aluno", a);
		model.addAttribute("matricula", m);
		model.addAttribute("periodoValido", periodoValido);
		model.addAttribute("disciplinas", disciplinasPorSemana);
		model.addAttribute("disciplinasMatriculadas", codigosDisciplinasMatriculadas);
		return new ModelAndView("matricula");
	}
	
	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
		} else {
			return null;
		}
	}

	private Disciplina buscarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		Optional<Disciplina> disciplinaOptional = dRep.findById(d.getCodigo());
		if (disciplinaOptional.isPresent()) {
			return disciplinaOptional.get();
		} else {
			return null;
		}
	}

	private String realizarMatriculaDisciplina(Disciplina d, Matricula m) throws SQLException, ClassNotFoundException {
//
		String saida = null;//dmRep.matricularDisciplina(d, m);
		return saida;
	}

	private boolean estaNoPeriodoDeMatricula() throws ClassNotFoundException, SQLException {
	    PeriodoMatricula p = new PeriodoMatricula();
	    long millisAtual = System.currentTimeMillis();
	    Date dataAtual = new Date(millisAtual);
	    boolean periodoValido = false;
	    p = pmRep.findConsultaPeriodoMatricula();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(p.getPeriodoMatriculaFim());
	    calendar.add(Calendar.DAY_OF_MONTH, 1);
	    Date periodoMatriculaFimPlusOne = new Date(calendar.getTimeInMillis());
	    periodoValido = (!p.getPeriodoMatriculaInicio().after(dataAtual) && !periodoMatriculaFimPlusOne.before(dataAtual));
	    return periodoValido;
	}

	private Matricula buscarMatriculaAtual(Aluno a) throws ClassNotFoundException, SQLException {
		PeriodoMatricula p = new PeriodoMatricula();
		p = pmRep.findConsultaPeriodoMatricula();
		Matricula m = new Matricula();
	//	m = mRep.buscarMatriculaAluno(a, c.getPeriodoMatriculaInicio(), c.getPeriodoMatriculaFim());
		return m;
	}

	private List<Integer> buscarDisciplinasMatriculadasCodigos(Matricula m) throws ClassNotFoundException, SQLException {
	//	List<Integer> codigos = dmRep.buscarCodigoDisciplinasMatriculadas(m.getCodigo());
	//	return codigos;
		return null;
	}

	private Matricula novaMatricula(Aluno a) throws ClassNotFoundException, SQLException {
	//	mRep.novaMatricula(a);
		Matricula m = buscarMatriculaAtual(a);
		return m;
	}

	private Map<String, List<Disciplina>> disciplinasDisponiveis(Aluno a) throws ClassNotFoundException, SQLException {
		List<Disciplina> disciplinas = new ArrayList<>();
		Map<String, List<Disciplina>> disciplinasPorSemana = new HashMap<String, List<Disciplina>>();
		
		//disciplinas = dRep.listarParaMatricula(a);
		for (Disciplina d : disciplinas) {
			List<Disciplina> temp = disciplinasPorSemana.get(d.getDiaSemana());
			if (temp == null) {
				temp = new ArrayList<Disciplina>();
				disciplinasPorSemana.put(d.getDiaSemana(), temp);
			}
			temp.add(d);
		}
		return disciplinasPorSemana;
	}
	
	

}
