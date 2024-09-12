package org.justme.articlestest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.justme.articlestest.data.repository.UserRepository;
import org.justme.articlestest.security.controller.AuthController;
import org.justme.articlestest.security.dto.LoginRequest;
import org.justme.articlestest.security.dto.RegisterRequest;
import org.justme.articlestest.services.articles.dto.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleAppTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthController authController;
    @Resource
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @TestTemplate
    private ResultActions registerTemplate(RegisterRequest request, String additional) throws Exception {
        return mockMvc.perform(post(String.format("/api/auth/register%s",additional))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @TestTemplate
    private ResultActions loginTemplate(LoginRequest request) throws Exception {
        return mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Test
    public void testUserRegistrationLoginAndFailures() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("testuser", "password");
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        // Проверка регистрации пользователя

        //Создание новой записи
        registerTemplate(registerRequest,"")
                .andExpect(status().isCreated());

        //Попытка создание с занятым именем пользователя
        registerTemplate(registerRequest,"")
                .andExpect(status().isBadRequest());

        // Проверка входа в учетную запись

        //Успешная попытка
        String responseBody = loginTemplate(loginRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn().getResponse().getContentAsString();
        // С вводом неверных данных
        LoginRequest wrongLoginRequest = new LoginRequest("wronguser", "wrongpassword");
        loginTemplate(wrongLoginRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testArticleCreationAndValidation() throws Exception {

        //Регистрация и вход

        RegisterRequest registerUserRequest = new RegisterRequest("testuser", "password");
        LoginRequest loginUserRequest = new LoginRequest("testuser", "password");

        RegisterRequest registerAdminRequest = new RegisterRequest("testAdmin", "password");
        LoginRequest loginAdminRequest = new LoginRequest("testAdmin", "password");

        registerTemplate(registerUserRequest, "").andExpect(status().isCreated());
        registerTemplate(registerAdminRequest, "/admin").andExpect(status().isCreated());

        String userTokenResponse = loginTemplate(loginUserRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn().getResponse().getContentAsString();
        String userToken = objectMapper.readTree(userTokenResponse).get("accessToken").asText();
        System.out.println("User Token: " + userToken);

        String adminTokenResponse = loginTemplate(loginAdminRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn().getResponse().getContentAsString();
        String adminToken = objectMapper.readTree(adminTokenResponse).get("accessToken").asText();
        System.out.println("Admin Token: " + adminToken);

        // Создание статьи без входа
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ArticleDTO("Title", "Author", "Content", LocalDate.now())))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Создание статьи с аутентификацией
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ArticleDTO("Title", "Author", "Content", LocalDate.now())))
                        .header("Authorization", "Bearer " + userToken)
                        .with(csrf()))
                .andExpect(status().isOk());

        // Просмотр статистики без прав доступа
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/statistics")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        // Просмотр статистики как админ
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/statistics")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}
