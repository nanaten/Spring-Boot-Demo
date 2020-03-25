package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.entity.Article
import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import com.nanaten.SpringBootDemo.request.ArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class ArticleController {

    val MESSAGE_REGISTER_NORMAL = "正常に投稿できました"
    val MESSAGE = "message"
    val MESSAGE_ARTICLE_DOES_NOT_EXISTS = "対象の記事が見つかりませんでした。"
    val MESSAGE_UPDATE_NORMAL = "正常に更新しました。"
    val MESSAGE_ARTICLE_KEY_UNMATCH = "投稿 KEY が一致しません。"
    val ALERT_CLASS_ERROR = "alert-error"
    val ALERT_CLASS = "alert_class"
    val MESSAGE_DELETE_NORMAL = "正常に削除しました。"

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @PostMapping("/")
    fun registerArticle(@ModelAttribute articleRequest: ArticleRequest,
                        redirectAttributes: RedirectAttributes): String {
        val article = Article(
                articleRequest.id,
                articleRequest.name,
                articleRequest.title,
                articleRequest.contents,
                articleRequest.articleKey
        )
        articleRepository.save(article)

        redirectAttributes.addFlashAttribute(
                MESSAGE, MESSAGE_REGISTER_NORMAL
        )
        return "redirect:/"
    }

    @GetMapping("/")
    fun getArticleList(model: Model): String {
        model.addAttribute("articles", articleRepository.findAll())
        return "index"
    }

    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int, model: Model,
                       redirectAttributes: RedirectAttributes): String {
        return if (articleRepository.existsById(id)) {
            model.addAttribute("article", articleRepository.findById(id))
            "edit"
        } else {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            "redirect:/"
        }
    }

    @PostMapping("/update")
    fun updateArticle(articleRequest: ArticleRequest,
                      redirectAttributes: RedirectAttributes): String {
        if (!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/edit/${articleRequest.id}"
        }

        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()

        articleRepository.save(article)
        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_UPDATE_NORMAL)
        return "redirect:/"
    }

    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(@PathVariable id: Int, model: Model,
                         redirectAttributes: RedirectAttributes): String {
        if (!articleRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }
        model.addAttribute("article", articleRepository.findById(id).get())
        return "delete_confirm"
    }

    @PostMapping("/delete")
    fun deleteArticle(@ModelAttribute articleRequest: ArticleRequest,
                      redirectAttributes: RedirectAttributes): String {
        if (!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article = articleRepository.findById(articleRequest.id).get()
        if (articleRequest.articleKey != article.articleKey) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/delete/confirm/${article.id}"
        }

        articleRepository.deleteById(articleRequest.id)

        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_DELETE_NORMAL)
        return "redirect:/"
    }
}