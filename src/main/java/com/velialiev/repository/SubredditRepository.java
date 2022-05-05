package com.velialiev.repository;

import com.velialiev.model.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<SubredditEntity, Long> {

    Optional<SubredditEntity> findBySubredditName(String subredditName);
}
