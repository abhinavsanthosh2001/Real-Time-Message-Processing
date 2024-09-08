package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.entities.Topic;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.utils.Helpers;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TopicServiceTest {
    @Spy
    @InjectMocks
    TopicService topicService;
    @Mock
    Helpers helpers;
    @Mock
    TopicRepo topicRepo;
    EasyRandom easyRandom = new EasyRandom();
    Topic topic = easyRandom.nextObject(Topic.class);
    TopicDto topicDto = easyRandom.nextObject(TopicDto.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void saveTopic() {
        when(helpers.topicDtoToEntity(any())).thenReturn(topic);
        when(helpers.topicEntityToDto(any())).thenReturn(topicDto);
        assertEquals(topicDto, topicService.saveTopic(topicDto));

    }
}