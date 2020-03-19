package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.Article
import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import com.nanaten.SpringBootDemo.request.ArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ArticleController {
    @Autowired
    lateinit var articleRepository: ArticleRepository

    @PostMapping("/")
    @ResponseBody
    fun registerArticle(@ModelAttribute articleRequest: ArticleRequest): String {
        val article = Article(
                articleRequest.id,
                articleRequest.name,
                articleRequest.title,
                articleRequest.contents,
                articleRequest.articleKey
        )
        articleRepository.save(article)
        return "Saved"
    }

    @GetMapping("/")
    fun getArticleList(model: Model): String {
        model.addAttribute("articles", articleRepository.findAll())
        return "index"
    }
}