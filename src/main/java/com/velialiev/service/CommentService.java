package com.velialiev.service;

import com.velialiev.dto.CommentDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.mapper.CommentMapper;
import com.velialiev.model.CommentEntity;
import com.velialiev.model.Post;
import com.velialiev.repository.CommentRepository;
import com.velialiev.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(CommentDto commentDto) {
        CommentEntity commentEntity = commentMapper.mapDtoToComment(commentDto);
        commentRepository.save(commentEntity);
    }

    public List<CommentDto> getAllCommentsByPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such post"));
        List<CommentEntity> commentEntities = commentRepository.findAllByPost(post)
                .orElseThrow(()->new SpringRedditException("No comments for this post"));
        return commentEntities.stream().map(commentMapper::mapCommentToDto).collect(Collectors.toList());
    }



}
