package com.duzj.navigation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转控制器
 */
@Controller
public class PageController {
    //后台页面
    @RequestMapping("/{backPage}")
    public String backPage(@PathVariable String backPage) {
        return "/back/" + backPage;
    }

    @RequestMapping("/")
    public String index() {
        return "/back/index.html";
    }
}
