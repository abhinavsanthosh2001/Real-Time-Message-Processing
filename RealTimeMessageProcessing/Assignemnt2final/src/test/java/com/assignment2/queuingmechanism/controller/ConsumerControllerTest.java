package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.responses.ConsumeResponse;
import com.assignment2.queuingmechanism.responses.SubscribeResponse;
import com.assignment2.queuingmechanism.service.ConsumerService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ConsumerControllerTest {

    @Autowired
     MockMvc mockMvc;

    @Mock
     ConsumerService consumerService;

    @InjectMocks
    ConsumerController consumerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    EasyRandom easyRandom = new EasyRandom();
    ConsumerDto consumerDto = easyRandom.nextObject(ConsumerDto.class);

    @Test
    void testSaveConsumer()  {
        when(consumerService.saveConsumer(consumerDto)).thenReturn(consumerDto);
        assertEquals(Objects.requireNonNull(ResponseEntity.status(HttpStatus.CREATED).body(consumerDto).getBody()).getName(), Objects.requireNonNull(consumerController.saveConsumer(consumerDto).getBody()).getName());
    }

    @Test
    void testSubscribe()  {
        SubscribeResponse subscribeResponse = new SubscribeResponse(consumerDto.getName(), "topic");
        assertEquals(Objects.requireNonNull(ResponseEntity.status(HttpStatus.OK).body(subscribeResponse).getBody()).getConsumerName(), Objects.requireNonNull(consumerController.subscribe(consumerDto.getName(), "topic").getBody()).getConsumerName());
    }

    @Test
    void testConsumeRequest() {
        // Setup
        // Configure ConsumerService.consumeMessageRequest(...).
        final ConsumeResponse consumeResponse = new ConsumeResponse(1, "topicName", consumerDto.getName(), "status");
        when(consumerService.consumeMessageRequest(consumerDto.getName(), "topic")).thenReturn(consumeResponse);
        assertEquals(consumeResponse,consumerController.consumeRequest("topic",consumerDto.getName()).getBody());
    }
}
