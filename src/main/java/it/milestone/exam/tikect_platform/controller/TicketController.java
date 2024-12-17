package it.milestone.exam.tikect_platform.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Note;
import it.milestone.exam.tikect_platform.model.Operator;
import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.repository.CategoryRepository;
import it.milestone.exam.tikect_platform.repository.NoteRepository;
import it.milestone.exam.tikect_platform.repository.OperatorRepository;
import it.milestone.exam.tikect_platform.repository.RoleRepository;
import it.milestone.exam.tikect_platform.repository.TicketRepository;
import it.milestone.exam.tikect_platform.repository.UserRepository;
import it.milestone.exam.tikect_platform.security.DatabaseUserDetails;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketRepository ticketRepo;

    @Autowired
    OperatorRepository operatorRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    NoteRepository noteRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping
    public String home(Model model, @RequestParam(name = "keyword", required = false) String keyword,
            @AuthenticationPrincipal DatabaseUserDetails authentication) {

        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("tickets", ticketRepo.findByObjectContains(keyword));
        } else {

            model.addAttribute("tickets", ticketRepo.findAll());
        }

        model.addAttribute("filteredTickets", ticketRepo.findByOperatorUserUsername(authentication.getUsername()));
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("operators", operatorRepo.findAll());
        model.addAttribute("operator", operatorRepo.findByUserUsername(authentication.getUsername()));
        return "tickets/home";
    }

    @GetMapping("/create")
    public String create(Model model, RedirectAttributes redirectAttributes) {

        List<Operator> avaliableOperators = operatorRepo.findByState(true);
        if (avaliableOperators.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Can't Create Ticket because no Operators avaliable");
            return "redirect:/tickets";
        }
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryRepo.findAll());

        return "tickets/create";
    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        List<Operator> avaliableOperators = operatorRepo.findByState(true);
        List<Operator> avaliableOperatorsOrdered = new ArrayList(avaliableOperators);

        for (int i = 0; i < avaliableOperators.size() - 1; i++) {
            for (int j = 0; j < avaliableOperators.size() - 1 - i; j++) {
                if (avaliableOperatorsOrdered.get(j).getTickets().size() > avaliableOperatorsOrdered.get(j + 1)
                        .getTickets().size()) {

                    Operator temp = avaliableOperatorsOrdered.get(j);
                    avaliableOperatorsOrdered.set(j, avaliableOperatorsOrdered.get(j + 1));
                    avaliableOperatorsOrdered.set(j + 1, temp);
                }
            }
        }

        model.addAttribute("categories", categoryRepo.findAll());
        if (ticketForm.getCategory() == null) {
            bindingResult.addError(new ObjectError("category", "Insert Category"));
        }
        if (bindingResult.hasErrors()) {
            return "tickets/create";
        }

        ticketForm.setOperator(avaliableOperatorsOrdered.get(0));
        ticketRepo.save(ticketForm);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket created succesfully");

        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes, Ticket ticketForm,
            BindingResult bindingResult) {

        model.addAttribute("ticket", ticketRepo.findById(id));
        model.addAttribute("categories", categoryRepo.findAll());

        return "tickets/edit";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("categories", categoryRepo.findAll());
        if (ticketForm.getCategory() == null) {
            bindingResult.addError(new ObjectError("Category not Found", "Insert Category"));
        }
        if (bindingResult.hasErrors()) {
            return "tickets/edit";
        }
        ticketForm.setNotes(noteRepo.findByTicket(ticketForm));
        ticketRepo.save(ticketForm);
        redirectAttributes.addFlashAttribute("editMessage", "Ticket updated succesfully");
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
        model.addAttribute("operators", operatorRepo.findAll());
        model.addAttribute("users", userRepo.findAll());
        Note note = new Note();
        note.setTicket(ticketRepo.findById(id).get());
        model.addAttribute("note", note);

        return "notes/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("deleteMessage",
                "Ticket : " + ticketRepo.findById(id).get().getObject() + " deleted succesfully");
        noteRepo.deleteAll(ticketRepo.findById(id).get().getNotes());
        ticketRepo.deleteById(id);
        return "redirect:/tickets";
    }

    @GetMapping("/state/{id}/{value}")
    public String state(@PathVariable Long id, @PathVariable int value, RedirectAttributes redirectAttributes) {

        Ticket updatedTicket = ticketRepo.findById(id).get();
        switch (value) {

            case 1:
                updatedTicket.setState("In progress");
                break;

            case 2:
                updatedTicket.setState("Completed");
                break;
        }

        ticketRepo.save(updatedTicket);
        redirectAttributes.addFlashAttribute("stateMessage",
                "State of ticket: " + ticketRepo.findById(id).get().getObject() + " changed to \""
                        + ticketRepo.findById(id).get().getState() + "\" succesfully");

        return "redirect:/tickets";
    }

}
