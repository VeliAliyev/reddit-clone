package com.velialiev.model;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1), NOVOTE(0);

    VoteType(Integer direction){};
}
