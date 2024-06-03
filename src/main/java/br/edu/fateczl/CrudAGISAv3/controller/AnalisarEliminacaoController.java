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

import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.model.Curso;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.Eliminacao;
import br.edu.fateczl.CrudAGISAv3.model.Matricula;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IEliminacaoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;

@Controller
public class AnalisarEliminacaoController {

	@Autowired
	IEliminacaoRepository eRep;

	@Autowired
	ICursoRepository cRep;

	@Autowired
	IAlunoRepository aRep;

	@Autowired
	IMatriculaRepository mRep;
	
	@Autowired
	IDisciplinaRepository dRep;

	@RequestMapping(name = "analisarEliminacao", value = "/analisarEliminacao", method = RequestMethod.GET)
	public ModelAndView analisarEliminacaoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Eliminacao> eliminacoes = new ArrayList<>();

		Eliminacao e = new Eliminacao();

		try {
			String cmd = allRequestParam.get("cmd");
			String codigo = allRequestParam.get("codigo");

			eliminacoes = listarEliminacoes();
			if (cmd != null) {
				if (cmd.contains("alterar")) {

					e.setCodigo(Integer.parseInt(codigo));
					e = buscarEliminacao(e);
					// saida = alterarEliminacao(e);
					// e = null;

				}
				eliminacoes = listarEliminacoes();
			}

		} catch (ClassNotFoundException | SQLException error) {
			erro = error.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("analisarEliminacao", e);
			model.addAttribute("eliminacoes", eliminacoes);

		}

		return new ModelAndView("analisarEliminacao");
	}

	@RequestMapping(name = "analisarEliminacao", value = "/analisarEliminacao", method = RequestMethod.POST)
	public ModelAndView analisarEliminacaoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String status = allRequestParam.get("status");

		String saida = "";
		String erro = "";

		Eliminacao e = new Eliminacao();
		
		List<Eliminacao> eliminacoes = new ArrayList<>();

		try {
			if (!cmd.contains("Listar")) {
				e.setCodigo(Integer.parseInt(codigo));
			}

			if (cmd.contains("Cadastrar")) {
				e.setStatus(status);
				e = null;
			} else if (cmd.contains("Alterar")) {
				e = buscarEliminacao(e);
				e.setStatus(status);
				saida = alterarEliminacao(e);

				e = null;
				eliminacoes = listarEliminacoes();
			} else if (cmd.contains("Buscar")) {
				e = buscarEliminacao(e);
				if (e == null) {
					saida = "Nenhuma Eliminação encontrada com o código especificado.";
				}
			} else if (cmd.contains("Listar")) {
				eliminacoes = listarEliminacoes();
			}
		} catch (SQLException | ClassNotFoundException error) {
			erro = error.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("analisarEliminacao", e);
			model.addAttribute("eliminacoes", eliminacoes);
		}

		return new ModelAndView("analisarEliminacao");

	}

	private String alterarEliminacao(Eliminacao e) throws SQLException, ClassNotFoundException {
		String saida = eRep.sp_update_eliminacao("U", e.getCodigo(), e.getCodigoDisciplina(), e.getCodigoMatricula(),
				e.getDataEliminacao(), e.getNomeInstituicao(), e.getStatus());
		return saida;
	}

	private Eliminacao buscarEliminacao(Eliminacao e) throws SQLException, ClassNotFoundException {
	    Optional<Eliminacao> eliminacaoOptional = eRep.findById(e.getCodigo());
	    if (eliminacaoOptional.isPresent()) {
	        Eliminacao eliminacao = eliminacaoOptional.get();
	      
	        Aluno aluno = aRep.findById(eliminacao.getMatriculaDisciplina().getCodigoMatricula().getCodigoAluno().getCPF()).orElse(null);
	        Curso curso = cRep.findById(eliminacao.getMatriculaDisciplina().getCodigoMatricula().getCodigoAluno().getCurso().getCodigo()).orElse(null);
	        Disciplina disciplina = dRep.findById(eliminacao.getMatriculaDisciplina().getCodigoDisciplina().getCodigo()).orElse(null); // Use dRep instead of cRep
	        
	        eliminacao.setAluno(aluno);
	        eliminacao.setCurso(curso);
	        eliminacao.setDisciplina(disciplina);
	        return eliminacao;
	    } else {
	        return null;
	    }
	}
	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
		} else {
			return null;
		}
	}

	private Curso buscarCurso(Curso c) throws SQLException, ClassNotFoundException {
		Optional<Curso> produtoOptional = cRep.findById(c.getCodigo());
		if (produtoOptional.isPresent()) {
			return produtoOptional.get();
		} else {
			return null;
		}
	}
	
	private Disciplina buscarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		Optional<Disciplina> produtoOptional = dRep.findById(d.getCodigo());
		if (produtoOptional.isPresent()) {
			return produtoOptional.get();
		} else {
			return null;
		}
	}

	private Matricula buscarMatricula(Matricula m, int RA) throws SQLException, ClassNotFoundException {
		// m = mDao.buscarMatricula(RA);
		return m;
	}

	private List<Eliminacao> listarEliminacoes() throws SQLException, ClassNotFoundException {
		List<Eliminacao> eliminacoes = eRep.findAllEliminacoes();
		return eliminacoes;
	}

}