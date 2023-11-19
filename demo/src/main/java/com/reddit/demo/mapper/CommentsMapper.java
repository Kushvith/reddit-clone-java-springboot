package com.reddit.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.demo.DTO.commentsDto;
import com.reddit.demo.model.Comment;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.User;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())" )
    @Mapping(target = "post",source = "post")
    Comment map(commentsDto commentsDto,User user,Post post);
    @Mapping(target="postId",expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName",expression = "java(comment.getUser().getUsername())")
    commentsDto mapToDto(Comment comment);
}
