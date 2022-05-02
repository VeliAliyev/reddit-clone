package com.velialiev.service;


import com.velialiev.dto.SubredditDto;
import com.velialiev.model.Subreddit;
import com.velialiev.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(save.getSubredditId());
        return subredditDto ;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().subredditName(subredditDto.getName()).subredditDescription(subredditDto.getDescription()).build();
    }
    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
         return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder()
                .name(subreddit.getSubredditName())
                .id(subreddit.getSubredditId())
                .description(subreddit.getSubredditDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }
}
