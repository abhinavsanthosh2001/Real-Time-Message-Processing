package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.responses.SubscribeResponse;
import com.assignment2.queuingmechanism.service.ConsumerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerController {
    ConsumerService consumerService;

    @PostMapping
    public ResponseEntity<ConsumerDto> saveConsumer(@RequestBody ConsumerDto consumerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consumerService.saveConsumer(consumerDto));
    }

    @PutMapping("/subscribe")
    public ResponseEntity<SubscribeResponse> subscribe(@RequestParam("consumerName") String consumerName,
                                                       @RequestParam("topicName") String topicName) {
        consumerService.subscribeToTopic(consumerName, topicName);
        return ResponseEntity.ok().body(new SubscribeResponse(consumerName, topicName));
    }

    @GetMapping("/consume")
    public ResponseEntity<Object> consumeRequest(@RequestParam("topicName") String topicName,
                                                 @RequestParam("consumerName") String consumerName) {
        return ResponseEntity.ok().body(consumerService.consumeMessageRequest(consumerName, topicName));
    }
}

