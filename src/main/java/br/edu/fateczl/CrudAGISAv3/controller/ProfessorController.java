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
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.IProfessorRepository;

@Controller
public class ProfessorController {
	
	@Autowired
	IProfessorRepository pRep;

	@RequestMapping(name = "professor", value = "/professor", method = RequestMethod.GET)
	public ModelAndView professorGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";
		
		List<Professor> professores = new ArrayList<>();
		Professor p = new Professor();

		try {
			String cmd = allRequestParam.get("cmd");
			String codigo = allRequestParam.get("codigo");

			if (cmd != null) {
				if (cmd.contains("alterar")) {

					p.setCodigo(Integer.parseInt(codigo));
					p = buscarProfessor(p);

				} else if (cmd.contains("excluir")) {
					p.setCodigo(Integer.parseInt(codigo));
					saida = excluirProfessor(p);

				}
				professores = listarProfessores();
			}

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("professor", p);
			model.addAttribute("professores", professores);

		}
		return new ModelAndView("professor");
	}

	@RequestMapping(name = "professor", value = "/professor", method = RequestMethod.POST)
	public ModelAndView professorPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		// entrada
		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String nome = allRequestParam.get("nome");
		String titulacao = allRequestParam.get("titulacao");

		// saida
		String saida = "";
		String erro = "";
		Professor p = new Professor();
		List<Professor> professores = new ArrayList<>();

		if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
			p = null;

		} else {

			if (!cmd.contains("Listar")) {
				p.setCodigo(Integer.parseInt(codigo));
			}
			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				p.setNome(nome);
				p.setTitulacao(titulacao);
			}
			try {
				if (cmd.contains("Cadastrar")) {
					saida = cadastrarProfessor(p);
					p = null;
				}
				if (cmd.contains("Alterar")) {
					saida = alterarProfessor(p);
					p = null;
				}
				if (cmd.contains("Excluir")) {
					saida = excluirProfessor(p);
					p = null;
				}
				if (cmd.contains("Buscar")) {
					p = buscarProfessor(p);
					if (p == null) {
						saida = "Nenhum professor encontrado com o código especificado.";
						p = null;
					}
				}
				if (cmd.contains("Listar")) {
					professores = listarProfessores();
				}
			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			}
		}

		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("professor", p);
		model.addAttribute("professores", professores);

		return new ModelAndView("professor");
	}

	private String cadastrarProfessor(Professor p) throws SQLException, ClassNotFoundException {
		String saida = pRep.sp_iud_professor("I", p.getCodigo(),p.getNome(),p.getTitulacao());
		return saida;

	}

	private String alterarProfessor(Professor p) throws SQLException, ClassNotFoundException {
		String saida = pRep.sp_iud_professor("U", p.getCodigo(),p.getNome(),p.getTitulacao());
		return saida;

	}

	private String excluirProfessor(Professor p) throws SQLException, ClassNotFoundException {
		String saida = pRep.sp_iud_professor("D", p.getCodigo(),p.getNome(),p.getTitulacao());
		return saida;

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

}