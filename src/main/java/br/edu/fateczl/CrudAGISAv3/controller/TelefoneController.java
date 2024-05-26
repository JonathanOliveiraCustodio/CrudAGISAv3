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
import br.edu.fateczl.CrudAGISAv3.model.Telefone;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.ITelefoneRepository;

@Controller
public class TelefoneController {

	@Autowired
	IAlunoRepository aRep;

	@Autowired
	ITelefoneRepository tRep;

	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.GET)
	public ModelAndView telefoneGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String aluno = allRequestParam.get("aluno");

		List<Telefone> telefones = new ArrayList<>();

		Aluno a = new Aluno();
		a.setCPF(aluno);

		try {
			telefones = listarTelefones(aluno);

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();

		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("telefones", telefones);
			model.addAttribute("aluno", a);
		}
		return new ModelAndView("telefone");
	}

	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.POST)
	public ModelAndView telefonePost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String aluno = allRequestParam.get("aluno");
		String numero = allRequestParam.get("numero");
		String tipo = allRequestParam.get("tipo");

		if (cmd != null && cmd.equals("Telefones")) {

			return new ModelAndView("redirect:/telefone?aluno=" + aluno, model);
		}

		String saida = "";
		String erro = "";

		Telefone t = new Telefone();
		Aluno a = new Aluno();

		List<Telefone> telefones = new ArrayList<>();

		try {
			if (!cmd.contains("Listar")) {
				a.setCPF(aluno);
				a = buscarAluno(a);
				t.setAluno(a);
				t.setNumero(numero);
				t.setTipo(tipo);
			}

			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				t.setNumero(numero);
				t.setTipo(tipo);
			}

			if (cmd.contains("Cadastrar")) {
				saida = cadastrarTelefone(t, a);
			}
			if (cmd.contains("Alterar")) {
				saida = alterarTelefone(t, a);
			}
			if (cmd.contains("Excluir")) {
				saida = excluirTelefone(t, a);
			}
			if (cmd.contains("Buscar")) {
				t = buscarTelefone(t);
				if (t == null) {
					saida = "Nenhum conteudo encontrado com o código especificado.";
				}
			}

			telefones = listarTelefones(aluno);

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("telefone", t);
			model.addAttribute("telefones", telefones);
			model.addAttribute("aluno", a);
		}

		return new ModelAndView("telefone");

	}

	private String cadastrarTelefone(Telefone t, Aluno a) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iud_telefone("I", a.getCPF(),t.getNumero(), t.getTipo());
		return saida;

	}

	private String alterarTelefone(Telefone t, Aluno a) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iud_telefone("U", a.getCPF(),t.getNumero(), t.getTipo());
		return saida;

	}

	private String excluirTelefone(Telefone t, Aluno a) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iud_telefone("D", a.getCPF(),t.getNumero(), t.getTipo());
		return saida;

	}

	private Telefone buscarTelefone(Telefone t) throws SQLException, ClassNotFoundException {
	//	t = tRep.fn_consultar_telefone_aluno(t.getAluno().getCPF(),t.getNumero());
		return t;
	}

	private List<Telefone> listarTelefones(String cpfAluno) throws SQLException, ClassNotFoundException {
		List<Telefone> telefones = tRep.findAllTelefones(cpfAluno);
		return telefones;
	}

	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
		} else {
			return null;
		}
	}

}
