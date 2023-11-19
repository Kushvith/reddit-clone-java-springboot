package com.reddit.demo.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.demo.DTO.Subredditdto;
import com.reddit.demo.mapper.SubredditMapper;
import com.reddit.demo.model.Subreddit;
import com.reddit.demo.repository.SubredditRepository;


import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor

public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;
    @Transactional
    public Subredditdto save(Subredditdto subredditdto)
    {
      Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditdto,authService.getCurrentUser()));
      subredditdto.setId(save.getId());
      return subredditdto;
    }

    // private Subreddit mapSubredditDto(Subredditdto subredditdto) {
    //    return  Subreddit.builder().name(subredditdto.getName())
    //     .description(subredditdto.getDescription())
    //     .build();
    // }
    @Transactional(readOnly = true)
    public List<Subredditdto> getAll(){
        return subredditRepository.findAll()
          .stream()
          .map(subredditMapper::mapSubredditdto)
          .toList()
          ;
    }
    // private Subredditdto mapToDto(Subreddit subreddit){
    //   return Subredditdto.builder().name(subreddit.getName())
    //   .id(subreddit.getId()).numberOfPosts(subreddit.getPost().size())
    //   .build();
    // }
}
