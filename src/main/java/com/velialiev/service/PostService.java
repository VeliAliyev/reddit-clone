package com.velialiev.service;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.*;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.SubredditRepository;
import com.velialiev.repository.UserRepository;
import com.velialiev.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    final private PostRepository postRepository;
    final private SubredditRepository subredditRepository;
    final private UserRepository userRepository;
    final private VoteRepository voteRepository;
    final private AuthService authService;

    @Transactional
    public void createPost(PostRequestDto postRequestDto) {
        PostEntity postEntity = mapDtoToPost(postRequestDto);
        postRepository.save(postEntity);
        voteRepository.save(VoteEntity.builder()
                        .voteType(VoteType.UPVOTE)
                        .postEntity(postEntity)
                        .userEntity(authService.getCurrentUser())
                .build());

    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(()->new SpringRedditException("No post with such id"));
        return mapPostToDto(postEntity);
    }
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        return postEntities.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPostsBySubreddit(Long id) {
        SubredditEntity subredditEntity = subredditRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such subreddit"));
        List<PostEntity> postEntities = postRepository.findAllBySubredditEntity(subredditEntity)
                .orElseThrow(()->new SpringRedditException("No posts in this subreddit"));
        return postEntities.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPostsByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new SpringRedditException("No user with such username"));
        List<PostEntity> postEntities = postRepository.findAllByUserEntity(userEntity)
                .orElseThrow(()->new SpringRedditException("No posts related to this user"));
        return postEntities.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }

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
                .build();
    }

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
                .build();
    }
}
