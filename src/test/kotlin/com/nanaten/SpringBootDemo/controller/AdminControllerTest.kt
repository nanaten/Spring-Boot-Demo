package com.nanaten.SpringBootDemo.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
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
}