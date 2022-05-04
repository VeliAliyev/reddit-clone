package com.velialiev.service;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.mapper.PostMapper;
import com.velialiev.model.Post;
import com.velialiev.model.Subreddit;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    final private PostRepository postRepository;
    final private PostMapper postMapper;
    final private SubredditRepository subredditRepository;

    public void createPost(PostRequestDto postRequestDto) {
        Post post = postMapper.mapDtoToPost(postRequestDto);
        postRepository.save(post);
    }

    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new SpringRedditException("No post with such id"));
        return postMapper.mapPostToDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = posts.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
        return postResponseDtos;
    }


    public List<PostResponseDto> getAllPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such subreddit"));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit)
                .orElseThrow(()->new SpringRedditException("No posts in this subreddit"));
        return posts.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
    }
}
