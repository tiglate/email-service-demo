package ludo.mentis.aciem.auctoritas.controller;

import javax.validation.Valid;

import ludo.mentis.aciem.commons.web.CustomCollectors;
import ludo.mentis.aciem.commons.web.FlashMessages;
import ludo.mentis.aciem.commons.web.GlobalizationUtils;
import ludo.mentis.aciem.commons.web.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import ludo.mentis.aciem.auctoritas.domain.Role;
import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.repos.RoleRepository;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import ludo.mentis.aciem.auctoritas.service.UserCrudService;
import ludo.mentis.aciem.auctoritas.util.UserRoles;


@Controller
@RequestMapping("/users")
public class UserController {

    private static final String REDIRECT_USERS = "redirect:/users";
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
                .collect(CustomCollectors.toSortedMap(Role::getId, Role::getCode)));
        model.addAttribute("softwareValues", softwareRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Software::getId, Software::getName)));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + UserRoles.ROLE_USER_WRITE + "', '" + UserRoles.ROLE_USER_READ + "')")
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final Page<UserDTO> users = userService.findAll(filter, pageable);
        model.addAttribute("users", users);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", PaginationUtils.getPaginationModel(users));
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
        redirectAttributes.addFlashAttribute(FlashMessages.MSG_SUCCESS, GlobalizationUtils.getMessage("user.create.success"));
        return REDIRECT_USERS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("user", userService.get(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        userService.update(id, userDTO);
        redirectAttributes.addFlashAttribute(FlashMessages.MSG_SUCCESS, GlobalizationUtils.getMessage("user.update.success"));
        return REDIRECT_USERS;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('" + UserRoles.ROLE_USER_WRITE + "')")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute(FlashMessages.MSG_INFO, GlobalizationUtils.getMessage("user.delete.success"));
        return REDIRECT_USERS;
    }

}
