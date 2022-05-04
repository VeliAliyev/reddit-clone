package com.velialiev.controller;

import com.velialiev.dto.CommentDto;
import com.velialiev.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
