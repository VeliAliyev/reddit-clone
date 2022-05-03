package com.velialiev.mapper;

import com.velialiev.dto.SubredditDto;
import com.velialiev.model.Subreddit;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SubredditMapperImpl implements SubredditMapper{
    @Override
    public SubredditDto mapSubredditToDto(Subreddit subreddit) {

        return SubredditDto.builder()
                .id(subreddit.getSubredditId())
                .name(subreddit.getSubredditName())
                .description(subreddit.getSubredditDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    @Override
    public Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder()
                .subredditId(subredditDto.getId())
                .subredditName(subredditDto.getName())
                .subredditDescription(subredditDto.getDescription())
                .createdDate(Instant.now())
                .build();
    }
}
