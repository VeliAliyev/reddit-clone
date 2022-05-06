package com.velialiev.service;

import com.velialiev.dto.CommentDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.mapper.CommentMapper;
import com.velialiev.model.CommentEntity;
import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.repository.CommentRepository;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.UserRepository;
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
    private final UserRepository userRepository;

    public void createComment(CommentDto commentDto) {
        CommentEntity commentEntity = commentMapper.mapDtoToComment(commentDto);
        commentRepository.save(commentEntity);
    }

    public List<CommentDto> getAllCommentsByPost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(()->new SpringRedditException("No such post"));
        List<CommentEntity> commentEntities = commentRepository.findAllByPostEntity(postEntity)
                .orElseThrow(()->new SpringRedditException("No comments for this post"));
        return commentEntities.stream().map(commentMapper::mapCommentToDto).collect(Collectors.toList());
    }


    public List<CommentDto> getAllCommentsByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->new SpringRedditException("No such user"));

        List<CommentEntity> commentEntities = commentRepository.findAllByUserEntity(userEntity)
                .orElseThrow(()->new SpringRedditException("No comments for this user"));
        return commentEntities.stream().map(commentMapper::mapCommentToDto).collect(Collectors.toList());
    }
}
