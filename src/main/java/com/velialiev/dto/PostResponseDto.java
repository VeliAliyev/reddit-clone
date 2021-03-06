package com.velialiev.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
    private String username;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;

}
