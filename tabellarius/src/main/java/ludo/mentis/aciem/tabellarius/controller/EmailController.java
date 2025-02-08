package ludo.mentis.aciem.tabellarius.controller;

import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.service.EmailService;
import ludo.mentis.aciem.tabellarius.util.LogHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ROLE_SEND_EMAIL')")
    public ResponseEntity<String> send(@Valid @RequestBody MessageDTO messageDTO, HttpServletRequest request) {
        LogHelper.traceMethodCall(EmailController.class,"send", messageDTO, request);
        var senderIp = request.getRemoteAddr();
        var result = emailService.send(messageDTO, senderIp);
        if (result) {
            return ResponseEntity.ok("Email sent successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }
}