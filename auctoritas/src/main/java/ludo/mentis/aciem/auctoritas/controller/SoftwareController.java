package ludo.mentis.aciem.auctoritas.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.service.SoftwareService;
import ludo.mentis.aciem.auctoritas.util.ReferencedWarning;
import ludo.mentis.aciem.auctoritas.util.UserRoles;
import ludo.mentis.aciem.auctoritas.util.WebUtils;


@Controller
@RequestMapping("/softwares")
public class SoftwareController {

    private final SoftwareService softwareService;

    public SoftwareController(final SoftwareService softwareService) {
        this.softwareService = softwareService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "', '" + UserRoles.ROLE_SOFTWARE_READ + "')")
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final Page<SoftwareDTO> softwares = softwareService.findAll(filter, pageable);
        model.addAttribute("softwares", softwares);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(softwares));
        return "software/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String add(@ModelAttribute("software") final SoftwareDTO softwareDTO) {
        return "software/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String add(@ModelAttribute("software") @Valid final SoftwareDTO softwareDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "software/add";
        }
        softwareService.create(softwareDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("software.create.success"));
        return "redirect:/softwares";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("software", softwareService.get(id));
        return "software/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("software") @Valid final SoftwareDTO softwareDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "software/edit";
        }
        softwareService.update(id, softwareDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("software.update.success"));
        return "redirect:/softwares";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = softwareService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            softwareService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("software.delete.success"));
        }
        return "redirect:/softwares";
    }

}
