package com.nanaten.SpringBootDemo.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminController {
    @GetMapping("/admin/index")
    fun getAdminIndex(): String {
        return "admin_index"
    }
}