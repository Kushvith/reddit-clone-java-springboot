package com.reddit.demo.DTO;

import com.reddit.demo.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VoteDto {
    private VoteType voteType;
    private Long postId;
}
