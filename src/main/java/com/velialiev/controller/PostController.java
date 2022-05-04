package com.velialiev.controller;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequestDto postRequestDto){
        postService.createPost(postRequestDto);
        return  new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("id")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long id){
        PostResponseDto postResponseDto = postService.getPost(id);
        return new ResponseEntity(postResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
        List<PostResponseDto> postResponseDtos = postService.getAllPosts();
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponseDto>> getAllPostsBySubreddit(@PathVariable(name = "id") Long id){
        List<PostResponseDto> postResponseDtos = postService.getAllPostsBySubreddit(id);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }
}
