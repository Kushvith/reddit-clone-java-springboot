package com.reddit.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.demo.DTO.commentsDto;
import com.reddit.demo.service.commentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/comments")
@AllArgsConstructor
public class CommentsController {
    private final commentService commentService;
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody commentsDto commentsdto){
        commentService.createComment(commentsdto);
        return new ResponseEntity<>(HttpStatus.CREATED);
        
    }
    @GetMapping("by-post")
    public ResponseEntity<List<commentsDto>> getAllCommentsForPost(@RequestParam("postId") Long postId){
        return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.getCommentPost(postId));
    }
      @GetMapping("by-user")
    public ResponseEntity<List<commentsDto>> getAllCommentsForUser(@RequestParam("userName") String username){
        return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.getCommentByUser(username));
    }
}
