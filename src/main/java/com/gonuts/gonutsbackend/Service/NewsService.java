package com.gonuts.gonutsbackend.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gonuts.gonutsbackend.model.News;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Service    
public class NewsService {
    
    private final DatabaseReference newsReference;

    public NewsService(){
        this.newsReference = FirebaseDatabase.getInstance().getReference("news");
    }

    public String addNews(News news){
        String newsId = UUID.randomUUID().toString();
        boolean validation = validateNews(news);

        if (validation) {
            // Set the current date for the news
    
            // Save the news data asynchronously
            ApiFuture<Void> future = newsReference.child(newsId).setValueAsync(news);
    
            try {
                // Block and check for any exceptions
                future.get();
                System.out.println("News added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding news: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid news data.");
        }
        return "New news posted!";
    }

    public boolean validateNews(News news){

        String title = news.getTitle();

        if (title == null || title.isEmpty() || title.length() < 5){
            throw new IllegalArgumentException("Title must be at least 5 charactesr long. ");
        }
        return true;
    }


}
