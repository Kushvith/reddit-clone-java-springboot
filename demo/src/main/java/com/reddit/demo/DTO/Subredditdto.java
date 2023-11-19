package com.reddit.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subredditdto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
