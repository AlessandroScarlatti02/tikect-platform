package it.milestone.exam.tikect_platform.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Note;
import it.milestone.exam.tikect_platform.model.Role;
import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.model.User;
import it.milestone.exam.tikect_platform.repository.CategoryRepository;
import it.milestone.exam.tikect_platform.repository.RoleRepository;
import it.milestone.exam.tikect_platform.repository.TicketRepository;
import it.milestone.exam.tikect_platform.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    RoleRepository roleRepo;

    @GetMapping
    public String home(Model model, @RequestParam(name = "keyword", required = false) String keyword) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("tickets", ticketRepo.findByObjectContains(keyword));
        } else {

            model.addAttribute("tickets", ticketRepo.findAll());
        }

        model.addAttribute("users", userRepo.findAll());
        return "tickets/home";
    }

    @GetMapping("/create")
    public String create(Model model, RedirectAttributes redirectAttributes) {

        List<User> avaliableUsers = userRepo.findByState(true);
        if (avaliableUsers.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Can't Create Ticket because no Operators avaliable");
            return "redirect:/tickets";
        }
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryRepo.findAll());

        return "tickets/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, Random random) {

        List<User> avaliableUsers = userRepo.findByState(true);
        int size = avaliableUsers.size();
        int i = random.nextInt(size);

        model.addAttribute("categories", categoryRepo.findAll());
        if (ticketForm.getCategory() == null) {
            bindingResult.addError(new ObjectError("Category not Found", "Insert Category"));
        }
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        }

        ticketForm.setUser(avaliableUsers.get(i));
        ticketRepo.save(ticketForm);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket created succesfully");

        return "redirect:/tickets";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Long id, Model model) {

        Optional<Ticket> ticket = ticketRepo.findById(id);

        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
        }
        return "tickets/show";
    }

    @GetMapping("/show/{id}/notes")
    public String notes(Model model, RedirectAttributes redirectAttributes, @PathVariable Long id) {

        List<Note> notes = ticketRepo.findById(id).get().getNotes();
        model.addAttribute("ticket", ticketRepo.findById(id).get());
        model.addAttribute("notes", notes);
        model.addAttribute("users", userRepo.findAll());
        Note note = new Note();
        note.setTicket(ticketRepo.findById(id).get());
        model.addAttribute("note", note);

        return "notes/index";
    }

}
