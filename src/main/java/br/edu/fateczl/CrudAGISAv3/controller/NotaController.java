package br.edu.fateczl.CrudAGISAv3.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
import br.edu.fateczl.CrudAGISAv3.model.Avaliacao;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.ListaChamada;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.model.MatriculaDisciplina;
import br.edu.fateczl.CrudAGISAv3.model.NotaParcial;
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IAvaliacaoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaMatriculadaRepotitory;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IListaChamadaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.INotaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IProfessorRepository;

@Controller
public class NotaController {

	@Autowired
	IDisciplinaRepository dRep;

	@Autowired
	INotaRepository inRep;

	@Autowired
	IProfessorRepository pRep;
	
	@Autowired
	IMatriculaRepository mRep;
	
	@Autowired
	IDisciplinaMatriculadaRepotitory mDRep;
	
	@Autowired
	IAlunoRepository aRep;
	
	@Autowired
	IListaChamadaRepository lcRep;
	
	@Autowired
	IAvaliacaoRepository avRep;

	@RequestMapping(name = "nota", value = "/nota", method = RequestMethod.GET)
	public ModelAndView chamadaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Professor> professores = new ArrayList<>();

		try {
			//String cmd = allRequestParam.get("cmd");
			//String codigo = allRequestParam.get("codigo");
			professores = listaProfessores();
		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("professores", professores);
	
		}

		return new ModelAndView("nota");
	}

	@RequestMapping(name = "nota", value = "/nota", method = RequestMethod.POST)
	public ModelAndView listaChamdaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String professor = allRequestParam.get("professor");
		String disciplina = allRequestParam.get("disciplina");
		String saida = "";
		String erro = "";

		Professor p = new Professor();
		Disciplina d = new Disciplina();
		Aluno a = new Aluno();

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Professor> professores = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		List<MatriculaDisciplina> matriculas = new ArrayList<>();

		try {
			p.setCodigo(Integer.parseInt(professor));
			p = buscarProfessor(p);
			professores = listaProfessores();
			disciplinas = listarDisciplinas(Integer.parseInt(professor));
			if (!disciplina.equals("0")) {
				d.setCodigo(Integer.parseInt(disciplina));
				d = buscarDisciplina(d);
			}
			if (cmd != null) {
				if (cmd.contains("Consultar")) {
					avaliacoes = consultarAvaliacao(d.getCodigo());
					matriculas = consultarMatricula(d.getCodigo(), avaliacoes);
				} else if(cmd.contains("Alterar")) {
					//Removido. Quebrou ao mover para main.
				}

			}

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
			e.printStackTrace();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("disciplina", d);
			model.addAttribute("aluno", a);
			model.addAttribute("professores", professores);
			model.addAttribute("professor", p);
			model.addAttribute("disciplina", d);
			model.addAttribute("avaliacoes", avaliacoes);
			model.addAttribute("matriculas", matriculas);
		}
		return new ModelAndView("nota");
	}

	private Professor buscarProfessor(Professor p) throws SQLException, ClassNotFoundException {
		Optional<Professor> professorOptional = pRep.findById(p.getCodigo());
		if (professorOptional.isPresent()) {
			return professorOptional.get();
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


	private List<Disciplina> listarDisciplinas(int codigoProfessor) throws ClassNotFoundException, SQLException {
		List<Disciplina> disciplinas = lcRep.findlistarDisciplinaProfessor(codigoProfessor);
		return disciplinas;
	}

	private List<Professor> listaProfessores() throws SQLException, ClassNotFoundException {
		List<Professor> professores = pRep.findAllProfessores();
		return professores;
	}
	
	private List<Avaliacao> consultarAvaliacao(int codigoDisciplina)
	        throws ClassNotFoundException, SQLException {
	    List<Avaliacao> avaliacoes = avRep.findByDisciplina(codigoDisciplina);
	    return avaliacoes;
	}
	
	private List<MatriculaDisciplina> consultarMatricula(int codigoDisciplina, List<Avaliacao> avaliacoes)
	        throws ClassNotFoundException, SQLException {
	    List<MatriculaDisciplina> matriculas = mDRep.findBuscarCodigoMatriculas(codigoDisciplina);
	    for (MatriculaDisciplina md : matriculas) {
	            Matricula matricula = mRep.findMatriculaPorCodigo(md.getCodigoMatricula().getCodigo());
	            Aluno aluno = aRep.findByCPF(matricula.getCodigoAluno().getCPF());
	            matricula.setAluno(aluno);
	            md.setMatricula(matricula);
	            inRep.sp_buscar_nota_aluno(avaliacoes.get(0).getCodigo(), md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo());
	            inRep.sp_buscar_nota_aluno(avaliacoes.get(1).getCodigo(), md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo());
	            inRep.sp_buscar_nota_aluno(avaliacoes.get(2).getCodigo(), md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo());
	            
	            md.setN1(inRep.findNota(avaliacoes.get(0).getCodigo(),  md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo()));
	            md.setN2(inRep.findNota(avaliacoes.get(1).getCodigo(),  md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo()));
	            md.setN3(inRep.findNota(avaliacoes.get(2).getCodigo(),  md.getCodigoDisciplina().getCodigo(), md.getCodigoMatricula().getCodigo()));
	        }
		return matriculas;
	    }
	}






