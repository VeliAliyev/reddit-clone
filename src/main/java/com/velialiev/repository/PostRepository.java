package com.velialiev.repository;

import com.velialiev.model.Post;
import com.velialiev.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findAllBySubreddit(Subreddit subreddit);
}
