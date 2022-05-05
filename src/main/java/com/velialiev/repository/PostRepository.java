package com.velialiev.repository;

import com.velialiev.model.PostEntity;
import com.velialiev.model.SubredditEntity;
import com.velialiev.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<List<PostEntity>> findAllBySubreddit(SubredditEntity subredditEntity);
    Optional<List<PostEntity>> findAllByUserEntity(UserEntity userEntity);
}
