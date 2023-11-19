package com.reddit.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByusername(String username);
      Optional<User> findByemail(String email);
}
