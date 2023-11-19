package com.reddit.demo.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.demo.DTO.PostRequest;
import com.reddit.demo.DTO.PostResponse;
import com.reddit.demo.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public ResponseEntity<Void> CreatePost(@RequestBody PostRequest PostRequest){
        postService.save(PostRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPost(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllpost());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }
    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostBySubreddit(@PathVariable Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostBySubreddit(id));
    }
     @GetMapping("by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostByusername(@PathVariable String username)
    {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostByusername(username));
    }
}
