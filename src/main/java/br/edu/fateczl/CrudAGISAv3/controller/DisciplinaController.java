package br.edu.fateczl.CrudAGISAv3.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class DisciplinaController {

	@Autowired
	DataSource ds; 
	
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
		String tipoListar = allRequestParam.get("tipoListar");
		String valorPesquisa = allRequestParam.get("valorPesquisa");

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
					saida = "Nenhuma disciplina encontrada com o código especificado.";
					d = null;
				}
			}
			if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
				d = null;
			}

			if (cmd.contains("Listar")) {
				if ("nome".equals(tipoListar) || "diaSemana".equals(tipoListar) || "curso".equals(tipoListar)
						|| "professor".equals(tipoListar)) {
					disciplinas = dRep.findDisciplinasParametro(tipoListar, valorPesquisa);
				} else {
					disciplinas = listarDisciplinas();
					tipoListar = "Todas";
					valorPesquisa = "N/A";
				}
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
			model.addAttribute("tipoListar", tipoListar);
			model.addAttribute("valorPesquisa", valorPesquisa);
		}

		return new ModelAndView("disciplina");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(name = "disciplinaRelatorio", value = "/disciplinaRelatorio", method = RequestMethod.POST)
	public ResponseEntity relatorioPost(@RequestParam Map<String, String> allRequestParam) {
		String erro = "";
		Map<String, Object> paramRelatorio =  new HashMap<String,Object>();
		paramRelatorio.put("tipoListar", allRequestParam.get("tipoListar"));
	    paramRelatorio.put("valorPesquisa", allRequestParam.get("valorPesquisa"));
		
		byte [] bytes = null;
		
		InputStreamResource resource = null;
		HttpStatus status = null;
		HttpHeaders header = new HttpHeaders();
		
		try {
			Connection c = DataSourceUtils.getConnection(ds);
			File arquivo = ResourceUtils.getFile("classpath:reports/RelatorioDisciplinas.jasper");
		
			JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(arquivo.getAbsolutePath());
			bytes = JasperRunManager.runReportToPdf(report, paramRelatorio, c);
		 } catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
			erro = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		} finally {
			 if (erro.equals("")) {
				 InputStream inputStream = new ByteArrayInputStream(bytes);
				 resource = new InputStreamResource(inputStream);
				 header.setContentLength(bytes.length);
				 header.setContentType(MediaType.APPLICATION_PDF);
				 status = HttpStatus.OK;		 
			 }
		}
		return new ResponseEntity(resource, header,status);
	}


	private String cadastrarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		return dRep.sp_iud_disciplina("I", d.getCodigo(), d.getCurso().getCodigo(), d.getProfessor().getCodigo(),
				d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(), d.getDiaSemana(), d.getNome());
	}

	private String alterarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		return dRep.sp_iud_disciplina("U", d.getCodigo(), d.getCurso().getCodigo(), d.getProfessor().getCodigo(),
				d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(), d.getDiaSemana(), d.getNome());
	}

	private String excluirDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		return dRep.sp_iud_disciplina("D", d.getCodigo(), d.getCurso().getCodigo(), d.getProfessor().getCodigo(),
				d.getHorasSemanais(), d.getSemestre(), d.getHorarioInicio(), d.getDiaSemana(), d.getNome());
	}

	private Disciplina buscarDisciplina(Disciplina d) throws SQLException, ClassNotFoundException {
		Optional<Disciplina> disciplinaOptional = dRep.findById(d.getCodigo());
		return disciplinaOptional.orElse(null);
	}

	private List<Disciplina> listarDisciplinas() throws SQLException, ClassNotFoundException {
		List<Disciplina> disciplinas = dRep.findAllDisciplinas();
		System.out.println("Número de disciplinas listadas: " + disciplinas.size());
		return disciplinas;
	}

	private Professor buscarProfessor(Professor p) throws SQLException, ClassNotFoundException {
		Optional<Professor> professorOptional = pRep.findById(p.getCodigo());
		return professorOptional.orElse(null);
	}

	private List<Professor> listarProfessores() throws SQLException, ClassNotFoundException {
		return pRep.findAllProfessores();
	}

	private Curso buscarCurso(Curso c) throws SQLException, ClassNotFoundException {
		Optional<Curso> cursoOptional = cRep.findById(c.getCodigo());
		return cursoOptional.orElse(null);
	}

	private List<Curso> listarCursos() throws SQLException, ClassNotFoundException {
		return cRep.findAllCursos();
	}
}
