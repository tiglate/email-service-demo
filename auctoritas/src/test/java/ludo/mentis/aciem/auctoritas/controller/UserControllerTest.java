package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.auctoritas.AuctoritasApplication;
import ludo.mentis.aciem.auctoritas.config.ControllerConfig;
import ludo.mentis.aciem.auctoritas.config.DomainConfig;
import ludo.mentis.aciem.auctoritas.config.WebConfig;
import ludo.mentis.aciem.auctoritas.model.UserDTO;
import ludo.mentis.aciem.auctoritas.security.KeyConfig;
import ludo.mentis.aciem.auctoritas.security.SecurityConfig;
import ludo.mentis.aciem.auctoritas.service.UserCrudService;
import ludo.mentis.aciem.auctoritas.util.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest(
        classes = {
                AuctoritasApplication.class,
                ControllerConfig.class,
                DomainConfig.class,
                WebConfig.class,
                SecurityConfig.class,
                KeyConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserCrudService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_READ})
    void testList() throws Exception {
        when(userService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(new UserDTO())));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("paginationModel"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testAddGet() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testAddPostWithErrors() throws Exception {
        var user = new UserDTO();
        mockMvc.perform(post("/users/add").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testAddPostWithoutErrors() throws Exception {
        var user = createValidUserDTO();
        mockMvc.perform(post("/users/add").flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testEditGet() throws Exception {
        var user = createValidUserDTO();
        when(userService.get(anyInt())).thenReturn(user);
        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testEditPostWithErrors() throws Exception {
        var user = new UserDTO();
        mockMvc.perform(post("/users/edit/1").flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testEditPostWithoutErrors() throws Exception {
        var user = createValidUserDTO();
        mockMvc.perform(post("/users/edit/1").flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_USER_WRITE})
    void testDelete() throws Exception {
        mockMvc.perform(post("/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    private UserDTO createValidUserDTO() {
        var user = new UserDTO();
        user.setUsername("user");
        user.setPassword("password");
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setRoles(List.of(1, 2));
        return user;
    }
}