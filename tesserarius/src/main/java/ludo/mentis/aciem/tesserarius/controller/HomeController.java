package ludo.mentis.aciem.tesserarius.controller;

import ludo.mentis.aciem.tesserarius.client.AuctoritasClient;
import ludo.mentis.aciem.tesserarius.client.EmailClient;
import ludo.mentis.aciem.tesserarius.model.AttachmentDTO;
import ludo.mentis.aciem.tesserarius.model.BodyType;
import ludo.mentis.aciem.tesserarius.model.MessageDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class HomeController {

    private final EmailClient emailClient;
    private final AuctoritasClient auctoritasClient;

    public HomeController(EmailClient emailClient, AuctoritasClient auctoritasClient) {
        this.emailClient = emailClient;
        this.auctoritasClient = auctoritasClient;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contentTile", "Public Key");
        model.addAttribute("content", auctoritasClient.getPublicKey().getContent());
        return "home/index";
    }

    @PostMapping("/")
    public String send(Model model) {
        model.addAttribute("contentTile", "E-mail Result");

        try {
            var message = getMessage();
            emailClient.send(message);
            model.addAttribute("content", "E-mail sent successfully.");
        }
        catch (Exception e) {
            model.addAttribute("content", "Failed to send e-mail: " + e.getMessage());
        }

        return "home/index";
    }

    private static MessageDTO getMessage() {
        var message = new MessageDTO();
        message.setRecipientsTo(List.of("example@example.com"));
        message.setRecipientsCc(List.of("cc@example.com"));
        message.setRecipientsBcc(List.of("bcc@example.com"));
        message.setSubject("Test Email");
        message.setBody("This is a test email body.");
        message.setBodyType(BodyType.TEXT);
        message.setAttachments(List.of(new AttachmentDTO("teste.txt", "plain/text", "teste".getBytes(StandardCharsets.UTF_8))));
        return message;
    }
}
