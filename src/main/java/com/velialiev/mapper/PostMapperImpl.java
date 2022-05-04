package com.velialiev.mapper;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.Post;
import com.velialiev.model.Subreddit;
import com.velialiev.model.UserEntity;
import com.velialiev.repository.SubredditRepository;
import com.velialiev.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class PostMapperImpl implements PostMapper{

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    @Override
    public Post mapDtoToPost(PostRequestDto postRequestDto) {

        Subreddit subreddit = subredditRepository.findBySubredditName(postRequestDto.getSubredditName())
                .orElseThrow(()->new SpringRedditException("No subreddit with such name"));

        UserEntity userEntity = authService.getCurrentUser();

        return Post.builder()
                .postId(postRequestDto.getPostId())
                .postName(postRequestDto.getPostName())
                .url(postRequestDto.getUrl())
                .description(postRequestDto.getDescription())
                .voteCount(1)
                .commentCount(0)
                .userEntity(userEntity)
                .createdDate(Instant.now())
                .subreddit(subreddit)
                .upVote(true)
                .downVote(false)
                .build();
    }

    @Override
    public PostResponseDto mapPostToDto(Post post) {

        return PostResponseDto.builder()
                .postId(post.getPostId())
                .subredditName(post.getSubreddit().getSubredditName())
                .postName(post.getPostName())
                .url(post.getUrl())
                .description(post.getDescription())
                .username(post.getUserEntity().getUsername())
                .voteCount(post.getVoteCount())
                .commentCount(post.getCommentCount())
                .duration(Duration.between(Instant.now(), post.getCreatedDate()).toString())
                .upVote(post.isUpVote())
                .downVote(post.isDownVote())
                .build();
    }
}
