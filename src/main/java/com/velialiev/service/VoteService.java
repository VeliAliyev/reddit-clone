package com.velialiev.service;

import com.velialiev.dto.VoteDto;
import com.velialiev.model.PostEntity;
import com.velialiev.model.UserEntity;
import com.velialiev.model.VoteEntity;
import com.velialiev.model.VoteType;
import com.velialiev.repository.PostRepository;
import com.velialiev.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteRepository voteRepository;

    public void vote(VoteDto voteDto){
        VoteEntity voteEntity;
        UserEntity userEntity = authService.getCurrentUser();
        PostEntity postEntity = postRepository.findById(voteDto.getPostId()).orElseThrow();
        Optional<VoteEntity> optional = voteRepository.findByUserEntityAndPostEntity(userEntity, postEntity);

        if(!optional.isPresent())
            voteEntity = createVote(voteDto, userEntity, postEntity);
        else
            voteEntity = optional.get();

        if (voteEntity.getVoteType().equals(voteDto.getVoteType()) && !voteDto.getVoteType().equals(VoteType.NOVOTE)) {
            voteDto.setVoteType(VoteType.NOVOTE);
            postEntity.setVoteCount(postEntity.getVoteCount() - 1);
        } else if (voteDto.getVoteType().equals(VoteType.UPVOTE)) {
            postEntity.setVoteCount(postEntity.getVoteCount() + 1);
        } else
            postEntity.setVoteCount(postEntity.getVoteCount() - 1);

        voteEntity.setVoteType(voteDto.getVoteType());
        postRepository.save(postEntity);
        voteRepository.save(voteEntity);
    }

    public VoteEntity createVote(VoteDto voteDto, UserEntity userEntity, PostEntity postEntity){
        return VoteEntity.builder()
                .voteId(voteDto.getVoteId())
                .voteType(VoteType.NOVOTE)
                .userEntity(userEntity)
                .postEntity(postEntity)
                .build();
    };


}
