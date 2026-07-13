package com.librarymanagement.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagement.backend.dto.BookRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookShouldReturnCreated() throws Exception {
        BookRequest request = BookRequest.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("9781234567890")
                .category("Test")
                .publisher("Test Publisher")
                .publishedYear(2024)
                .language("English")
                .totalCopies(2)
                .build();

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void getAllBooksShouldReturnOk() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }
}
