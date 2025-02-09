package ludo.mentis.aciem.tesserarius.controller;

import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import ludo.mentis.aciem.tesserarius.client.EmailClient;
import ludo.mentis.aciem.tesserarius.model.AttachmentDTO;
import ludo.mentis.aciem.tesserarius.model.BodyType;
import ludo.mentis.aciem.tesserarius.model.MessageDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
public class HomeController {

    public static final String CONTENT = "content";
    public static final String CONTENT_TILE = "contentTile";
    private final OAuthService oauthService;
    private final EmailClient emailClient;

    public HomeController(EmailClient emailClient, OAuthService oauthService) {
        this.emailClient = emailClient;
        this.oauthService = oauthService;
    }

    @GetMapping("/")
    public String index(Model model) throws PublicKeyException {
        model.addAttribute(CONTENT_TILE, "Public Key");
        model.addAttribute(CONTENT, Base64.getEncoder().encodeToString(oauthService.getPublicKey().getEncoded()));
        return "home/index";
    }

    @PostMapping("/")
    public String send(Model model) {
        model.addAttribute(CONTENT_TILE, "E-mail Result");

        try {
            var message = getMessage();
            emailClient.send(message);
            model.addAttribute(CONTENT, "E-mail sent successfully.");
        }
        catch (Exception e) {
            model.addAttribute(CONTENT, "Failed to send e-mail: " + e.getMessage());
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
