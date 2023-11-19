package com.reddit.demo.repository;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.demo.model.Post;
import com.reddit.demo.model.Subreddit;
import com.reddit.demo.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findBySubreddit(Subreddit subreddit);
    List<Post> findByuser(User user);
}
