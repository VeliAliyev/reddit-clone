package com.velialiev.mapper;

import com.velialiev.dto.CommentDto;
import com.velialiev.model.CommentEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CommentMapperImpl implements CommentMapper{

    private final AuthService authService;

    @Override
    public CommentEntity mapDtoToComment(CommentDto commentDto) {

        UserEntity userEntity = authService.getCurrentUser();

        return CommentEntity.builder()
                .commentId(commentDto.getCommentId())
                .text(commentDto.getText())
                .post(commentDto.getPost())
                .createdDate(Instant.now())
                .userEntity(userEntity)
                .build();
    }

    @Override
    public CommentDto mapCommentToDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .commentId(commentEntity.getCommentId())
                .text(commentEntity.getText())
                .post(commentEntity.getPost())
                .build();
    }
}
