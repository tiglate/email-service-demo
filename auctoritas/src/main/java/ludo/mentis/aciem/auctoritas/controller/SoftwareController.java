package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.service.SoftwareService;
import ludo.mentis.aciem.auctoritas.util.*;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static java.util.Map.entry;


@Controller
@RequestMapping("/softwares")
public class SoftwareController {

    private static final String ENTITY_NAME = "Application";
    private static final String CONTROLLER_ADD = "software/add";
    private static final String CONTROLLER_EDIT = "software/edit";
    private static final String CONTROLLER_VIEW = "software/view";
    private static final String CONTROLLER_LIST = "software/list";
    private static final String REDIRECT_TO_CONTROLLER_INDEX = "redirect:/softwares";
    private final SoftwareService softwareService;
    private final SortUtils sortUtils;

    public SoftwareController(final SoftwareService softwareService) {
        this.softwareService = softwareService;
        this.sortUtils = new SortUtils();
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_READ + "')")
    public String list(@ModelAttribute("softwareSearch") SoftwareDTO filter,
                       @RequestParam(name = "sort", required = false) String sort,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final var sortOrder = this.sortUtils.addSortAttributesToModel(model, sort, pageable, Map.ofEntries(
                entry("id", "sortById"),
                entry("code", "sortByCode"),
                entry("name", "sortByName")
        ));
        final var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
        final var softwares = softwareService.findAll(filter, pageRequest);
        model.addAttribute("softwares", softwares);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(softwares));
        return CONTROLLER_LIST;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_READ + "')")
    public String view(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("software", softwareService.get(id));
        return CONTROLLER_VIEW;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String add(@ModelAttribute("software") final SoftwareDTO softwareDTO) {
        return CONTROLLER_ADD;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String add(@ModelAttribute("software") @Valid final SoftwareDTO softwareDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return CONTROLLER_ADD;
        }
        softwareService.create(softwareDTO);
        FlashMessages.createSuccess(redirectAttributes, ENTITY_NAME);
        return REDIRECT_TO_CONTROLLER_INDEX;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("software", softwareService.get(id));
        return CONTROLLER_EDIT;
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String edit(@PathVariable(name = "id") final Integer id,
                       @ModelAttribute("software") @Valid final SoftwareDTO softwareDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return CONTROLLER_EDIT;
        }
        softwareService.update(id, softwareDTO);
        FlashMessages.updateSuccess(redirectAttributes, ENTITY_NAME);
        return REDIRECT_TO_CONTROLLER_INDEX;
    }

    @SuppressWarnings("SameReturnValue")
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_SOFTWARE_WRITE + "')")
    public String delete(@PathVariable(name = "id") final Integer id,
                         final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = softwareService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            softwareService.delete(id);
            FlashMessages.deleteSuccess(redirectAttributes, ENTITY_NAME);
        }
        return REDIRECT_TO_CONTROLLER_INDEX;
    }

}
