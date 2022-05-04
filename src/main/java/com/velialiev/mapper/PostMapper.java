package com.velialiev.mapper;

import com.velialiev.dto.PostRequestDto;
import com.velialiev.dto.PostResponseDto;
import com.velialiev.model.Post;

public interface PostMapper {

    public Post mapDtoToPost(PostRequestDto postRequestDto);
    public PostResponseDto mapPostToDto(Post post);

}
