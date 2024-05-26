package br.edu.fateczl.CrudAGISAv3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MenuSecretariaController {

	@RequestMapping(name = "menuSecretaria", value = "/menuSecretaria", method = RequestMethod.GET)
	public ModelAndView menuSecretariaGet(ModelMap model) {
		return new ModelAndView("menuSecretaria");
	}

	@RequestMapping(name = "menuSecretaria", value = "/menuSecretaria", method = RequestMethod.POST)
	public ModelAndView menuSecretariaPost(ModelMap model) {
		return new ModelAndView("menuSecretaria");
	}
}
