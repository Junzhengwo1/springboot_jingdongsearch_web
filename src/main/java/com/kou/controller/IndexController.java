package com.kou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author JIAJUN KOU
 */
@Controller
public class IndexController {

    @GetMapping({"/","/index"})
    public String index(){

        return "index";
    }
}
