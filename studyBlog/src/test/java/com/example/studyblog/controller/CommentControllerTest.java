package com.example.studyblog.controller;

import com.example.studyblog.controller.request.CommentRequestDto;
import com.example.studyblog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentControllerTest.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private CommentService commentService;

    static MockHttpServletRequest request = new MockHttpServletRequest();


    @Test
    void createComment() throws Exception{
//        mockMvc.perform(post("/api/comment/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new CommentRequestDto("test"))))
//                .andExpect(status().isOk());
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }

    @Test
    void getComment() {
    }
}