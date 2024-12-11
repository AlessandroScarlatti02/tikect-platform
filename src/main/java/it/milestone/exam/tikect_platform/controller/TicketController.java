package it.milestone.exam.tikect_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.repository.CategoryRepository;
import it.milestone.exam.tikect_platform.repository.TicketRepository;
import it.milestone.exam.tikect_platform.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketRepository ticketRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping
    public String home(Model model, @RequestParam(name = "keyword", required = false) String keyword) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("tickets", ticketRepo.findByObjectContains(keyword));
        } else {

            model.addAttribute("tickets", ticketRepo.findAll());
        }

        return "tickets/home";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryRepo.findAll());

        return "tickets/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("categories", categoryRepo.findAll());
        if (ticketForm.getCategory() == null) {
            bindingResult.addError(new ObjectError("Category not Found", "Insert Category"));
        }
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        }

        ticketRepo.save(ticketForm);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket created succesfully");

        return "redirect:/tickets";
    }

}
