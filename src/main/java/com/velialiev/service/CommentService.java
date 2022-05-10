package com.velialiev.service;

import com.velialiev.dto.CommentDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.CommentEntity;
import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.repository.CommentRepository;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public void createComment(CommentDto commentDto) {
        CommentEntity commentEntity = mapDtoToComment(commentDto);
        commentRepository.save(commentEntity);
    }

    public List<CommentDto> getAllCommentsByPost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such post"));
        List<CommentEntity> commentEntities = commentRepository.findAllByPostEntity(postEntity)
                .orElseThrow(()->new SpringRedditException("No comments for this post"));
        return commentEntities.stream().map(this::mapCommentToDto).collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new SpringRedditException("No such user"));

        List<CommentEntity> commentEntities = commentRepository.findAllByUserEntity(userEntity)
                .orElseThrow(()->new SpringRedditException("No comments for this user"));
        return commentEntities.stream().map(this::mapCommentToDto).collect(Collectors.toList());
    }

    public CommentEntity mapDtoToComment(CommentDto commentDto) {

        UserEntity userEntity = authService.getCurrentUser();
        PostEntity postEntity = postRepository
                .findById(commentDto.getPostID()).orElseThrow(()->new SpringRedditException("No post for this comment"));

        return CommentEntity.builder()
                .commentId(commentDto.getCommentId())
                .text(commentDto.getText())
                .postEntity(postEntity)
                .createdDate(Instant.now())
                .userEntity(userEntity)
                .build();
    }

    public CommentDto mapCommentToDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .commentId(commentEntity.getCommentId())
                .text(commentEntity.getText())
                .postID(commentEntity.getPostEntity().getPostId())
                .build();
    }
}
