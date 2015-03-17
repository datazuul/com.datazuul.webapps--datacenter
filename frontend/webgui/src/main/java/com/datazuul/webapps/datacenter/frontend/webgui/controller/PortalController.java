package com.datazuul.webapps.datacenter.frontend.webgui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for Portal.
 *
 * @author ralf
 */
@Controller
//@RequestMapping("/") // base path (under context "/contacts") for request mapping, "/" alone is not needed
// (To override a class-level mapping rather than refine it, place a slash in front of the method-level mapping.)
public class PortalController {

  @Autowired
  private MessageSource messageSource;

  @RequestMapping(value = {"/", "index"})
  // mapped to "/index[.*]"
  public String index(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
    model.addAttribute("name", name);
    return "index";
  }
}
