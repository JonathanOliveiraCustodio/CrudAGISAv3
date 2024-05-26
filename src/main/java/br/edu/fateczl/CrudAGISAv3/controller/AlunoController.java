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
import br.edu.fateczl.CrudAGISAv3.model.Curso;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;

@Controller
public class AlunoController {

	@Autowired
	IAlunoRepository aRep;

	@Autowired
	ICursoRepository cRep;

	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.GET)
	public ModelAndView alunoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("cmd");
		String CPF = allRequestParam.get("CPF");

		Aluno a = new Aluno();
		a.setCPF(CPF);

		String saida = "";
		String erro = "";
		List<Aluno> alunos = new ArrayList<>();
		List<Curso> cursos = new ArrayList<>();

		try {
			if (cmd != null) {
				if (cmd.contains("alterar")) {
					a = buscarAluno(a);
				} else if (cmd.contains("excluir")) {
					a = buscarAluno(a);
					saida = excluirAluno(a);
				}
				alunos = listarAlunos();
			}
			cursos = listarCursos();
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);
			model.addAttribute("cursos", cursos);
		}

		return new ModelAndView("aluno");
	}

	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.POST)
	public ModelAndView alunoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String CPF = allRequestParam.get("CPF");
		String nome = allRequestParam.get("nome");
		String nomeSocial = allRequestParam.get("nomeSocial");
		String dataNascimento = allRequestParam.get("dataNascimento");
		String telefoneContato = allRequestParam.get("telefoneContato");
		String emailPessoal = allRequestParam.get("emailPessoal");
		String emailCorporativo = allRequestParam.get("emailCorporativo");
		String dataConclusao2Grau = allRequestParam.get("dataConclusao2Grau");
		String instituicaoConclusao2Grau = allRequestParam.get("instituicaoConclusao2Grau");
		String pontuacaoVestibular = allRequestParam.get("pontuacaoVestibular");
		String posicaoVestibular = allRequestParam.get("posicaoVestibular");
		String anoIngresso = allRequestParam.get("anoIngresso");
		String semestreIngresso = allRequestParam.get("semestreIngresso");
		String semestreAnoLimiteGraduacao = allRequestParam.get("semestreAnoLimiteGraduacao");
		String RA = allRequestParam.get("RA");
		String curso = allRequestParam.get("curso");

		// saida
		String saida = "";
		String erro = "";
		Aluno a = new Aluno();
		Curso c = new Curso();
		List<Aluno> alunos = new ArrayList<>();
		List<Curso> cursos = new ArrayList<>();

		if (!cmd.contains("Listar")) {
			a.setCPF((CPF));
		}
		try {
			cursos = listarCursos();

			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {

				a.setNome(nome);
				a.setNomeSocial(nomeSocial);
				a.setDataNascimento(Date.valueOf(dataNascimento));
				a.setTelefoneContato(telefoneContato);
				a.setEmailPessoal(emailPessoal);
				a.setEmailCorporativo(emailCorporativo);
				a.setDataConclusao2Grau(Date.valueOf(dataConclusao2Grau));
				a.setInstituicaoConclusao2Grau(instituicaoConclusao2Grau);
				a.setPontuacaoVestibular(Float.parseFloat(pontuacaoVestibular));
				a.setPosicaoVestibular(Integer.parseInt(posicaoVestibular));
				a.setAnoIngresso(Integer.parseInt(anoIngresso));
				a.setSemestreIngresso(Integer.parseInt(semestreIngresso));
				a.setSemestreAnoLimiteGraduacao(Date.valueOf(semestreAnoLimiteGraduacao));
				a.setRA(Integer.parseInt(RA));

				c.setCodigo(Integer.parseInt(curso));
				c = buscarCurso(c);
				a.setCurso(c);
			}

			if (cmd.contains("Cadastrar")) {
				saida = cadastrarAluno(a);
				a = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarAluno(a);
				a = null;
			}
			if (cmd.contains("Excluir")) {
				a = buscarAluno(a);
				saida = excluirAluno(a);
				a = null;
			}
			if (cmd.contains("Buscar")) {
				a = buscarAluno(a);
				if (a == null) {
					saida = "Nenhum aluno encontrado com o CPF especificado.";
					a = null;
				}
			}
			if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
				a = null;
			}

			if (cmd.contains("Listar")) {
				alunos = listarAlunos();
			}

			if (cmd.contains("Telefone")) {
				a = buscarAluno(a);
				if (a == null) {
					saida = "Nenhum aluno encontrado com o CPF especificado.";
					a = null;
				} else {
					model.addAttribute("aluno", a);
					return new ModelAndView("forward:/telefone", model);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);
			model.addAttribute("cursos", cursos);
		}

		return new ModelAndView("aluno");
	}

	private String cadastrarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		String saida = aRep.sp_iud_aluno("I", a.getCPF(), a.getRA(), a.getAnoIngresso(), a.getDataConclusao2Grau(),
				a.getDataNascimento(), a.getEmailCorporativo(), a.getEmailPessoal(), a.getInstituicaoConclusao2Grau(),
				a.getNome(), a.getNomeSocial(), a.getPontuacaoVestibular(), a.getPosicaoVestibular(),
				a.getSemestreAnoLimiteGraduacao(), a.getSemestreIngresso(), a.getTelefoneContato(),
				a.getCurso().getCodigo());
		return saida;

	}

	private String alterarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		String saida = aRep.sp_iud_aluno("U", a.getCPF(), a.getRA(), a.getAnoIngresso(), a.getDataConclusao2Grau(),
				a.getDataNascimento(), a.getEmailCorporativo(), a.getEmailPessoal(), a.getInstituicaoConclusao2Grau(),
				a.getNome(), a.getNomeSocial(), a.getPontuacaoVestibular(), a.getPosicaoVestibular(),
				a.getSemestreAnoLimiteGraduacao(), a.getSemestreIngresso(), a.getTelefoneContato(),
				a.getCurso().getCodigo());
		return saida;

	}

	private String excluirAluno(Aluno a) throws SQLException, ClassNotFoundException {
		String saida = aRep.sp_iud_aluno("D", a.getCPF(), a.getRA(), a.getAnoIngresso(), a.getDataConclusao2Grau(),
				a.getDataNascimento(), a.getEmailCorporativo(), a.getEmailPessoal(), a.getInstituicaoConclusao2Grau(),
				a.getNome(), a.getNomeSocial(), a.getPontuacaoVestibular(), a.getPosicaoVestibular(),
				a.getSemestreAnoLimiteGraduacao(), a.getSemestreIngresso(), a.getTelefoneContato(),
				a.getCurso().getCodigo());
		return saida;

	}

	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
		} else {
			return null;
		}
	}

	private List<Aluno> listarAlunos() throws SQLException, ClassNotFoundException {
		List<Aluno> alunos = aRep.findAllAlunos();
		return alunos;
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