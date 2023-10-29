package cyy.greenblue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OAuthRedirectTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOAuthRedirect() throws Exception {
        // 시뮬레이션된 OAuth 로그인 요청 (예: Google OAuth)
        mockMvc.perform(MockMvcRequestBuilders.get("/oauth2/authorization/google"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("https://accounts.google.com/**"));
    }
}
