package com.gonuts.gonutsbackend.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.NewsService;
import com.gonuts.gonutsbackend.model.News;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    
    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @PostMapping("/addNews")
    public void addNews(@RequestBody News news){
        newsService.addNews(news);
    }
}
