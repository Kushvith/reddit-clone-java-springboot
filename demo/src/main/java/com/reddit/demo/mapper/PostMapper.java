package com.reddit.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.reddit.demo.DTO.PostRequest;
import com.reddit.demo.DTO.PostResponse;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.Subreddit;
import com.reddit.demo.model.User;
import com.reddit.demo.repository.CommentRepository;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit",source  = "subreddit")
    @Mapping(target="user",source  = "user")
    @Mapping(target = "description",source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0") 
    public abstract Post map(PostRequest postRequest,Subreddit subreddit,User user);

    @Mapping(target = "id",source = "postId")
    @Mapping(target = "subredditName",source = "subreddit.name")
    @Mapping(target = "userName",source = "user.username")
    @Mapping(target = "commentCount",expression = "java(commentCount(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();

    }

}
