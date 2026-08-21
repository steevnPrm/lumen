package steevnPrm.lumen.visual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import steevnPrm.lumen.authentification.SecurityConfig;
import steevnPrm.lumen.user.User;
import steevnPrm.lumen.user.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisualController.class)
@Import(SecurityConfig.class)
class VisualControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisualService visualService;

    @MockitoBean
    private UserService userService;

    private static final String SUBJECT = "auth0|abc123";

    @Test
    void list_withoutToken_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me/visuals"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void list_withToken_returnsOnlyCurrentUsersVisuals() throws Exception {
        User user = new User(SUBJECT);
        UserVisual visual = new UserVisual(user, "users/1/abc.png", "avatar.png", "image/png", 42L);
        when(userService.getOrCreate(SUBJECT)).thenReturn(user);
        when(visualService.listForUser(user)).thenReturn(List.of(visual));
        when(visualService.urlFor(visual)).thenReturn("https://minio.local/signed-url");

        mockMvc.perform(get("/api/users/me/visuals").with(jwt().jwt(jwtBuilder -> jwtBuilder.subject(SUBJECT))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].url").value("https://minio.local/signed-url"))
            .andExpect(jsonPath("$[0].contentType").value("image/png"));
    }

    @Test
    void upload_withImageFile_returnsCreatedVisual() throws Exception {
        User user = new User(SUBJECT);
        UserVisual visual = new UserVisual(user, "users/1/abc.png", "avatar.png", "image/png", 4L);
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", "data".getBytes());
        when(userService.getOrCreate(SUBJECT)).thenReturn(user);
        when(visualService.upload(eq(user), any())).thenReturn(visual);
        when(visualService.urlFor(visual)).thenReturn("https://minio.local/signed-url");

        mockMvc.perform(multipart("/api/users/me/visuals").file(file).with(jwt().jwt(jwtBuilder -> jwtBuilder.subject(SUBJECT))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.url").value("https://minio.local/signed-url"));
    }

    @Test
    void delete_withOwnedVisual_returnsNoContent() throws Exception {
        User user = new User(SUBJECT);
        when(userService.getOrCreate(SUBJECT)).thenReturn(user);

        mockMvc.perform(delete("/api/users/me/visuals/1").with(jwt().jwt(jwtBuilder -> jwtBuilder.subject(SUBJECT))))
            .andExpect(status().isNoContent());
    }

    @Test
    void delete_withVisualNotOwnedByUser_returnsNotFound() throws Exception {
        User user = new User(SUBJECT);
        when(userService.getOrCreate(SUBJECT)).thenReturn(user);
        org.mockito.Mockito.doThrow(new VisualNotFoundException(1L)).when(visualService).delete(user, 1L);

        mockMvc.perform(delete("/api/users/me/visuals/1").with(jwt().jwt(jwtBuilder -> jwtBuilder.subject(SUBJECT))))
            .andExpect(status().isNotFound());
    }
}
