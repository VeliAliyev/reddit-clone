package com.velialiev.mapper;

import com.velialiev.dto.CommentDto;
import com.velialiev.model.CommentEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentMapper {

    public CommentEntity mapDtoToComment(CommentDto commentDto);
    public CommentDto mapCommentToDto(CommentEntity commentEntity);

}
