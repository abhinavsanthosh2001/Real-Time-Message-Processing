package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.dto.ProducerDto;
import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.dto.TopicMessageDto;
import com.assignment2.queuingmechanism.entities.Producer;
import com.assignment2.queuingmechanism.entities.Topic;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import com.assignment2.queuingmechanism.repository.ProducerRepo;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.responses.PublisherResponse;
import com.assignment2.queuingmechanism.utils.Helpers;
import com.assignment2.queuingmechanism.utils.Validations;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class ProducerServiceTest {

    @Spy
    @InjectMocks
    ProducerService producerService;
    @Mock
    ProducerRepo producerRepo;
    @Mock
    Helpers helpers;
    @Mock
    TopicRepo topicRepo;
    @Mock
    Validations validations;
    @Mock
    TopicService topicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProducer() {
        EasyRandom easyRandom = new EasyRandom();
        when(helpers.producerDtoToEntity(any())).thenReturn(easyRandom.nextObject(Producer.class));
        when(helpers.producerEntityToDto(any())).thenReturn(easyRandom.nextObject(ProducerDto.class));
        assertDoesNotThrow(() -> producerService.saveProducer(new ProducerDto()));
    }

    @Test
    void addMessageToTopic() throws ExecutionException, InterruptedException {
        EasyRandom easyRandom = new EasyRandom();
        Topic topic = easyRandom.nextObject(Topic.class);
        TopicDto topicDto = easyRandom.nextObject(TopicDto.class);
        Map<Long, LinkedBlockingQueue<TopicMessageDto>> put = TopicService.topics.put(topic.getName(), new HashMap<>());
        Map<String,Map<Long, LinkedBlockingQueue<TopicMessageDto>>> topics=new HashMap<>();
        topics.put(topic.getName(),new HashMap<>());
        when(topicService.getTopics()).thenReturn(topics);
        PublisherResponse publisherResponse = new PublisherResponse(1, topic.getName(), "message");
        when(producerRepo.existsById(any())).thenReturn(true);
        when(topicRepo.findByName(any())).thenReturn(topic);
        when(helpers.topicEntityToDto(any())).thenReturn(topicDto);
        assertEquals(publisherResponse.getTopicName(), producerService.addMessageToTopic(1, topic.getName(), "message").get().getTopicName());
    }

    @Test
    void addMessageToTopicWithError()  {
        when(producerRepo.existsById(any())).thenReturn(false);
        assertThrows(CustomGenericException.class,()->producerService.addMessageToTopic(1,"topic","message"));
    }
}