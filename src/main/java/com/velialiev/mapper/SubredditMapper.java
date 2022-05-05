package com.velialiev.mapper;

import com.velialiev.dto.SubredditDto;
import com.velialiev.model.SubredditEntity;

public interface SubredditMapper {

    public SubredditDto mapSubredditToDto(SubredditEntity subredditEntity);
    public SubredditEntity mapDtoToSubreddit(SubredditDto subredditDto);


}
