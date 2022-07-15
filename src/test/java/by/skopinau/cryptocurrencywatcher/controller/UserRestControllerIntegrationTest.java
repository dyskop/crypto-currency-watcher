package by.skopinau.cryptocurrencywatcher.controller;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.dal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
    }

    @Test
    public void givenCreatedUser_whenNotify_thenReturnSavedUserObject()
            throws Exception {
        // given
        User user = userRepository.save(new User("dmitry", "BTC", 123.12));

        // when
        ResultActions response = mockMvc.perform(post("/api/users")
                        .param("username", user.getUsername())
                        .param("symbol", user.getSymbol())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.symbol", is(user.getSymbol())));
    }

    @Test
    public void givenCreatedUser_whenNotify_thenReturnEmptyObject()
            throws Exception {
        // given
        User user = userRepository.save(new User("dmitry", "AAAAA", 123.12));

        // when
        ResultActions response = mockMvc.perform(post("/api/users")
                .param("username", user.getUsername())
                .param("symbol", user.getSymbol())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest());
    }
}