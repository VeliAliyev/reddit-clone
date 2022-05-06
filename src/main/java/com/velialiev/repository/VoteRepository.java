package com.velialiev.repository;

import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.model.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    Optional<VoteEntity> findByUserEntityAndPostEntity(UserEntity userEntity, PostEntity postEntity);
}
