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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Operator;
import it.milestone.exam.tikect_platform.repository.OperatorRepository;
import it.milestone.exam.tikect_platform.repository.UserRepository;
import it.milestone.exam.tikect_platform.security.DatabaseUserDetails;
import jakarta.validation.Valid;
import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.model.User;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    OperatorRepository operatorRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("operators", operatorRepo.findAll());

        return "operators/index";
    }

    @GetMapping("/state/{value}")
    public String state(@PathVariable int value, RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal DatabaseUserDetails authenticate) {

        Operator operator = operatorRepo.findByUserUsername(authenticate.getUsername());
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : operator.getTickets()) {
            if (!ticket.getState().equals("Completed")) {
                tickets.add(ticket);
            }
        }
        switch (value) {

            case 0:
                if (tickets.isEmpty()) {
                    operator.setState(false);
                    redirectAttributes.addFlashAttribute("stateOperatorSuccessMessage", "State Changed Succesfully");
                } else {
                    redirectAttributes.addFlashAttribute("stateOperatorErrorMessage",
                            "Can't Change status because you have tickets not completed");
                }
                break;
            case 1:
                operator.setState(true);
                redirectAttributes.addFlashAttribute("stateOperatorSuccessMessage", "State Changed Succesfully");
                break;

        }
        operatorRepo.save(operator);

        return "redirect:/tickets";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("operator", new Operator());

        return "operators/create";

    }

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("operator") Operator operatorForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        User user = new User();
        user.setMail(operatorForm.getName().toLowerCase().trim() + "." + operatorForm.getSurname().toLowerCase().trim()
                + "@ticketplatform.com");

        List<User> optionalList = userRepo.findByMail(user.getMail());

        if (!optionalList.isEmpty()) {
            bindingResult.addError(new ObjectError("duplicate", "User already exists"));
        }

        if (bindingResult.hasErrors()) {
            return "operators/create";
        }

        user.setUsername(operatorForm.getName().toLowerCase().trim());
        user.setPassword("{noop}" + operatorForm.getSurname().toLowerCase().trim());

        userRepo.save(user);

        operatorForm.setState(false);
        operatorForm.setUser(user);
        operatorRepo.save(operatorForm);

        return "redirect:/operator";
    }

}
