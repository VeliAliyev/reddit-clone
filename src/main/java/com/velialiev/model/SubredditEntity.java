package com.velialiev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "subreddit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubredditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subredditId;

    @NotBlank(message = "Subreddit name cannot be empty")
    private String subredditName;

    @NotBlank(message = "Description cannot be empty")
    private String subredditDescription;

    @OneToMany(fetch = FetchType.LAZY)
    private List<PostEntity> postEntities;

    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;
}
