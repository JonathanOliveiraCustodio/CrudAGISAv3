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
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Eliminacao;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IEliminacaoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;


@Controller
public class SolicitarEliminacaoController {

	@Autowired
	IEliminacaoRepository eRep;

	@Autowired
	ICursoRepository cRep;

	@Autowired
	IAlunoRepository aRep;

	@Autowired
	IMatriculaRepository mRep;


	@RequestMapping(name = "solicitarEliminacao", value = "/solicitarEliminacao", method = RequestMethod.GET)
	public ModelAndView analisarEliminacaoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		return new ModelAndView("solicitarEliminacao");
	}

	@RequestMapping(name = "solicitarEliminacao", value = "/solicitarEliminacao", method = RequestMethod.POST)
	public ModelAndView analisarEliminacaoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
	//	String codigo = allRequestParam.get("codigo");
	//	String codigoMatricula = allRequestParam.get("codigoMatricula");
	//	String codigoDisciplina = allRequestParam.get("codigoDisciplina");

		String nomeInstituicao = allRequestParam.get("nomeInstituicao");

		String disciplina = allRequestParam.get("disciplina");
		String RA = allRequestParam.get("RA");

		String saida = "";
		String erro = "";

		Eliminacao e = new Eliminacao();

		Aluno a = new Aluno();

		// Curso c = new Curso();

		Matricula m = new Matricula();

		List<Eliminacao> eliminacoes = new ArrayList<>();
		List<Disciplina> disciplinas = new ArrayList<>();

		try {
			a.setRA(Integer.parseInt(RA));
			a = buscarAlunoRA(a, Integer.parseInt(RA));

			m = buscarMatricula(m, Integer.parseInt(RA));
			m.setCodigoAluno(a);

			if (cmd.contains("Solicitar")) {

				e.setCodigoDisciplina(Integer.parseInt(disciplina));
				e.setCodigoMatricula(m.getCodigo());
				e.setNomeInstituicao(nomeInstituicao);

				System.out.println(disciplina);
				System.out.println(m.getCodigo());
				System.out.println(nomeInstituicao);
				saida = cadastrarEliminacao(e);
				e = null;
			}

			if (cmd.contains("Buscar")) {
				a = buscarAlunoRA(a, Integer.parseInt(RA));
				if (a == null) {
					saida = "Nenhum aluno encontrado com o RA especificado.";
				}
				disciplinas = listarDisciplinas(Integer.parseInt(RA));

			}
			if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
				a = null;
				e = null;
			}

			if (cmd.contains("Listar")) {
				if (cmd != null && !cmd.isEmpty()) {

					eliminacoes = listarEliminacoes(Integer.parseInt(RA));
				} else {
					saida = "Nenhum aluno encontrado para Listar.";
				}
			}

		} catch (SQLException | ClassNotFoundException error) {
			erro = error.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("solicitarEliminacao", e);
			model.addAttribute("aluno", a);
			model.addAttribute("eliminacoes", eliminacoes);
			model.addAttribute("disciplinas", disciplinas);
		}

		return new ModelAndView("solicitarEliminacao");

	}

	private String cadastrarEliminacao(Eliminacao e) throws SQLException, ClassNotFoundException {
		String saida = eRep.sp_inserir_eliminacao(e.getCodigoMatricula(), e.getCodigoDisciplina(),
				e.getNomeInstituicao());
		return saida;
	}


	private Aluno buscarAlunoRA(Aluno a, int RA) throws SQLException, ClassNotFoundException {
		a = aRep.findByRA(RA);
		return a;
	}

	private List<Eliminacao> listarEliminacoes(int RA) throws SQLException, ClassNotFoundException {
		List<Eliminacao> eliminacoes = eRep.findAllEliminacoesRA(RA);
		return eliminacoes;
	}

	private List<Disciplina> listarDisciplinas(int RA) throws SQLException, ClassNotFoundException {
		List<Disciplina> disciplinas = eRep.findAllDisciplinasRA(RA);
		return disciplinas;
	}

	private Matricula buscarMatricula(Matricula m, int RA) throws SQLException, ClassNotFoundException {
		m = mRep.findAllBuscarMatricula(RA);
		return m;
	}

}