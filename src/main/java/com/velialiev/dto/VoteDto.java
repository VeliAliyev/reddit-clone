package com.velialiev.dto;

import com.velialiev.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

    public VoteType voteType;
    private Long postId;
    private Long voteId;

}
