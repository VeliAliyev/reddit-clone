package com.velialiev.repository;

import com.velialiev.model.CommentEntity;
import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Optional<List<CommentEntity>> findAllByPostEntity(PostEntity postEntity);
    Optional<List<CommentEntity>> findAllByUserEntity(UserEntity userEntity);

}
