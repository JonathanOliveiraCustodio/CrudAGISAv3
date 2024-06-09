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
import br.edu.fateczl.CrudAGISAv3.model.Aluno;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.ICursoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IEliminacaoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IMatriculaRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class RelatorioHistoricoAlunoController {

	@Autowired
	DataSource ds; 
	
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

	@RequestMapping(name = "relatorioHistoricoAluno", value = "/relatorioHistoricoAluno", method = RequestMethod.GET)
	public ModelAndView relatorioHistoricoAlunoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Aluno> alunos = new ArrayList<>();

		Aluno a = new Aluno();

		try {
			String cmd = allRequestParam.get("cmd");
			String CPF = allRequestParam.get("CPF");

			alunos = listarAlunos();
			if (cmd != null) {
				if (cmd.contains("alterar")) {
					a.setCPF(CPF);
					a = buscarAluno(a);
				}
				alunos = listarAlunos();
			}

		} catch (ClassNotFoundException | SQLException error) {
			erro = error.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("relatorioHistoricoAluno", a);
			model.addAttribute("alunos", alunos);

		}

		return new ModelAndView("relatorioHistoricoAluno");
	}

	@RequestMapping(name = "relatorioHistoricoAluno", value = "/relatorioHistoricoAluno", method = RequestMethod.POST)
	public ModelAndView relatorioHistoricoAlunoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String CPF = allRequestParam.get("CPF");

		String saida = "";
		String erro = "";

		Aluno a = new Aluno();
		
		List<Aluno> alunos = new ArrayList<>();

		try {
			if (!cmd.contains("Listar")) {
				a.setCPF(CPF);
			}
			if (cmd.contains("Cadastrar")) {
				a= null;
			} else if (cmd.contains("Alterar")) {

			} else if (cmd.contains("Buscar")) {
				a = buscarAluno(a);
				if (a == null) {
					saida = "Nenhuma Aluno encontrada com o código especificado.";
				}
			} else if (cmd.contains("Listar")) {
				alunos = listarAlunos();
			}
		} catch (SQLException | ClassNotFoundException error) {
			erro = error.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("relatorioHistoricoAluno", a);
			model.addAttribute("alunos", alunos);
		}

		return new ModelAndView("relatorioHistoricoAluno");

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(name = "relatorioHistoricoAlunoGerar", value = "/relatorioHistoricoAlunoGerar", method = RequestMethod.POST)
	public ResponseEntity relatorioPost(@RequestParam Map<String, String> allRequestParam) {
		String erro = "";
		Map<String, Object> paramRelatorio =  new HashMap<String,Object>();
		paramRelatorio.put("CPF", allRequestParam.get("CPF"));
		
		byte [] bytes = null;
		
		//Inicializando os Elementos do Response
		InputStreamResource resource = null;
		HttpStatus status = null;
		HttpHeaders header = new HttpHeaders();
		
		try {
			Connection c = DataSourceUtils.getConnection(ds);
			File arquivo = ResourceUtils.getFile("classpath:reports/RelatorioHistorico.jasper");
		
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

	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
		} else {
			return null;
		}
	}

	private List<Aluno> listarAlunos() throws SQLException, ClassNotFoundException {
		List<Aluno> alunos = aRep.findAllByOrderByNomeAsc();
		return alunos;
	}

}