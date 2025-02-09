package ludo.mentis.aciem.tesserarius.controller;

import ludo.mentis.aciem.commons.security.service.OAuthService;
import ludo.mentis.aciem.tesserarius.client.EmailClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailClient emailClient;

    @Mock
    private OAuthService oauthService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testIndex() throws Exception {
        when(oauthService.getPublicKey()).thenReturn(java.security.KeyPairGenerator.getInstance("RSA").generateKeyPair().getPublic());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attributeExists("contentTile"))
                .andExpect(model().attributeExists("content"));
    }

    @Test
    void testSendSuccess() throws Exception {
        mockMvc.perform(post("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("contentTile", "E-mail Result"))
                .andExpect(model().attribute("content", "E-mail sent successfully."));
    }

    @Test
    void testSendFailure() throws Exception {
        when(emailClient.send(any())).thenThrow(new RuntimeException("Failed to send e-mail"));

        mockMvc.perform(post("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(model().attribute("contentTile", "E-mail Result"))
                .andExpect(model().attribute("content", "Failed to send e-mail: Failed to send e-mail"));
    }
}