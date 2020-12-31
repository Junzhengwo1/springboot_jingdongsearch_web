package com.kou.controller;

import com.kou.service.impl.ContentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author JIAJUN KOU
 */
@Controller
public class ContentController {
    @Autowired
    private ContentServiceImpl contentService;

    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public Boolean parse(@PathVariable("keyword") String keyword) throws IOException {
        return contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    @ResponseBody
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") Integer pageNo,
                                           @PathVariable("pageSize") Integer pageSize) throws IOException {


        return contentService.searchPage(keyword,pageNo,pageSize);
    }

}
