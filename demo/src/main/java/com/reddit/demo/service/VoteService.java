package com.reddit.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reddit.demo.DTO.VoteDto;
import com.reddit.demo.exception.SpringRedditExpection;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.Vote;
import com.reddit.demo.model.VoteType;
import com.reddit.demo.repository.PostRepository;
import com.reddit.demo.repository.VoteRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    @Transactional
    public void vote(VoteDto voteDto){
        Post post = postRepository.findById(voteDto.getPostId())
            .orElseThrow(()-> new SpringRedditExpection("post not found"));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(voteByPostAndUser.isPresent()){
            if(voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
                throw new SpringRedditExpection("you already voted this post");
            }else{
                voteByPostAndUser.get().setVoteType(voteDto.getVoteType());
            }
            
        }else{
            if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
                 post.setVoteCount(post.getVoteCount() + 1);
            }else{
                post.setVoteCount(post.getVoteCount() -1);
            }
            voteRepository.save(mapToVote(voteDto, post));
            postRepository.save(post);
        }
       
    }
     public Vote mapToVote(VoteDto voteDto,Post post){
            return Vote.builder().voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
        }
}
