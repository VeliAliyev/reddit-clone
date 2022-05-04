package com.velialiev.controller;

import com.velialiev.dto.CommentDto;
import com.velialiev.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentDto commentDto){
        commentService.createComment(commentDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("by-postId/{id}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable(name = "id") Long id){
        List<CommentDto> commentDtos = commentService.getAllCommentsByPost(id);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping("by-user/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUsername(@PathVariable(name = "username") String username){
        List<CommentDto> commentDtos = commentService.getAllCommentsByUsername(username);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }
}
