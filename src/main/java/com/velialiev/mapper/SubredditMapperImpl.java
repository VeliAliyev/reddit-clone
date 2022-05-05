package com.velialiev.mapper;

import com.velialiev.dto.SubredditDto;
import com.velialiev.model.SubredditEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SubredditMapperImpl implements SubredditMapper{
    @Override
    public SubredditDto mapSubredditToDto(SubredditEntity subredditEntity) {

        return SubredditDto.builder()
                .id(subredditEntity.getSubredditId())
                .name(subredditEntity.getSubredditName())
                .description(subredditEntity.getSubredditDescription())
                .numberOfPosts(subredditEntity.getPostEntities().size())
                .build();
    }

    @Override
    public SubredditEntity mapDtoToSubreddit(SubredditDto subredditDto) {
        return SubredditEntity.builder()
                .subredditId(subredditDto.getId())
                .subredditName(subredditDto.getName())
                .subredditDescription(subredditDto.getDescription())
                .createdDate(Instant.now())
                .build();
    }
}
