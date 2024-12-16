package it.milestone.exam.tikect_platform.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Note;
import it.milestone.exam.tikect_platform.repository.NoteRepository;
import it.milestone.exam.tikect_platform.repository.OperatorRepository;
import it.milestone.exam.tikect_platform.repository.UserRepository;
import it.milestone.exam.tikect_platform.security.DatabaseUserDetails;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteRepository noteRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    OperatorRepository operatorRepo;

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute(name = "note") Note noteForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model,
            @AuthenticationPrincipal DatabaseUserDetails authenticate) {

        noteForm.setCreationDate(LocalDate.now());

        if (bindingResult.hasErrors()) {
            model.addAttribute("ticket", noteForm.getTicket());
            model.addAttribute("notes", noteRepo.findAll());
            model.addAttribute("users", userRepo.findAll());
            return "notes/index";
        }
        noteForm.setAuthor(operatorRepo.findByUserUsername(authenticate.getUsername()));
        noteRepo.save(noteForm);
        redirectAttributes.addFlashAttribute("successMessage", "Note created succesfully!");
        return "redirect:/tickets/show/" + noteForm.getTicket().getId() + "/notes";
    }

}
