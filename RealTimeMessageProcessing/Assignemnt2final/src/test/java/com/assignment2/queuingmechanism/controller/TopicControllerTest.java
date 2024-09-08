package com.assignment2.queuingmechanism.controller;

import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.entities.Topic;
import com.assignment2.queuingmechanism.responses.SaveTopicResponse;
import com.assignment2.queuingmechanism.service.TopicService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.mockito.Mockito.when;


class TopicControllerTest {
    @Spy
    @InjectMocks
    TopicController topicController;
    @Mock
    TopicService topicService;
    EasyRandom easyRandom = new EasyRandom();
    Topic topic = easyRandom.nextObject(Topic.class);
    TopicDto topicDto = easyRandom.nextObject(TopicDto.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTopic() throws Exception {
        when(topicService.saveTopic(topicDto)).thenReturn(topicDto);
        SaveTopicResponse saveTopicResponse=new SaveTopicResponse(topicDto.getId(),topicDto.getName());
        Assertions.assertEquals(Objects.requireNonNull(ResponseEntity.status(HttpStatus.CREATED).body(saveTopicResponse).getBody()).getTopicName(), Objects.requireNonNull(topicController.saveTopic(topicDto).getBody()).getTopicName());
    }

    @Test
    void testGetAllMessages() throws Exception {

        when(topicService.getTopics()).thenReturn("result");
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).body("result"), topicController.showAllMessages());
    }
}
