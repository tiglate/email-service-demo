package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.commons.web.FlashMessages;
import ludo.mentis.aciem.commons.web.GlobalizationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    private final GlobalizationUtils globalizationUtils;

    public HomeController(GlobalizationUtils globalizationUtils) {
        this.globalizationUtils = globalizationUtils;
    }

    @GetMapping("/")
	public final String index(
            @RequestParam(required = false) final Boolean logoutSuccess,
            final Model model) {
        if (logoutSuccess != null && logoutSuccess.equals(Boolean.TRUE)) {
            model.addAttribute(FlashMessages.MSG_INFO,
                    globalizationUtils.getMessage("authentication.logout.success"));
        }
        return "home/index";
    }

}
