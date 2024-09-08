package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.dto.InboundMessageDto;
import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.entities.Consumer;
import com.assignment2.queuingmechanism.entities.InboundMessage;
import com.assignment2.queuingmechanism.entities.Topic;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import com.assignment2.queuingmechanism.repository.ConsumerRepo;
import com.assignment2.queuingmechanism.repository.InboundMessageRepo;
import com.assignment2.queuingmechanism.repository.ProducerRepo;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.responses.ConsumeResponse;
import com.assignment2.queuingmechanism.utils.Helpers;
import com.assignment2.queuingmechanism.utils.Validations;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ConsumerServiceTest {

    @InjectMocks
    ConsumerService consumerService;
    @Mock
    ConsumerRepo consumerRepo;
    @Mock
    ProducerRepo producerRepo;
    @Mock
    Helpers helpers;
    @Mock
    Validations validations;
    @Mock
    TopicRepo topicRepo;
    @InjectMocks
    ProducerService producerService;
    @Mock
    InboundMessageRepo inboundMessageRepo;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void subscribeToTopic() {
        EasyRandom easyRandom = new EasyRandom();
        when(consumerRepo.findByName(any())).thenReturn(easyRandom.nextObject(Consumer.class));
        when(topicRepo.findByName(any())).thenReturn(easyRandom.nextObject(Topic.class));
        when(helpers.consumerEntityToDto(any())).thenReturn(easyRandom.nextObject(ConsumerDto.class));
        when(helpers.topicEntityToDto(any())).thenReturn(easyRandom.nextObject(TopicDto.class));
        when(helpers.topicDtoToEntity(any())).thenReturn(easyRandom.nextObject(Topic.class));
        assertDoesNotThrow(()->consumerService.subscribeToTopic("",""));
    }

    @Test
    void testProcessConsumeRequests() {
        Consumer consumer = new Consumer();
        consumer.setName("Consumer");
        ConsumerDto consumerDto = new ConsumerDto();
        consumerDto.setName("Consumer");
        Topic topic = new Topic();
        topic.setName("topic");
        topic.setSubscribers(Collections.singletonList(consumer));
        TopicDto topicDto=new TopicDto();
        topicDto.setName("topic");
        topicDto.setSubscribers(Collections.singletonList(consumerDto));
        when(producerRepo.existsById(any())).thenReturn(true);
        when(consumerRepo.findByName(any())).thenReturn(consumer);
        when(topicRepo.findByName(any())).thenReturn(topic);
        when(helpers.consumerEntityToDto(any())).thenReturn(consumerDto);
        when(helpers.topicEntityToDto(any())).thenReturn(topicDto);
        producerService.addMessageToTopic(1,"topic","message");
        consumerService.consumeMessageRequest("Consumer","topic");
        final InboundMessage inboundMessage = new InboundMessage(0L, 0L, 0L, "topicName", "message",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(helpers.inboundMessageDtoToEntity(new InboundMessageDto(0L, 0L, 0L, "topicName", "message",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)))).thenReturn(inboundMessage);
        assertDoesNotThrow(()->consumerService.processConsumeRequests());
    }

    @Test
    void consumeMessageWithoutSubscription() {
        EasyRandom easyRandom = new EasyRandom();
        when(consumerRepo.findByName(any())).thenReturn(easyRandom.nextObject(Consumer.class));
        when(topicRepo.findByName(any())).thenReturn(easyRandom.nextObject(Topic.class));
        when(helpers.consumerEntityToDto(any())).thenReturn(easyRandom.nextObject(ConsumerDto.class));
        when(helpers.topicEntityToDto(any())).thenReturn(easyRandom.nextObject(TopicDto.class));
        assertThrows(CustomGenericException.class,()->consumerService.consumeMessageRequest("",""));
    }

    @Test
    void consumeMessageWithSubscription() {
        ConsumeResponse consumeResponse=new ConsumeResponse(1,"topic","Consumer", Constants.PROCESSING);
        Consumer consumer = new Consumer();
        consumer.setName("Consumer");
        ConsumerDto consumerDto = new ConsumerDto();
        consumerDto.setName("Consumer");
        Topic topic = new Topic();
        topic.setName("topic");
        topic.setSubscribers(Collections.singletonList(consumer));
        TopicDto topicDto=new TopicDto();
        topicDto.setName("topic");
        topicDto.setSubscribers(Collections.singletonList(consumerDto));
        when(consumerRepo.findByName(any())).thenReturn(consumer);
        when(topicRepo.findByName(any())).thenReturn(topic);
        when(helpers.consumerEntityToDto(any())).thenReturn(consumerDto);
        when(helpers.topicEntityToDto(any())).thenReturn(topicDto);
        assertEquals(consumeResponse.getConsumerName(),consumerService.consumeMessageRequest("Consumer","topic").getConsumerName());
    }

    @Test
    void saveConsumer() {
        EasyRandom easyRandom = new EasyRandom();
        Consumer consumer = easyRandom.nextObject(Consumer.class);
        ConsumerDto consumerDto=easyRandom.nextObject(ConsumerDto.class);
        when(helpers.consumerDtoToEntity(any())).thenReturn(consumer);
        when(helpers.consumerEntityToDto(any())).thenReturn(consumerDto);
        assertEquals(consumerDto,consumerService.saveConsumer(consumerDto));
    }
    @Test
    void testProcessConsumeRequestsWhenNoMessage(){
        Consumer consumer = new Consumer();
        consumer.setName("Consumer");
        ConsumerDto consumerDto = new ConsumerDto();
        consumerDto.setName("Consumer");
        Topic topic = new Topic();
        topic.setName("topic");
        topic.setSubscribers(Collections.singletonList(consumer));
        TopicDto topicDto=new TopicDto();
        topicDto.setName("topic");
        topicDto.setSubscribers(Collections.singletonList(consumerDto));
        when(producerRepo.existsById(any())).thenReturn(true);
        when(consumerRepo.findByName(any())).thenReturn(consumer);
        when(topicRepo.findByName(any())).thenReturn(topic);
        when(helpers.consumerEntityToDto(any())).thenReturn(consumerDto);
        when(helpers.topicEntityToDto(any())).thenReturn(topicDto);
        producerService.addMessageToTopic(1,"topic","message");
        consumerService.consumeMessageRequest("Consumer","topic1");
        final InboundMessage inboundMessage = new InboundMessage(0L, 0L, 0L, "topicName", "message",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(helpers.inboundMessageDtoToEntity(new InboundMessageDto(0L, 0L, 0L, "topicName", "message",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)))).thenReturn(inboundMessage);
        assertDoesNotThrow(()->consumerService.processConsumeRequests());
    }

}