package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.responses.SaveTopicResponse;
import com.assignment2.queuingmechanism.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/topic")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {
    TopicService topicService;

    @PostMapping
    public ResponseEntity<SaveTopicResponse> saveTopic(@RequestBody @Valid TopicDto topicDto) {
        TopicDto savedTopicDto = topicService.saveTopic(topicDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SaveTopicResponse(savedTopicDto.getId(), savedTopicDto.getName()));
    }

    @GetMapping
    public ResponseEntity<Object> showAllMessages() {
        return ResponseEntity.ok().body(topicService.getTopics());
    }
}
