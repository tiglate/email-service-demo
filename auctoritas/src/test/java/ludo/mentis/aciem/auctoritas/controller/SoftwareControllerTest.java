package ludo.mentis.aciem.auctoritas.controller;

import ludo.mentis.aciem.auctoritas.AuctoritasApplication;
import ludo.mentis.aciem.auctoritas.config.ControllerConfig;
import ludo.mentis.aciem.auctoritas.config.DomainConfig;
import ludo.mentis.aciem.auctoritas.config.WebConfig;
import ludo.mentis.aciem.auctoritas.model.SoftwareDTO;
import ludo.mentis.aciem.auctoritas.security.KeyConfig;
import ludo.mentis.aciem.auctoritas.security.SecurityConfig;
import ludo.mentis.aciem.auctoritas.service.SoftwareService;
import ludo.mentis.aciem.auctoritas.util.ReferencedWarning;
import ludo.mentis.aciem.auctoritas.util.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
class SoftwareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoftwareService softwareService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_READ})
    void testList() throws Exception {
        when(softwareService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(new SoftwareDTO())));

        mockMvc.perform(get("/softwares"))
               .andExpect(status().isOk())
               .andExpect(view().name("software/list"))
               .andExpect(model().attributeExists("softwares"))
               .andExpect(model().attributeExists("paginationModel"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testAddGet() throws Exception {
        mockMvc.perform(get("/softwares/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("software/add"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testAddPostWithErrors() throws Exception {
        var software = new SoftwareDTO();
        mockMvc.perform(post("/softwares/add").flashAttr("software", software))
               .andExpect(status().isOk())
               .andExpect(view().name("software/add"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testAddPostWithoutErrors() throws Exception {
        var software = new SoftwareDTO();
        software.setCode("ABC");
        software.setName("Dummy Software");
        mockMvc.perform(post("/softwares/add").flashAttr("software", software))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/softwares"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testEditGet() throws Exception {
        var software = new SoftwareDTO();
        software.setCode("ABC");
        software.setName("Dummy Software");
        when(softwareService.get(anyInt())).thenReturn(software);
        mockMvc.perform(get("/softwares/edit/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("software/edit"))
               .andExpect(model().attributeExists("software"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testEditPostWithErrors() throws Exception {
        var software = new SoftwareDTO();
        mockMvc.perform(post("/softwares/edit/1").flashAttr("software", software))
               .andExpect(status().isOk())
               .andExpect(view().name("software/edit"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testEditPostWithoutErrors() throws Exception {
        var software = new SoftwareDTO();
        software.setCode("ABC");
        software.setName("Dummy Software");
        mockMvc.perform(post("/softwares/edit/1").flashAttr("software", software))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/softwares"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testDelete() throws Exception {
        mockMvc.perform(post("/softwares/delete/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/softwares"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {UserRoles.ROLE_SOFTWARE_WRITE})
    void testDeleteWithReferencedWarning() throws Exception {
        var warning = new ReferencedWarning();
        warning.setKey("key");
        warning.addParam("param");
        when(softwareService.getReferencedWarning(anyInt())).thenReturn(warning);

        mockMvc.perform(post("/softwares/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/softwares"));
    }
}