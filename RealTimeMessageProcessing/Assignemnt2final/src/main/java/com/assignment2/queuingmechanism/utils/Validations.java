package com.assignment2.queuingmechanism.utils;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Validations {

    public void validateSubscribeRequest(TopicDto topic, ConsumerDto consumer) {
        if (consumer == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_CONSUMER_MESSAGE);
        }
        if (topic == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_TOPIC_MESSAGE);
        }
        for (ConsumerDto subscribedConsumer : topic.getSubscribers()) {
            if (consumer.getName().equals(subscribedConsumer.getName())) {
                throw new CustomGenericException(Constants.BAD_REQUEST, Constants.ALREADY_SUBSCRIBED);
            }
        }
    }

    public void validateConsumerAndTopic(ConsumerDto consumer, TopicDto topic) {
        if (consumer == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_CONSUMER_MESSAGE);
        }
        if (topic == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_TOPIC_MESSAGE);
        }
    }

    public void validatePublishMessageInput(TopicDto topicDto, String message) {
        if (topicDto == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_TOPIC_MESSAGE);
        }
        if (message.length() < 2 || message.length() > 50) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.MESSAGE_LENGTH_VIOLATION);
        }
    }
}
