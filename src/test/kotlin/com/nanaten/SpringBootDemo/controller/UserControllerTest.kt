package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.UserRole
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun getUserSignup() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/signup"))
                .andExpect(status().isOk)
    }

    @Test
    fun postUserSignupNormal() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test1")
                .param("email", "test1@example.com")
                .param("password", "test1")
                .param("role", UserRole.USER.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))


    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test2', 'test2@example.com', 'test2', 'USER');"])
    fun postUserSignupRegisteredError() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test2")
                .param("email", "test2@example.com")
                .param("password", "test2")
                .param("role", UserRole.USER.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("request"))
    }

    @Test
    fun postUserSignupPasswordShort() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test2")
                .param("email", "test2@example.com")
                .param("password", "a")
                .param("role", UserRole.USER.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("request"))
    }

    @Test
    fun postUserSignupPasswordLong() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test2")
                .param("email", "test2@example.com")
                .param("password", "123456789012345")
                .param("role", UserRole.USER.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("request"))
    }

    @Test
    fun postUserSignupPasswordCharError() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test2")
                .param("email", "test2@example.com")
                .param("password", "あいうえお")
                .param("role", UserRole.USER.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("request"))
    }

    @Test
    fun postUserSignupAdminRegistration() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .with(csrf())
                .param("name", "test2")
                .param("email", "test2@example.com")
                .param("password", "1234")
                .param("role", UserRole.ADMIN.toString())
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/signup"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("request"))
    }
}