package br.edu.fateczl.CrudAGISAv3.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fateczl.CrudAGISAv3.model.PeriodoMatricula;
import br.edu.fateczl.CrudAGISAv3.repository.IPeriodoMatriculaRepository;

@Controller
public class PeriodoMatriculaController {

	
	@Autowired
	IPeriodoMatriculaRepository cDao;

	@RequestMapping(name = "periodoMatricula", value = "/periodoMatricula", method = RequestMethod.GET)
	public ModelAndView periodoMatriculaGet(@RequestParam Map<String, String> allRequestParam, ModelMap model) {

		String erro = "";

		PeriodoMatricula pm = new PeriodoMatricula();

		try {
			pm = cDao.findConsultaPeriodoMatricula();
		} catch (Exception e) {
			erro = e.getMessage();
		}

		model.addAttribute("erro", erro);
		model.addAttribute("peridoMatricula", pm);

		return new ModelAndView("periodoMatricula");
	}

	@RequestMapping(name = "periodoMatricula", value = "/periodoMatricula", method = RequestMethod.POST)
	public ModelAndView periodoMatriculaPost(@RequestParam Map<String, String> allRequestParam, ModelMap model) {
	
		String periodoMatriculaInicio = allRequestParam.get("periodoMatriculaInicio");
		String periodoMatriculaFim = allRequestParam.get("periodoMatriculaFim");

		String saida = "";
		String erro = "";

		PeriodoMatricula pm = new PeriodoMatricula();
		pm.setPeriodoMatriculaInicio(Date.valueOf(periodoMatriculaInicio));
		pm.setPeriodoMatriculaFim(Date.valueOf(periodoMatriculaFim));

		try {
			saida = alterarPeriodoMatricula(pm);
		} catch (Exception e) {
			erro = e.getMessage();
		}
		model.addAttribute("saida", saida);
		model.addAttribute("erro", erro);
		model.addAttribute("periodoMatricula", pm);

		return new ModelAndView("periodoMatricula");
	}

	private String alterarPeriodoMatricula(PeriodoMatricula pm) throws SQLException, ClassNotFoundException {
		String saida = cDao.sp_u_periodomatricula(pm.getPeriodoMatriculaInicio(),pm.getPeriodoMatriculaFim());
		return saida;

	}

}
