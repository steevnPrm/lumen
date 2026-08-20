package steevnPrm.lumen.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import steevnPrm.lumen.authentification.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getProfile_withoutToken_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getProfile_withToken_returnsProfileWithoutEmail() throws Exception {
        User user = new User("auth0|abc123");
        user.setUsername("jdoe");
        user.setFirstname("Jane");
        user.setLastname("Doe");
        when(userService.getOrCreate("auth0|abc123")).thenReturn(user);

        mockMvc.perform(get("/api/users/me").with(jwt().jwt(jwtBuilder -> jwtBuilder.subject("auth0|abc123"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("jdoe"))
            .andExpect(jsonPath("$.firstname").value("Jane"))
            .andExpect(jsonPath("$.lastname").value("Doe"))
            .andExpect(jsonPath("$.email").doesNotExist());
    }

    @Test
    void updateProfile_withInvalidBody_isBadRequest() throws Exception {
        mockMvc.perform(put("/api/users/me")
                .with(jwt().jwt(jwtBuilder -> jwtBuilder.subject("auth0|abc123")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"firstname\":\"\",\"lastname\":\"\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_withValidBody_updatesAndReturnsProfile() throws Exception {
        User updated = new User("auth0|abc123");
        updated.setUsername("newname");
        updated.setFirstname("New");
        updated.setLastname("Name");
        when(userService.updateProfile(eq("auth0|abc123"), any())).thenReturn(updated);

        mockMvc.perform(put("/api/users/me")
                .with(jwt().jwt(jwtBuilder -> jwtBuilder.subject("auth0|abc123")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newname\",\"firstname\":\"New\",\"lastname\":\"Name\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newname"))
            .andExpect(jsonPath("$.email").doesNotExist());
    }
}
