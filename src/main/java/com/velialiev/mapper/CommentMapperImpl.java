package com.velialiev.mapper;

import com.velialiev.dto.CommentDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.CommentEntity;
import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.repository.PostRepository;
import com.velialiev.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class CommentMapperImpl implements CommentMapper{

    private final AuthService authService;
    private final PostRepository postRepository;

    @Override
    public CommentEntity mapDtoToComment(CommentDto commentDto) {

        UserEntity userEntity = authService.getCurrentUser();
        PostEntity postEntity = postRepository.findById(commentDto.getPostID()).orElseThrow(()->new SpringRedditException("No post for this comment"));

        return CommentEntity.builder()
                .commentId(commentDto.getCommentId())
                .text(commentDto.getText())
                .postEntity(postEntity)
                .createdDate(Instant.now())
                .userEntity(userEntity)
                .build();
    }

    @Override
    public CommentDto mapCommentToDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .commentId(commentEntity.getCommentId())
                .text(commentEntity.getText())
                .postID(commentEntity.getPostEntity().getPostId())
                .build();
    }
}
