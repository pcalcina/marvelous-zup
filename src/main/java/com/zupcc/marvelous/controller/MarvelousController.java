package com.zupcc.marvelous.controller;

import com.zupcc.marvelous.model.Person;
import com.zupcc.marvelous.repo.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;

@Controller
public class MarvelousController {

    @Autowired
    private IUserRepo repo;

    @GetMapping("/index")
    public String index(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);

        Person u = new Person();
        u.setCpf("23254345656");
        u.setBirthday(LocalDate.of(2021, 12, 12 ));
        u.setEmail("a@c.com");
        u.setName("AA CC");
        repo.save(u);

        return "index";
    }

    @GetMapping("/list-people")
    public String listPeople(Model model) {
        model.addAttribute("people", repo.findAll());
        return "list-people";
    }
}
