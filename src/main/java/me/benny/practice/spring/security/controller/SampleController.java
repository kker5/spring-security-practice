package me.benny.practice.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SampleController {
    @RequestMapping(value = "/example", method = RequestMethod.GET)
    public String newPerson(Model model) {
        model.addAttribute("name", "김철수");
        model.addAttribute("age", 30);
        return "example";
    }
}
