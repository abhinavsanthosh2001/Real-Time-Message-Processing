package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.dto.ProducerDto;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import com.assignment2.queuingmechanism.service.ProducerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProducerController {
    ProducerService producerService;

    @PostMapping
    public ResponseEntity<ProducerDto> saveProducer(@RequestBody ProducerDto producerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(producerService.saveProducer(producerDto));
    }

    @PostMapping("/publish")
    public ResponseEntity<Object> publish(@RequestParam("producerId") long producerId,
                                          @RequestParam("topicName") String topicName,
                                          @RequestParam("message") String message) {
        try {
            return ResponseEntity.ok().body(producerService.addMessageToTopic(producerId, topicName, message).get());
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
