package com.nanaten.SpringBootDemo.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
internal class ArticleControllerTest {

    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var articleController: ArticleController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun registerArticle() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/")
                                .param("name", "test")
                                .param("title", "test")
                                .param("contents", "test")
                                .param("articleKey", "test")
                )
                .andExpect(status().isOk)
                .andExpect(content().string("Saved"))
    }

    @Test
    fun getAttributeList() {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/")
                )
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("articles"))
                .andExpect(view().name("index"))
    }
}