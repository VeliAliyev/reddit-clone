package com.velialiev.service;


import com.velialiev.dto.SubredditDto;
import com.velialiev.exceptions.SpringRedditException;
import com.velialiev.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getSubredditId());
        return subredditDto ;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
         return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        return subredditMapper.mapSubredditToDto(
                subredditRepository.findById(id)
                        .orElseThrow(()->new SpringRedditException("No subreddit found with id: " + id)));
    }
}
