package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.auctoritas.AuctoritasApplication;
import ludo.mentis.aciem.auctoritas.config.ControllerConfig;
import ludo.mentis.aciem.auctoritas.config.DomainConfig;
import ludo.mentis.aciem.auctoritas.config.WebConfig;
import ludo.mentis.aciem.auctoritas.security.KeyConfig;
import ludo.mentis.aciem.auctoritas.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class HtmxErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testHtmxError() throws Exception {
        mockMvc.perform(get("/error").header("HX-Request", "true"))
               .andExpect(status().isOk());
    }
}