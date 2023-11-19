package com.reddit.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.demo.model.Comment;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.User;

public interface CommentRepository extends JpaRepository<Comment,Long>{
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
}
