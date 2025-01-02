package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.auctoritas.domain.Role;
import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.RoleRepository;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import ludo.mentis.aciem.auctoritas.service.UserCrudService;
import ludo.mentis.aciem.auctoritas.util.CustomCollectors;
import ludo.mentis.aciem.auctoritas.util.UserRoles;
import ludo.mentis.aciem.auctoritas.util.WebUtils;
import org.springframework.data.domain.Page;
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

import javax.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserCrudService userService;
    private final RoleRepository roleRepository;
    private final SoftwareRepository softwareRepository;

    public UserController(final UserCrudService userService,
                          final RoleRepository roleRepository,
                          final SoftwareRepository softwareRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.softwareRepository = softwareRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("rolesValues", roleRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getId, Role::getRole)));
        model.addAttribute("softwareValues", softwareRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Software::getId, Software::getName)));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_USER_WRITE + "', '" + UserRoles.ROLE_USER_READ + "')")
    public String list(@RequestParam(name = "filter", required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final Page<UserDTO> users = userService.findAll(filter, pageable);
        model.addAttribute("users", users);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(users));
        return "user/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String add(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String add(@ModelAttribute("user") @Valid final UserDTO userDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        userService.create(userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("user", userService.get(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String edit(@PathVariable(name = "id") final Integer id,
                       @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        userService.update(id, userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String delete(@PathVariable(name = "id") final Integer id,
                         final RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        return "redirect:/users";
    }

}
