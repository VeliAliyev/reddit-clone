package com.velialiev.service;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.mapper.PostMapper;
import com.velialiev.model.*;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.SubredditRepository;
import com.velialiev.repository.UserRepository;
import com.velialiev.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    final private PostRepository postRepository;
    final private PostMapper postMapper;
    final private SubredditRepository subredditRepository;
    final private UserRepository userRepository;
    final private VoteRepository voteRepository;
    final private AuthService authService;

    @Transactional
    public void createPost(PostRequestDto postRequestDto) {
        PostEntity postEntity = postMapper.mapDtoToPost(postRequestDto);
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
        return postMapper.mapPostToDto(postEntity);
    }
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = postEntities.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
        return postResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPostsBySubreddit(Long id) {
        SubredditEntity subredditEntity = subredditRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such subreddit"));
        List<PostEntity> postEntities = postRepository.findAllBySubredditEntity(subredditEntity)
                .orElseThrow(()->new SpringRedditException("No posts in this subreddit"));
        return postEntities.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPostsByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new SpringRedditException("No user with such username"));
        List<PostEntity> postEntities = postRepository.findAllByUserEntity(userEntity)
                .orElseThrow(()->new SpringRedditException("No posts related to this user"));
        return postEntities.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
    }
}
