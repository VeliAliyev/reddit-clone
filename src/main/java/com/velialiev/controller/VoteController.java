package com.velialiev.controller;

import com.velialiev.dto.VoteDto;
import com.velialiev.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping
    public ResponseEntity vote(@RequestBody VoteDto voteDto){
        voteService.vote(voteDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
