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
import br.edu.fateczl.CrudAGISAv3.model.Conteudo;
import br.edu.fateczl.CrudAGISAv3.model.Disciplina;
import br.edu.fateczl.CrudAGISAv3.repository.IConteudoRepository;
import br.edu.fateczl.CrudAGISAv3.repository.IDisciplinaRepository;


@Controller
public class ConteudoController {

	@Autowired
	IConteudoRepository cRep;
	
	@Autowired
	IDisciplinaRepository dRep;
	
	@RequestMapping(name = "conteudo", value = "/conteudo", method = RequestMethod.GET)
	public ModelAndView conteudoGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
		
		String erro = "";
		String saida = "";
		
		List<Disciplina> disciplinas = new ArrayList<>();
	    List<Conteudo> conteudos = new ArrayList<>();

		Conteudo c = new Conteudo();

		try {
			String cmd = allRequestParam.get("cmd");
			String codigo = allRequestParam.get("codigo");

			if (cmd != null) {
				if (cmd.contains("alterar")) {

					c.setCodigo(Integer.parseInt(codigo));
					c = buscarConteudo(c);

				} else if (cmd.contains("excluir")) {
					
					c.setCodigo(Integer.parseInt(codigo));
					c = buscarConteudo(c);
					saida = excluirConteudo(c);
					c = null;

				}
				conteudos = listarConteudos();
			}
			disciplinas = listarDisciplinas();

		} catch (ClassNotFoundException | SQLException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("conteudo", c);
			model.addAttribute("conteudos", conteudos);
			model.addAttribute("disciplinas", disciplinas);

		}
		return new ModelAndView("conteudo");
	}
	@RequestMapping(name = "conteudo", value = "/conteudo", method = RequestMethod.POST)
	public ModelAndView conteudoPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String cmd = allRequestParam.get("botao");
		String codigo = allRequestParam.get("codigo");	
		String nome = allRequestParam.get("nome");
		String descricao = allRequestParam.get("descricao");
		String disciplina = allRequestParam.get("disciplina");
		
		// saida
		String saida = "";
		String erro = "";	
		
		Conteudo c = new Conteudo();
		Disciplina d = new Disciplina();

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Conteudo> conteudos = new ArrayList<>();
	    

		try {
			
		if (!cmd.contains("Listar")) {
			d.setCodigo(Integer.parseInt(disciplina));
			d = buscarDisciplina(d);
			c.setDisciplina(d);
			c.setCodigo(Integer.parseInt(codigo));
		}
			
			disciplinas = listarDisciplinas();
			//conteudos = listarConteudos();
				
		    if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {	  			
		    	c.setNome(nome);
		    	c.setDescricao(descricao);			
		    }

		    if (cmd.contains("Cadastrar")) {
		    	saida = cadastrarConteudo(c);
				c = null;
		    }
		    if (cmd.contains("Alterar")) {
		    	saida = alterarConteudo(c);
				c = null;
		    }
		    if (cmd.contains("Excluir")) {    	
		   
		    	saida = excluirConteudo(c);
				c = null;
		    }
		    if (cmd.contains("Buscar")) {
				c = buscarConteudo(c);
				if (c == null) {
					saida = "Nenhum conteudo encontrado com o código especificado.";
					c = null;
				}
			}
			if (cmd != null && !cmd.isEmpty() && cmd.contains("Limpar")) {
				c = null;
			}

			if (cmd.contains("Listar")) {
				conteudos = listarConteudos();
			}
		    			    
		} catch (SQLException | ClassNotFoundException e) {
		    erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
		    model.addAttribute("erro", erro);
		    model.addAttribute("conteudo", c);
		    model.addAttribute("disciplinas", disciplinas);
		    model.addAttribute("conteudos", conteudos);		
		}
		
		return new ModelAndView("conteudo");
	}
	
	private String cadastrarConteudo(Conteudo c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_conteudo("I", c.getCodigo(),c.getDescricao(),c.getNome(),c.getDisciplina().getCodigo());
		return saida;

	}

	private String alterarConteudo(Conteudo c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_conteudo("U", c.getCodigo(),c.getDescricao(),c.getNome(),c.getDisciplina().getCodigo());
		return saida;

	}

	private String excluirConteudo(Conteudo c) throws SQLException, ClassNotFoundException {
		String saida = cRep.sp_iud_conteudo("D", c.getCodigo(),c.getDescricao(),c.getNome(),c.getDisciplina().getCodigo());;
		return saida;

	}
	private Conteudo buscarConteudo(Conteudo c) throws SQLException, ClassNotFoundException {
		Optional<Conteudo> conteudoOptional = cRep.findById(c.getCodigo());
		if (conteudoOptional.isPresent()) {
			return conteudoOptional.get();
		} else {
			return null;
		}
	}

	private List<Conteudo> listarConteudos() throws SQLException, ClassNotFoundException {
		List<Conteudo> conteudos = cRep.findAllConteudos();
		return conteudos;
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

}
