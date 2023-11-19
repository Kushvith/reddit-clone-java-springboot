package com.reddit.demo.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.demo.DTO.Subredditdto;
import com.reddit.demo.model.Post;
import com.reddit.demo.model.Subreddit;
import com.reddit.demo.model.User;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    @Mapping(target = "numberOfPosts",expression = "java(mapPosts(subreddit.getPost()))")
    Subredditdto mapSubredditdto(Subreddit subreddit);
    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }
    @InheritInverseConfiguration
    @Mapping(target = "post",ignore = true)
    @Mapping(target = "user",source = "user")
    @Mapping(target ="name", source="subredditdto.name")
    @Mapping(target = "description",source = "subredditdto.description") 
    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    Subreddit mapDtoToSubreddit(Subredditdto subredditdto,User user);
}
