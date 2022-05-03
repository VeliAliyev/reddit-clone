package com.velialiev.mapper;

import com.velialiev.dto.SubredditDto;
import com.velialiev.model.Subreddit;

public interface SubredditMapper {

    public SubredditDto mapSubredditToDto(Subreddit subreddit);
    public Subreddit mapDtoToSubreddit(SubredditDto subredditDto);


}
