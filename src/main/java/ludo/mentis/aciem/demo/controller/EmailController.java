package ludo.mentis.aciem.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ludo.mentis.aciem.demo.model.MessageDTO;
import ludo.mentis.aciem.demo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@Valid @RequestBody MessageDTO messageDTO, HttpServletRequest request) {
        var senderIp = request.getRemoteAddr();
        var result = emailService.send(messageDTO, senderIp);
        if (result) {
            return ResponseEntity.ok("Email sent successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }
}