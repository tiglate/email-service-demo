package ludo.mentis.aciem.auctoritas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ludo.mentis.aciem.auctoritas.model.AuthenticationRequest;
import ludo.mentis.aciem.auctoritas.util.WebUtils;


@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) final Boolean loginRequired,
            @RequestParam(required = false) final Boolean loginError,
            final Model model) {
        model.addAttribute("authentication", new AuthenticationRequest());
        if (loginRequired != null && loginRequired.equals(Boolean.TRUE)) {
            model.addAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("authentication.login.required"));
        }
        if (loginError != null && loginError.equals(Boolean.TRUE)) {
            model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.login.error"));
        }
        return "authentication/login";
    }

}
