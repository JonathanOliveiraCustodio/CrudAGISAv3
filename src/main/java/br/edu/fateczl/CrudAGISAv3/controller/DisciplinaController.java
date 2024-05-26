package br.edu.fateczl.CrudAGISAv3.controller;

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
import br.edu.fateczl.CrudAGISAv3.model.Curso;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IProfessorRepository;

@Controller
public class DisciplinaController {

	@Autowired
	IDisciplinaRepository dRep;

	@Autowired
	ICursoRepository cRep;

	@Autowired
	IProfessorRepository pRep;

	@RequestMapping(name = "disciplina", value = "/disciplina", method = RequestMethod.GET)
	public ModelAndView disciplinaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Professor> professores = pRep.findAllProfessores();
		List<Curso> cursos = cRep.findAllCursos();

		List<Disciplina> disciplinas = new ArrayList<>();

		Disciplina d = new Disciplina();

		try {
			professores = listarProfessores();
			cursos = listarCursos();

			String cmd = allRequestParam.get("cmd");
			String codigo = allRequestParam.get("codigo");

			if (cmd != null) {
				if (cmd.contains("alterar")) {

					d.setCodigo(Integer.parseInt(codigo));
					d = buscarDisciplina(d);

				} else if (cmd.contains("excluir")) {
					d.setCodigo(Integer.parseInt(codigo));
					saida = excluirDisciplina(d);

				}
				disciplinas = listarDisciplinas();
			}

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("disciplina", d);
			model.addAttribute("professores", professores);
			model.addAttribute("cursos", cursos);
			model.addAttribute("disciplinas", disciplinas);
		}

		return new ModelAndView("disciplina");
	}

	@RequestMapping(name = "disciplina", value = "/disciplina", method = RequestMethod.POST)
	public ModelAndView disciplinaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String nome = allRequestParam.get("nome");
		String horasSemanais = allRequestParam.get("horasSemanais");
		String horarioInicio = allRequestParam.get("horarioInicio");
		String semestre = allRequestParam.get("semestre");
		String diaSemana = allRequestParam.get("diaSemana");
		String professor = allRequestParam.get("professor");
		String curso = allRequestParam.get("curso");

		// saida
		String saida = "";
		String erro = "";

		Disciplina d = new Disciplina();
		Professor p = new Professor();
		Curso c = new Curso();

		List<Professor> professores = new ArrayList<>();
		List<Curso> cursos = new ArrayList<>();
		List<Disciplina> disciplinas = new ArrayList<>();

		try {

			if (!cmd.contains("Listar")) {
				p.setCodigo(Integer.parseInt(professor));
				p = buscarProfessor(p);
				d.setProfessor(p);
				c.setCodigo(Integer.parseInt(curso));
				c = buscarCurso(c);
				d.setCurso(c);
				d.setCodigo(Integer.parseInt(codigo));
			}
			professores = listarProfessores();
			cursos = listarCursos();

			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				d.setNome(nome);
				d.setHorasSemanais(Integer.parseInt(horasSemanais));
				d.setHorarioInicio(horarioInicio);
				d.setSemestre(Integer.parseInt(semestre));
				d.setDiaSemana(diaSemana);
			}

			if (cmd.contains("Cadastrar")) {
				saida = cadastrarDisciplina(d);
				d = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarDisciplina(d);
				d = null;
			}
			if (cmd.contains("Excluir")) {

				saida = excluirDisciplina(d);
				d = null;
			}
			if (cmd.contains("Buscar")) {
				d = buscarDisciplina(d);
				if (d == null) {
					saida = "Nenhuma disciplina encontrado com o código especificado.";
					d = null;
				}
			}
			if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
				d = null;
			}

			if (cmd.contains("Listar")) {
				disciplinas = listarDisciplinas();
			}

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplina", d);
			model.addAttribute("professores", professores);
			model.addAttribute("cursos", cursos);
			model.addAttribute("disciplinas", disciplinas);

		}

		return new ModelAndView("disciplina");
	}

	private String cadastrarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		String saida = dRep.sp_iud_disciplina("I", d.getCodigo(), d.getCurso().getCodigo(),
				d.getProfessor().getCodigo(), d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(),
				d.getDiaSemana(), d.getNome());
		return saida;

	}

	private String alterarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		String saida = dRep.sp_iud_disciplina("U", d.getCodigo(), d.getCurso().getCodigo(),
				d.getProfessor().getCodigo(), d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(),
				d.getDiaSemana(), d.getNome());
		return saida;

	}

	private String excluirDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		String saida = dRep.sp_iud_disciplina("D", d.getCodigo(), d.getCurso().getCodigo(),
				d.getProfessor().getCodigo(), d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(),
				d.getDiaSemana(), d.getNome());
		return saida;

	}

	private Disciplina buscarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		Optional<Disciplina> disciplinaOptional = dRep.findById(d.getCodigo());
		if (disciplinaOptional.isPresent()) {
			return disciplinaOptional.get();
		} else {
			return null;
		}
	}

	private List<Disciplina> listarDisciplinas() throws SQLException, ClassNotFoundException {
		List<Disciplina> disciplinas = dRep.findAllDisciplinas();
		return disciplinas;
	}

	private Professor buscarProfessor(Professor p) throws SQLException, ClassNotFoundException {
		Optional<Professor> professorOptional = pRep.findById(p.getCodigo());
		if (professorOptional.isPresent()) {
			return professorOptional.get();
		} else {
			return null;
		}
	}

	private List<Professor> listarProfessores() throws SQLException, ClassNotFoundException {
		List<Professor> professores = pRep.findAllProfessores();
		return professores;
	}

	private Curso buscarCurso(Curso c) throws SQLException, ClassNotFoundException {
		Optional<Curso> produtoOptional = cRep.findById(c.getCodigo());
		if (produtoOptional.isPresent()) {
			return produtoOptional.get();
		} else {
			return null;
		}
	}

	private List<Curso> listarCursos() throws SQLException, ClassNotFoundException {
		List<Curso> cursos = cRep.findAllCursos();
		return cursos;
	}

}
