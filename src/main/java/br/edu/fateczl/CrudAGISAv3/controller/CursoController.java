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
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;

@Controller
public class CursoController {

	@Autowired
	private ICursoRepository cRep;

	@RequestMapping(name = "curso", value = "/curso", method = RequestMethod.GET)
	public ModelAndView cursoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Curso> cursos = new ArrayList<>();
		Curso c = new Curso();

		try {
			String cmd = allRequestParam.get("cmd");
			String codigo = allRequestParam.get("codigo");

			if (cmd != null) {
				if (cmd.contains("alterar")) {

					c.setCodigo(Integer.parseInt(codigo));
					c = buscarCurso(c);

				} else if (cmd.contains("excluir")) {
					c.setCodigo(Integer.parseInt(codigo));
					saida = excluirCurso(c);

				}
				cursos = listarCurso();
			}

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("curso", c);
			model.addAttribute("cursos", cursos);

		}
		return new ModelAndView("curso");
	}

	@RequestMapping(name = "curso", value = "/curso", method = RequestMethod.POST)
	public ModelAndView cursoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");
		String nome = allRequestParam.get("nome");
		String cargaHoraria = allRequestParam.get("cargaHoraria");
		String sigla = allRequestParam.get("sigla");
		String ultimaNotaENADE = allRequestParam.get("ultimaNotaENADE");
		String turno = allRequestParam.get("turno");

		// saida
		String saida = "";
		String erro = "";
		Curso c = new Curso();
		List<Curso> cursos = new ArrayList<>();

		if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
			c = null;

		} else {

			if (!cmd.contains("Listar")) {
				c.setCodigo(Integer.parseInt(codigo));
			}
			if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
				c.setNome(nome);
				c.setCargaHoraria(Integer.parseInt(cargaHoraria));
				c.setSigla(sigla);
				c.setUltimaNotaENADE(Float.parseFloat(ultimaNotaENADE));
				c.setTurno(turno);

			}
			try {
				if (cmd.contains("Cadastrar")) {
					saida = cadastrarCurso(c);
					c = null;
				}
				if (cmd.contains("Alterar")) {
					saida = alterarCurso(c);
					c = null;
				}
				if (cmd.contains("Excluir")) {
					saida = excluirCurso(c);
					c = null;
				}
				if (cmd.contains("Buscar")) {
					c = buscarCurso(c);
					if (c == null) {
						saida = "Nenhum curso encontrado com o código especificado.";
						c = null;
					}
				}

				if (cmd.contains("Listar")) {
					cursos = listarCurso();
				}
			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			}
		}

		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("curso", c);
		model.addAttribute("cursos", cursos);

		return new ModelAndView("curso");
	}

	private String cadastrarCurso(Curso c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_curso("I", c.getCodigo(), c.getNome(), c.getCargaHoraria(), c.getSigla(),
				c.getUltimaNotaENADE(), c.getTurno());
		return saida;

	}

	private String alterarCurso(Curso c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_curso("U", c.getCodigo(), c.getNome(), c.getCargaHoraria(), c.getSigla(),
				c.getUltimaNotaENADE(), c.getTurno());
		return saida;

	}

	private String excluirCurso(Curso c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_curso("D", c.getCodigo(), c.getNome(), c.getCargaHoraria(), c.getSigla(),
				c.getUltimaNotaENADE(), c.getTurno());
		return saida;

	}

	private Curso buscarCurso(Curso c) throws SQLException, ClassNotFoundException {
		Optional<Curso> produtoOptional = cRep.findById(c.getCodigo());
		if (produtoOptional.isPresent()) {
			return produtoOptional.get();
		} else {
			return null;
		}
	}

	private List<Curso> listarCurso() throws SQLException, ClassNotFoundException {
		List<Curso> cursos = cRep.findAllCursos();
		return cursos;
	}

}