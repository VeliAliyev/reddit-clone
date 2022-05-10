package com.velialiev.service;


import com.velialiev.dto.SubredditDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.model.SubredditEntity;
import com.velialiev.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        SubredditEntity save = subredditRepository.save(mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getSubredditId());
        return subredditDto ;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
         return subredditRepository.findAll().stream().map(this::mapSubredditToDto).collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        return mapSubredditToDto(
                subredditRepository.findById(id)
                        .orElseThrow(()->new SpringRedditException("No subreddit found with id: " + id)));
    }

    public SubredditDto mapSubredditToDto(SubredditEntity subredditEntity) {

        return SubredditDto.builder()
                .id(subredditEntity.getSubredditId())
                .name(subredditEntity.getSubredditName())
                .description(subredditEntity.getSubredditDescription())
                .numberOfPosts(subredditEntity.getPostEntities().size())
                .build();
    }

    public SubredditEntity mapDtoToSubreddit(SubredditDto subredditDto) {
        return SubredditEntity.builder()
                .subredditId(subredditDto.getId())
                .subredditName(subredditDto.getName())
                .subredditDescription(subredditDto.getDescription())
                .createdDate(Instant.now())
                .build();
    }
}
