package com.reddit.demo.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.reddit.demo.DTO.PostRequest;
import com.reddit.demo.DTO.PostResponse;
import com.reddit.demo.exception.SpringRedditExpection;
import com.reddit.demo.mapper.PostMapper;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.Subreddit;
import com.reddit.demo.model.User;
import com.reddit.demo.repository.PostRepository;
import com.reddit.demo.repository.SubredditRepository;
import com.reddit.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
        .orElseThrow(() ->new SpringRedditExpection("subreddit not found"));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));

    }
    public List<PostResponse> getAllpost(){
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::mapToDto).toList();
    }
    public List<PostResponse> getPostBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(()->new SpringRedditExpection("subreddit not found"));
        List<Post> posts = postRepository.findBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).toList();
    }
    public List<PostResponse> getPostByusername(String username){
        User user = userRepository.findByusername(username)
        .orElseThrow(()->new SpringRedditExpection("user not foumd"));
        List<Post> posts = postRepository.findByuser(user);
        return posts.stream().map(postMapper::mapToDto).toList();
    }
    public PostResponse getPostById(Long id){
        return postMapper.mapToDto(postRepository.findById(id).orElseThrow(()->new SpringRedditExpection("post not found")));
    }
}
