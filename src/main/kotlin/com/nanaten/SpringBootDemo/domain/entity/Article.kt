package com.nanaten.SpringBootDemo.domain.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "articles")
data class Article(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,
        var name: String = "",
        var title: String = "",
        var contents: String = "",
        @Column(name = "article_key")
        var articleKey: String = "",
        @Column(name = "register_at")
        var registerAt: Date = Date(),
        @Column(name = "update_at")
        var updateAt: Date = Date(),
        @Column(name = "user_id")
        var userId: Int? = null
)