package com.velialiev.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.PostEntity;
import com.velialiev.model.SubredditEntity;
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
    public PostEntity mapDtoToPost(PostRequestDto postRequestDto) {

        SubredditEntity subredditEntity = subredditRepository.findBySubredditName(postRequestDto.getSubredditName())
                .orElseThrow(()->new SpringRedditException("No subreddit with such name"));

        UserEntity userEntity = authService.getCurrentUser();

        return PostEntity.builder()
                .postId(postRequestDto.getPostId())
                .postName(postRequestDto.getPostName())
                .url(postRequestDto.getUrl())
                .description(postRequestDto.getDescription())
                .voteCount(1)
                .commentCount(0)
                .userEntity(userEntity)
                .createdDate(Instant.now())
                .subredditEntity(subredditEntity)
                .upVote(true)
                .downVote(false)
                .build();
    }

    @Override
    public PostResponseDto mapPostToDto(PostEntity postEntity) {

        return PostResponseDto.builder()
                .postId(postEntity.getPostId())
                .subredditName(postEntity.getSubredditEntity().getSubredditName())
                .postName(postEntity.getPostName())
                .url(postEntity.getUrl())
                .description(postEntity.getDescription())
                .username(postEntity.getUserEntity().getUsername())
                .voteCount(postEntity.getVoteCount())
                .commentCount(postEntity.getCommentCount())
                .duration(TimeAgo.using(postEntity.getCreatedDate().toEpochMilli()))
                .upVote(postEntity.isUpVote())
                .downVote(postEntity.isDownVote())
                .build();
    }
}
