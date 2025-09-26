package ludo.mentis.aciem.tabellarius.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ludo.mentis.aciem.tabellarius.model.AttachmentDTO;
import ludo.mentis.aciem.tabellarius.model.MessageDTO;
import ludo.mentis.aciem.tabellarius.service.EmailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class PostmanController {

    private final EmailService emailService;

    public PostmanController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasAuthority('ROLE_SEND_EMAIL')")
    @GetMapping("/postman")
    public String showEmailForm(Model model) {
        model.addAttribute("messageDTO", new MessageDTO());
        return "postman/emailForm";
    }

    @PreAuthorize("hasAuthority('ROLE_SEND_EMAIL')")
    @PostMapping("/postman")
    public String sendEmail(@Valid @ModelAttribute MessageDTO messageDTO,
                            BindingResult bindingResult,
                            @RequestParam(required = false) List<MultipartFile> files,
                            HttpServletRequest request,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "postman/emailForm";
        }
        if (files != null && !files.isEmpty()) {
            var attachmentDTOs = files.stream()
                    .map(AttachmentDTO::new)
                    .toList();
            messageDTO.setAttachments(attachmentDTOs);
        }
        var senderIp = request.getRemoteAddr();
        var result = emailService.send(messageDTO, senderIp);
        if (result) {
            model.addAttribute("message", "Email sent successfully");
        } else {
            model.addAttribute("message", "Failed to send email");
        }
        return "postman/emailResult";
    }
}