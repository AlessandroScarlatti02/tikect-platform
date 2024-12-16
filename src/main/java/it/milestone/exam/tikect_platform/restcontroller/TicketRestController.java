package it.milestone.exam.tikect_platform.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.milestone.exam.tikect_platform.model.Ticket;
import it.milestone.exam.tikect_platform.repository.TicketRepository;
import it.milestone.exam.tikect_platform.response.model.Payload;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketRestController {

    @Autowired
    TicketRepository ticketRepo;

    @GetMapping()
    public ResponseEntity<Payload<List<Ticket>>> all() {

        return ResponseEntity.ok(new Payload<List<Ticket>>("OK", "200", ticketRepo.findAll()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Payload<List<Ticket>>> status(@PathVariable String status) {

        try {
            List<Ticket> tickets = ticketRepo.findByState(status);
            if (!tickets.isEmpty()) {
                return ResponseEntity.ok(new Payload<List<Ticket>>("OK", "200", ticketRepo.findByState(status)));
            }
            throw new Exception("Status not Found");
        } catch (Exception e) {
            return new ResponseEntity<Payload<List<Ticket>>>(new Payload<List<Ticket>>(e.getMessage(), "404", null),
                    HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Payload<List<Ticket>>> category(@PathVariable String category) {

        try {
            List<Ticket> tickets = ticketRepo.findByCategoryName(category);
            if (!tickets.isEmpty()) {
                return ResponseEntity.ok(new Payload<List<Ticket>>("OK", "200", ticketRepo.findByState(category)));
            }
            throw new Exception("Category not Found");
        } catch (Exception e) {
            return new ResponseEntity<Payload<List<Ticket>>>(new Payload<List<Ticket>>(e.getMessage(), "404", null),
                    HttpStatus.NOT_FOUND);
        }

    }

}
