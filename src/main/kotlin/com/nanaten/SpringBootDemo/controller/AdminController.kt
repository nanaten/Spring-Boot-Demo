package com.nanaten.SpringBootDemo.controller

import com.nanaten.SpringBootDemo.domain.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AdminController {

    val MESSAGE_ARTICLE_DOES_NOT_EXISTS = "対象の記事が見つかりませんでした"
    val MESSAGE_ARTICLE_NOT_SELECTED = "削除する記事を選択してください"
    val MESSAGE = "message"
    val MESSAGE_DELETE_NORMAL = "正常に削除しました"
    val ALERT_CLASS_ERROR = "alert-error"
    val ALERT_CLASS = "alert_class"
    val PAGE_SIZE: Int = 10

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @GetMapping("/admin/index")
    fun getAdminIndex(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int, model: Model): String {
        val pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "updateAt").and(Sort.by(Sort.Direction.ASC, "id")))

        val articles = articleRepository.findAll(pageable)
        model.addAttribute("page", articles)
        model.addAttribute("isAdmin", true)
        return "admin_index"
    }

    @PostMapping("/admin/article/delete/{id}")
    fun deleteArticle(@PathVariable id: Int,
                      redirectAttributes: RedirectAttributes): String {
        if (!articleRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/admin/index"
        }

        articleRepository.deleteById(id)

        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_DELETE_NORMAL)
        return "redirect:/admin/index"
    }


    @PostMapping("/admin/article/deletes")
    fun deleteArticles(
            @RequestParam(value = "article_checks", required = false)
            checkboxValues: List<Int>?,
            redirectAttributes: RedirectAttributes
    ): String {
        if (checkboxValues == null || checkboxValues.isEmpty()) {
            redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_ARTICLE_NOT_SELECTED)
            redirectAttributes.addFlashAttribute(ALERT_CLASS, ALERT_CLASS_ERROR)
            return "redirect:/admin/index"
        }

        articleRepository.deleteByIdIn(checkboxValues)
        redirectAttributes.addFlashAttribute(MESSAGE, MESSAGE_DELETE_NORMAL)
        return "redirect:/admin/index"
    }
}