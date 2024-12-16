package it.milestone.exam.tikect_platform.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.milestone.exam.tikect_platform.model.Operator;
import it.milestone.exam.tikect_platform.repository.OperatorRepository;
import it.milestone.exam.tikect_platform.security.DatabaseUserDetails;
import it.milestone.exam.tikect_platform.model.Ticket;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    OperatorRepository operatorRepo;

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

}
