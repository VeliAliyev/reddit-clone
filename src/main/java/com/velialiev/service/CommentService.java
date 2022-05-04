package com.velialiev.service;

import com.velialiev.dto.CommentDto;
import com.velialiev.mapper.CommentMapper;
import com.velialiev.model.CommentEntity;
import com.velialiev.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void createComment(CommentDto commentDto) {
        CommentEntity commentEntity = commentMapper.mapDtoToComment(commentDto);
        commentRepository.save(commentEntity);
    }
}
