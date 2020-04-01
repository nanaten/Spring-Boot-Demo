package com.nanaten.SpringBootDemo.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
internal class AdminControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var adminController: AdminController

    @Test
    fun noAuthenticationTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/index"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "admin")
    fun authenticationTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/index"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("page"))
                .andExpect(view().name("admin_index"))
                .andExpect(model().attributeExists("isAdmin"))
    }

    @Test
    @WithMockUser(username = "admin")
    fun singleDeleteNotExistsArticle() {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/article/delete/0")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/admin/index"))
                .andExpect(flash().attributeExists(adminController.MESSAGE))
                .andExpect(flash().attribute(adminController.MESSAGE, adminController.MESSAGE_ARTICLE_DOES_NOT_EXISTS))
    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key) VALUES ('test', 'test', 'test', 'test');"])
    @WithMockUser(username = "admin")
    fun singleDeleteExistsArticle() {
        val latestArticle = adminController.articleRepository.findAll().last()

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/article/delete/" + latestArticle.id)
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/admin/index"))
                .andExpect(flash().attributeExists(adminController.MESSAGE))
                .andExpect(flash().attribute(adminController.MESSAGE, adminController.MESSAGE_DELETE_NORMAL))
    }
}