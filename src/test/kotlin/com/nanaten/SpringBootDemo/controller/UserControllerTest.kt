package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
internal class UserControllerTest {

    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var target: UserController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(target).build()
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun addNewUser() {
        mockMvc.perform(
                post("/user/add")
                        .param("name", "unit_test"))
                .andExpect(status().isOk)
                .andExpect(content().string("Save Success."))
    }

    @Test
    fun getAllUsers() {
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    @Sql(statements = ["INSERT INTO users (name) VALUES ('update_data');"])
    fun updateUser() {
        val lastUser: User = target.userRepository.findAll().last()

        mockMvc.perform(
                post("/user/update")
                        .param("id", lastUser.id.toString())
                        .param("name", "updated!!!")
        )
                .andExpect(status().isOk)
                .andExpect(content().string("Update Success."))
    }


    @Test
    fun deleteNoUser() {
        mockMvc.perform(
                post("/user/delete").param("id", "-1")
        )
                .andExpect(status().isOk)
                .andExpect(content().string("User ID Unavailable."))
    }


    @Test
    @Sql(statements = ["INSERT INTO users (name) VALUES ('delete_data');"])
    fun deleteUser() {
        val lastUser: User = target.userRepository.findAll().last()
        mockMvc.perform(
                post("/user/delete").param("id", lastUser.id.toString())
        )
                .andExpect(status().isOk)
                .andExpect(content().string("Delete Success."))
    }
}