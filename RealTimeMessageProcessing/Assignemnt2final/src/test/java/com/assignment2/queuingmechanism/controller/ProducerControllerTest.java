package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.dto.ProducerDto;
import com.assignment2.queuingmechanism.entities.Producer;
import com.assignment2.queuingmechanism.responses.PublisherResponse;
import com.assignment2.queuingmechanism.service.ProducerService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProducerControllerTest {

    @Mock
    ProducerService producerService;
    @Spy
    @InjectMocks
    ProducerController producerController;

    EasyRandom easyRandom = new EasyRandom();
    Producer producer = easyRandom.nextObject(Producer.class);
    ProducerDto producerDto = easyRandom.nextObject(ProducerDto.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProducer() throws Exception {
        when(producerService.saveProducer(producerDto)).thenReturn(producerDto);
        assertEquals(Objects.requireNonNull(ResponseEntity.status(HttpStatus.CREATED).body(producerDto).getBody()).getName(), Objects.requireNonNull(producerController.saveProducer(producerDto).getBody()).getName());
    }

    @Test
    void testPublish() throws Exception {
        final Future<PublisherResponse> publisherResponseFuture = CompletableFuture.completedFuture(
                new PublisherResponse(producerDto.getId(), producerDto.getName(), "message"));
        when(producerService.addMessageToTopic(producerDto.getId(), producerDto.getName(), "message")).thenReturn(publisherResponseFuture);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(publisherResponseFuture.get()), producerController.publish(producerDto.getId(), producerDto.getName(), "message"));
    }
}
