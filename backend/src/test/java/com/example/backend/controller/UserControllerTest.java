package com.example.backend.controller;

import com.example.backend.model.UserModel;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private static final String URL_BASE = "/api/users";

    private static final String NAME_FIRST = "John";
    private static final String NAME_SECOND = "Jane";
    private static final String NAME_THIRD = "Jack";

    private static final String PASSWORD = "123456";

    private SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor mockUser() {
        return oidcLogin().userInfoToken(token -> token
                .claim("login", NAME_FIRST));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    void createUser_sucess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                              {
                                                "name": "%s",
                                                "password": "%s",
                                                "isGitHubUser": "%s"
                                              }
                                        """.formatted(NAME_FIRST, PASSWORD, true)
                        )
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk());
        List<UserModel> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals(NAME_FIRST, users.getFirst().getName());
    }

    @Test
    @DirtiesContext
    void createUser_fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                              {
                                                "name": null,
                                                "password": "%s",
                                                "isGitHubUser": "%s"
                                              }
                                        """.formatted(PASSWORD, true)
                        )
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        List<UserModel> actualUsers = userRepository.findAll();
        List<UserModel> expectedUsers = List.of();
        assertEquals(actualUsers, expectedUsers);
    }

    @Test
    @DirtiesContext
    void getAllUsers() throws Exception {
        userRepository.saveAll(
                List.of(
                        UserModel.builder().name(NAME_FIRST).build(),
                        UserModel.builder().name(NAME_SECOND).build()
                )
        );
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(NAME_FIRST))
                .andExpect(jsonPath("$[1].name").value(NAME_SECOND));

    }

    @Test
    @DirtiesContext
    void getUser_byId_sucess() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/" + firstUser.getId()).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(NAME_FIRST))
                .andExpect(jsonPath("$.id").value(firstUser.getId()));
    }

    @Test
    @DirtiesContext
    void getUser_by_NonExistingId() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/" + secondUser.getId())
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DirtiesContext
    void getUser_byName_sucess() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/find_by_name/" + firstUser.getName()).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(NAME_FIRST))
                .andExpect(jsonPath("$.id").value(firstUser.getId()));
    }

    @Test
    @DirtiesContext
    void getUser_byName_NonExistantName() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser));
        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/find_by_name/" + secondUser.getName())
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @DirtiesContext
    void updateUser_sucess() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/" + firstUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                              {
                                                "name": "%s",
                                                "password": "%s",
                                                "isGitHubUser": "%s"
                                              }
                                        """.formatted(NAME_THIRD, PASSWORD, true)
                        )
                        .with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(NAME_THIRD))
                .andExpect(jsonPath("$.id").value(firstUser.getId()));
    }

    @Test
    @DirtiesContext
    void updateUser_nonExistingId() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_BASE + "/" + "12345678WWW")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                      {
                                        "name": "%s",
                                        "password": "%s",
                                        "isGitHubUser": "%s"
                                      }
                                """.formatted(NAME_THIRD, PASSWORD, true)
                )
                .with(mockUser())).andExpect(MockMvcResultMatchers.status().is4xxClientError());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(NAME_FIRST))
                .andExpect(jsonPath("$[1].name").value(NAME_SECOND));
    }

    @Test
    @DirtiesContext
    void deleteUser_sucess() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/" + firstUser.getId())
                .with(mockUser())).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(NAME_SECOND));
    }

    @Test
    @DirtiesContext
    void deleteUser_NonExistingId() throws Exception {
        UserModel firstUser = UserModel.builder().name(NAME_FIRST).build();
        UserModel secondUser = UserModel.builder().name(NAME_SECOND).build();

        userRepository.saveAll(List.of(firstUser, secondUser));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_BASE + "/" + "A12345678WWW")
                .with(mockUser())).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_BASE).with(mockUser()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(NAME_FIRST))
                .andExpect(jsonPath("$[1].name").value(NAME_SECOND));
    }

}