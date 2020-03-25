package com.nanaten.SpringBootDemo.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
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
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_REGISTER_NORMAL))
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

    @Test
    fun getArticleEditNotExistsId() {
        mockMvc.perform(MockMvcRequestBuilders.get("/edit/" + 0))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_DOES_NOT_EXISTS))

    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key) VALUES ('test', 'test', 'test', 'test');"])
    fun getArticleEditExistId() {
        val latestArticle = articleController.articleRepository.findAll().last()
        mockMvc.perform(MockMvcRequestBuilders.get("/edit/" + latestArticle.id))
                .andExpect(status().isOk)
                .andExpect(view().name("edit"))
    }

    @Test
    fun updateArticleNotExistArticle() {
        mockMvc.perform(MockMvcRequestBuilders.post("/update")
                .param("id", "0")
                .param("name", "test")
                .param("title", "test")
                .param("contents", "test")
                .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_DOES_NOT_EXISTS))
                .andExpect(flash().attribute(articleController.ALERT_CLASS, articleController.ALERT_CLASS_ERROR))

    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key, register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleNotMatchArticleKey() {
        val latestArticle = articleController.articleRepository.findAll().last()

        mockMvc.perform(
                MockMvcRequestBuilders.post("/update")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/edit/${latestArticle.id}"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_KEY_UNMATCH))
                .andExpect(flash().attribute(articleController.ALERT_CLASS, articleController.ALERT_CLASS_ERROR))
    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key, register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleExistArticle() {
        val latestArticle = articleController.articleRepository.findAll().last()
        mockMvc.perform(
                MockMvcRequestBuilders.post("/update")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", latestArticle.articleKey)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_UPDATE_NORMAL))
    }

    @Test
    fun getDeleteConfirmNotExistsId() {
        mockMvc.perform(MockMvcRequestBuilders.get("/delete/confirm/0"))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_DOES_NOT_EXISTS))
    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key) VALUES ('test', 'test', 'test', 'test');"])
    fun getDeleteConfirmExistsId() {
        val latestArticle = articleController.articleRepository.findAll().last()
        mockMvc.perform(MockMvcRequestBuilders.get("/delete/confirm/${latestArticle.id}"))
                .andExpect(status().isOk)
                .andExpect(view().name("delete_confirm"))

    }

    @Test
    fun deleteArticleNotExistArticle() {
        mockMvc.perform(MockMvcRequestBuilders.post("/delete")
                .param("id", "0")
                .param("name", "test")
                .param("title", "test")
                .param("contents", "test")
                .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_DOES_NOT_EXISTS))
                .andExpect(flash().attribute(articleController.ALERT_CLASS, articleController.ALERT_CLASS_ERROR))

    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key, register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun deleteArticleNotMatchArticleKey() {
        val latestArticle = articleController.articleRepository.findAll().last()

        mockMvc.perform(
                MockMvcRequestBuilders.post("/delete")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", "err.")
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/delete/confirm/${latestArticle.id}"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_ARTICLE_KEY_UNMATCH))
                .andExpect(flash().attribute(articleController.ALERT_CLASS, articleController.ALERT_CLASS_ERROR))
    }

    @Test
    @Sql(statements = ["INSERT INTO articles (name, title, contents, article_key, register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun deleteArticleExistArticle() {
        val latestArticle = articleController.articleRepository.findAll().last()
        mockMvc.perform(
                MockMvcRequestBuilders.post("/delete")
                        .param("id", latestArticle.id.toString())
                        .param("name", latestArticle.name)
                        .param("title", latestArticle.title)
                        .param("contents", latestArticle.contents)
                        .param("articleKey", latestArticle.articleKey)
        )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
                .andExpect(flash().attributeExists(articleController.MESSAGE))
                .andExpect(flash().attribute(articleController.MESSAGE, articleController.MESSAGE_DELETE_NORMAL))
    }
}