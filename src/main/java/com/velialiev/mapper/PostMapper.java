package com.velialiev.mapper;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.model.PostEntity;

public interface PostMapper {

    public PostEntity mapDtoToPost(PostRequestDto postRequestDto);
    public PostResponseDto mapPostToDto(PostEntity postEntity);

}
