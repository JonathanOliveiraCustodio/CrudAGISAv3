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
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.model.ListaChamada;
import br.edu.fateczl.CrudAGISAv3.model.Professor;
import br.edu.fateczl.CrudAGISAv3.repository.IAlunoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IListaChamadaRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IProfessorRepository;


@Controller
public class ListaChamadaController {


	@Autowired
	IDisciplinaRepository dRep;

	@Autowired
	IListaChamadaRepository lcRep;

	@Autowired
	IProfessorRepository pRep;
	
	@Autowired
	IAlunoRepository aRep;
	String dataDaLista;

	@RequestMapping(name = "listaChamada", value = "/listaChamada", method = RequestMethod.GET)
	public ModelAndView chamadaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";
		String saida = "";

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Professor> professores = new ArrayList<>();
		List<ListaChamada> datasChamadas = new ArrayList<>();

		ListaChamada lc = new ListaChamada();

		try {
			//String cmd = allRequestParam.get("cmd");
			//String codigo = allRequestParam.get("codigo");
			professores = listaProfessores();
			datasChamadas = listaChamadas();

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("listaChamada", lc);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("professores", professores);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("datasChamadas", datasChamadas);
		}

		return new ModelAndView("listaChamada");
	}

	@RequestMapping(name = "listaChamada", value = "/listaChamada", method = RequestMethod.POST)
	public ModelAndView listaChamdaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		String cmd = allRequestParam.get("botao");
		String professor = allRequestParam.get("professor");
		String dataChamada = allRequestParam.get("dataChamada");
		String disciplina = allRequestParam.get("disciplina");
		String aluno = allRequestParam.get("aluno");

		String saida = "";
		String erro = "";

		ListaChamada lc = new ListaChamada();

		Professor p = new Professor();
		Disciplina d = new Disciplina();
		Aluno a = new Aluno();

		List<Disciplina> disciplinas = new ArrayList<>();
		List<ListaChamada> listasChamadas = new ArrayList<>();
		List<Professor> professores = new ArrayList<>();
		List<ListaChamada> datasChamadas = new ArrayList<>();

		try {
			p.setCodigo(Integer.parseInt(professor));
			p = buscarProfessor(p);
			lc.setProfessor(p);
			professores = listaProfessores();
			disciplinas = listarDisciplinas(Integer.parseInt(professor));
			if (!disciplina.equals("0")) {
				datasChamadas = listaDatas(disciplina);
				d.setCodigo(Integer.parseInt(disciplina));
				d = buscarDisciplina(d);
				lc.setDisciplina(d);
			}
			if (cmd != null) {
				if (cmd.contains("Nova Chamada")) {
					gerarNovaChamada(Integer.parseInt(disciplina));
					datasChamadas = listaDatas(disciplina);
				}
				
				if (cmd.contains("Alterar")) {
					listasChamadas = consultarListaChamada(Integer.parseInt(disciplina), Date.valueOf(dataDaLista));
					lc.setCodigoDisciplina(d.getCodigo());
					lc.setDataChamada(Date.valueOf(dataDaLista));
					for (ListaChamada chamada : listasChamadas) {
						String codigoLc = String.valueOf(chamada.getCodigo());
						lc.setCodigo(Integer.parseInt(codigoLc));
						lc.setCodigoMatricula(chamada.getCodigoMatricula());
						lc.setPresenca1(allRequestParam.containsKey("presenca1[" + codigoLc + "]") ? 1 : 0);
						lc.setPresenca2(allRequestParam.containsKey("presenca2[" + codigoLc + "]") ? 1 : 0);
						if (d.getHorasSemanais() > 2) {
							lc.setPresenca3(allRequestParam.containsKey("presenca3[" + codigoLc + "]") ? 1 : 0);
							if (d.getHorasSemanais() > 3) {
								lc.setPresenca4(allRequestParam.containsKey("presenca4[" + codigoLc + "]") ? 1 : 0);
							}
						}
						saida = alterarListaChamada(lc);
					}
					lc = null;
					disciplina = null;
					professor = null;
					listasChamadas = null;
				}

				if (cmd.contains("Consultar")) {
//					a.setCPF((aluno));
//					a = buscarAluno(a);
//					lc.setAluno(a);
					d.setCodigo(Integer.parseInt(disciplina));
					d = buscarDisciplina(d);
					lc.setDisciplina(d);
					lc.setDataChamada(Date.valueOf(dataChamada));
					dataDaLista = dataChamada;

					listasChamadas = consultarListaChamada(Integer.parseInt(disciplina), Date.valueOf(dataChamada));
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {

			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("listaChamada", lc);
			model.addAttribute("listasChamadas", listasChamadas);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("disciplina", d);
			model.addAttribute("aluno", a);
			model.addAttribute("professores", professores);
			model.addAttribute("datasChamadas", datasChamadas);
			model.addAttribute("dataChamada", dataChamada);
		}
		return new ModelAndView("listaChamada");
	}

	private void gerarNovaChamada(int disciplina) throws ClassNotFoundException, SQLException {
		lcRep.sp_nova_chamada(disciplina);
	}

	private String alterarListaChamada(ListaChamada lc) throws SQLException, ClassNotFoundException {
		String saida = lcRep.sp_iud_listaChamada("U", lc.getCodigo(),lc.getCodigoDisciplina(),lc.getCodigoMatricula(),lc.getDataChamada(),lc.getPresenca1(),lc.getPresenca2(),lc.getPresenca3(),lc.getPresenca4());
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
	
	private Aluno buscarAluno(Aluno a) throws SQLException, ClassNotFoundException {
		Optional<Aluno> alunoOptional = aRep.findById(a.getCPF());
		if (alunoOptional.isPresent()) {
			return alunoOptional.get();
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

	private List<ListaChamada> consultarListaChamada(int codigoDisciplina, Date dataChamada)
			throws ClassNotFoundException, SQLException {
		List<ListaChamada> listachamada = lcRep.findConsultarListaChamada(codigoDisciplina, dataChamada);
		return listachamada;
	}

	private List<Disciplina> listarDisciplinas(int codigoProfessor) throws ClassNotFoundException, SQLException {
		List<Disciplina> disciplinas = lcRep.findlistarDisciplinaProfessor(codigoProfessor);
		return disciplinas;
	}

	private List<Professor> listaProfessores() throws SQLException, ClassNotFoundException {
		List<Professor> professores = pRep.findAllProfessores();
		return professores;
	}

	private List<ListaChamada> listaChamadas() throws ClassNotFoundException, SQLException {
		List<ListaChamada> listaChamadas = lcRep.findAllListaChamada();
		return listaChamadas;
	}

	private List<ListaChamada> listaDatas(String disciplina) throws ClassNotFoundException, SQLException {
		List<ListaChamada> datasChamadas = lcRep.findListarDatas(Integer.parseInt(disciplina));
		return datasChamadas;
	}

}