package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.UserRole
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userController: UserController

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

    @Test
    fun getUserLogin() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/login"))
                .andExpect(status().isOk)
    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test3', 'test3@example.com', '\$2a\$10\$MMnbIXYB4BQI88yiKpiR2eiIIHiUEymGMyWqWlp01Iz.aqbD3ud4i', 'USER');"])
    fun UserLoginAuth() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login/auth")
                .with(csrf())
                .param("email", "test3@example.com")
                .param("password", "test3")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))
    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test4', 'test4@example.com', 'dummy', 'USER')"])
    @WithUserDetails(value = "test4")
    fun authentication() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/index"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("user"))
    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test5', 'test5@example.com', 'dummy', 'USER')"])
    @WithUserDetails(value = "test5")
    fun getUserIndex() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/index"))
                .andExpect(status().isOk)
    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test6', 'test6@example.com', 'dummy', 'USER');"])
    @WithUserDetails("test6")
    fun registerArticle() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/article/register")
                .with(csrf())
                .param("name", "test")
                .param("title", "test")
                .param("contents", "test")
                .param("articleKey", "test")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/index"))
                .andExpect(flash().attributeExists(userController.MESSAGE))
                .andExpect(flash().attribute(userController.MESSAGE, userController.MESSAGE_REGISTER_NORMAL))
    }

    @Test
    // @Sql(statements = ["INSERT INTO users (name, email, password, role) VALUES ('test7', 'test7@example.com', 'dummy', 'USER');"])
    @WithUserDetails("test7")
    fun registerArticleRequestError() {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/article/register")
                .with(csrf())
                .param("name", "")
                .param("title", "")
                .param("contents", "")
                .param("articleKey", "")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/user/index"))
                .andExpect(flash().attributeExists(userController.ERRORS))
                .andExpect(flash().attributeExists(userController.REQUEST))
    }
}