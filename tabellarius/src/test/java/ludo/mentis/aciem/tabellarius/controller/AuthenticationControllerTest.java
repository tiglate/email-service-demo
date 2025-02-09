package ludo.mentis.aciem.tabellarius.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/login"))
                .andExpect(model().attributeExists("authentication"));
    }

    @Test
    void testLoginWithLoginRequired() throws Exception {
        mockMvc.perform(get("/login").param("loginRequired", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/login"))
                .andExpect(model().attributeExists("authentication"))
                .andExpect(model().attribute("MSG_INFO", "Please login to access this area."));
    }

    @Test
    void testLoginWithLoginError() throws Exception {
        mockMvc.perform(get("/login").param("loginError", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/login"))
                .andExpect(model().attributeExists("authentication"))
                .andExpect(model().attribute("MSG_ERROR", "Your login was not successful - please try again."));
    }
}