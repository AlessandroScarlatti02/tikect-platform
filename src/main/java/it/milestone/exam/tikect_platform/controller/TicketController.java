package it.milestone.exam.tikect_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import it.milestone.exam.tikect_platform.repository.TicketRepository;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class TicketController {

    @Autowired
    TicketRepository ticketRepo;

    @GetMapping()
    public String home(Model model) {

        model.addAttribute("tickets", ticketRepo.findAll());

        return "tickets/home";
    }

}
