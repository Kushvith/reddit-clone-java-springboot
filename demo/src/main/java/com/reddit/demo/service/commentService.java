package com.reddit.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reddit.demo.DTO.NotificationEmail;
import com.reddit.demo.DTO.commentsDto;
import com.reddit.demo.exception.SpringRedditExpection;
import com.reddit.demo.mapper.CommentsMapper;
import com.reddit.demo.model.Comment;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.User;
import com.reddit.demo.repository.CommentRepository;
import com.reddit.demo.repository.PostRepository;
import com.reddit.demo.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class commentService {
    private static final String Post_Url =" ";
    private final CommentsMapper commentsMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailService mailService;


    public void createComment(commentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId())
        .orElseThrow(()-> new SpringRedditExpection("Post not found"));
        Comment comment = commentsMapper.map(commentsDto, authService.getCurrentUser(), post);
        commentRepository.save(comment);
         mailService.sendMail(new NotificationEmail(
            post.getUser().getUsername() +" commented on your post",
            post.getUser().getEmail(),
            "posted a comment on your post"+post.getUrl()
        ));
    }
    public List<commentsDto> getCommentPost(Long postId)
    {
        Post post = postRepository.findById(postId).orElseThrow(()-> new SpringRedditExpection("post not found"));
        return commentRepository.findByPost(post).stream()
            .map(commentsMapper::mapToDto).toList();
    } 
    public List<commentsDto> getCommentByUser(String username)
    {
        User user = userRepository.findByusername(username).orElseThrow(()-> new SpringRedditExpection("username not found"));
        return commentRepository.findByUser(user).stream()
            .map(commentsMapper::mapToDto).toList();
    } 
    
}
