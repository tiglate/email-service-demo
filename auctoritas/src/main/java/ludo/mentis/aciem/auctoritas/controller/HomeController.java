package ludo.mentis.aciem.auctoritas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ludo.mentis.aciem.auctoritas.util.WebUtils;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) final Boolean logoutSuccess,
            final Model model) {
        if (logoutSuccess != null && logoutSuccess.equals(Boolean.TRUE)) {
            model.addAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("authentication.logout.success"));
        }
        return "home/index";
    }

}
