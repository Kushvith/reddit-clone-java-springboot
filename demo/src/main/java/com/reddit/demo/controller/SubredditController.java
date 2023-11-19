package com.reddit.demo.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.demo.DTO.Subredditdto;
import com.reddit.demo.service.SubredditService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor

public class SubredditController {
    private final SubredditService subredditService;
    @PostMapping
    public ResponseEntity<Subredditdto> createSubreddit(@RequestBody Subredditdto subredditdto){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(subredditService.save(subredditdto));
    }
    @GetMapping
    public ResponseEntity<List<Subredditdto>> getAllSubreddits(){
        return ResponseEntity
        .status(HttpStatus.OK).body(subredditService.getAll());
    }
}
