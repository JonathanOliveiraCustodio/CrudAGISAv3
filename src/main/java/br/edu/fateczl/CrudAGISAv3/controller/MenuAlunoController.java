package br.edu.fateczl.CrudAGISAv3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MenuAlunoController {

	@RequestMapping(name = "menuAluno", value = "/menuAluno", method = RequestMethod.GET)
	public ModelAndView menuAlunoGet(ModelMap model) {
		return new ModelAndView("menuAluno");
	}

	@RequestMapping(name = "menuAluno", value = "/menuAluno", method = RequestMethod.POST)
	public ModelAndView menuAlunoPost(ModelMap model) {
		return new ModelAndView("menuAluno");
	}
}