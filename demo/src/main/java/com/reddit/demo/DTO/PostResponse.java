package com.reddit.demo.DTO;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long Id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String SubredditName;
    private Integer voteCount;
    private Integer commentCount;
     private Instant createdDate;
}
