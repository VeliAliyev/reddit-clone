package com.velialiev.controller;

import com.velialiev.dto.VoteDto;
import com.velialiev.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity vote(@RequestBody VoteDto voteDto){
        voteService.vote(voteDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
