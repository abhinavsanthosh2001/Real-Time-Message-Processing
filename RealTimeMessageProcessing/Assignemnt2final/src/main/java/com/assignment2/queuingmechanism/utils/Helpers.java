package com.assignment2.queuingmechanism.utils;

import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.dto.InboundMessageDto;
import com.assignment2.queuingmechanism.dto.ProducerDto;
import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.entities.Consumer;
import com.assignment2.queuingmechanism.entities.InboundMessage;
import com.assignment2.queuingmechanism.entities.Producer;
import com.assignment2.queuingmechanism.entities.Topic;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Helpers {
    @Autowired
    ModelMapper modelMapper;
    long messageId = 0;
    long consumeRequestId = 0;

    public ProducerDto producerEntityToDto(Producer producer) {
        if (producer == null) {
            return null;
        }
        return modelMapper.map(producer, ProducerDto.class);
    }

    public Producer producerDtoToEntity(ProducerDto producerDto) {
        if (producerDto == null) {
            return null;
        }
        return modelMapper.map(producerDto, Producer.class);
    }

    public ConsumerDto consumerEntityToDto(Consumer consumer) {
        if (consumer == null) {
            return null;
        }
        return modelMapper.map(consumer, ConsumerDto.class);
    }

    public Consumer consumerDtoToEntity(ConsumerDto consumerDto) {
        if (consumerDto == null) {
            return null;
        }
        return modelMapper.map(consumerDto, Consumer.class);
    }

    public InboundMessage inboundMessageDtoToEntity(InboundMessageDto inboundMessageDto) {
        if (inboundMessageDto == null) {
            return null;
        }
        return modelMapper.map(inboundMessageDto, InboundMessage.class);
    }

    public TopicDto topicEntityToDto(Topic topic) {
        if (topic == null) {
            return null;
        }
        return modelMapper.map(topic, TopicDto.class);
    }

    public Topic topicDtoToEntity(TopicDto topicDto) {
        if (topicDto == null) {
            return null;
        }
        return modelMapper.map(topicDto, Topic.class);
    }

    public long generateMessageId() {
        messageId++;
        return messageId;
    }

    public long generateConsumeRequestId() {
        consumeRequestId++;
        return consumeRequestId;
    }
}
