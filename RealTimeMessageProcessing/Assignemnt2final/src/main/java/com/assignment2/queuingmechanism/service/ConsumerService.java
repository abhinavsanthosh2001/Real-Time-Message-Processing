package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.constants.ConsumeRequestStatus;
import com.assignment2.queuingmechanism.dto.*;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import com.assignment2.queuingmechanism.repository.ConsumerRepo;
import com.assignment2.queuingmechanism.repository.InboundMessageRepo;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.requests.ConsumeRequest;
import com.assignment2.queuingmechanism.responses.ConsumeResponse;
import com.assignment2.queuingmechanism.utils.Helpers;
import com.assignment2.queuingmechanism.utils.Validations;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerService {
    static Queue<ConsumeRequest> consumeRequests = new ConcurrentLinkedQueue<>();
    ConsumerRepo consumerRepo;
    Helpers helpers;
    InboundMessageRepo inboundMessageRepo;
    Validations validations;
    TopicRepo topicRepo;

    public ConsumerDto saveConsumer(ConsumerDto consumerDto) {
        return helpers.consumerEntityToDto(consumerRepo.save(helpers.consumerDtoToEntity(consumerDto)));
    }

    public void subscribeToTopic(String consumerName, String topicName) {
        ConsumerDto consumer = helpers.consumerEntityToDto(consumerRepo.findByName(consumerName));
        TopicDto topicDto = helpers.topicEntityToDto(topicRepo.findByName(topicName));
        validations.validateSubscribeRequest(topicDto, consumer);
        topicDto.getSubscribers().add(consumer);
        topicRepo.save(helpers.topicDtoToEntity(topicDto));
    }

    public ConsumeResponse consumeMessageRequest(String consumerName, String topicName) {
        ConsumerDto consumer = helpers.consumerEntityToDto(consumerRepo.findByName(consumerName));
        TopicDto topic = helpers.topicEntityToDto(topicRepo.findByName(topicName));
        validations.validateConsumerAndTopic(consumer, topic);
        if (!checkIfSubscribed(topic, consumer)) {
            throw new CustomGenericException(Constants.FORBIDDEN, Constants.NO_SUBSCRIPTION);
        }
        ConsumeRequest consumeRequest = new ConsumeRequest();
        consumeRequest.setId(helpers.generateConsumeRequestId());
        consumeRequest.setConsumer(consumer);
        consumeRequest.setTopic(topic);
        consumeRequests.add(consumeRequest);
        log.info("Message Processing Started for consumer: {}", consumerName);
        return new ConsumeResponse(consumeRequest.getId(), topicName, consumerName, Constants.PROCESSING);
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void processConsumeRequests() throws InterruptedException {
        //log.info("Consumption Started {}",Thread.currentThread());
        Thread.sleep(5000);
        consumeRequests.stream()
                .filter(consumerRequest -> consumerRequest.getStatus() == ConsumeRequestStatus.PENDING)
                .filter(this::checkForPartitions)
                .forEach(consumerRequest -> {
                    log.info("Consumption Started for Request: {} in thread: {}", consumerRequest, Thread.currentThread());
                    getMessageFromPartition(consumerRequest, consumerRequest.getPartition());
                    if (consumerRequest.getStatus() == ConsumeRequestStatus.DONE) {
                        consumeRequests.remove(consumerRequest);
                    }
                });
    }

    private boolean checkForPartitions(ConsumeRequest consumerRequest) {
        if (ProducerService.partitionList.stream()
                .noneMatch(partition -> partition.getTopicName().equals(consumerRequest.getTopic().getName()))) {
            log.info("No messages in the topic: {}", consumerRequest.getTopic().getName());
            consumeRequests.remove(consumerRequest);
        }
        Optional<Partition> optionalPartition = ProducerService.partitionList.stream()
                .filter(partition -> partition.getTopicName().equals(consumerRequest.getTopic().getName()) && partition.isAvailable())
                .findFirst();
        optionalPartition.ifPresent(consumerRequest::setPartition);
        return optionalPartition.isPresent();
    }

    @Async
    private void getMessageFromPartition(ConsumeRequest consumeRequest, Partition partition) {
        partition.setAvailable(false);
        consumeRequest.setStatus(ConsumeRequestStatus.PROCESSING);
        LinkedBlockingQueue<TopicMessageDto> topicMessageDtos = TopicService
                .topics
                .get(consumeRequest.getTopic().getName())
                .get(consumeRequest.getPartition().getProducerId());
        if (topicMessageDtos == null) {
            log.info("No messages in the assigned partition for consumer: {}", consumeRequest.getConsumer().getName());
        }
        TopicMessageDto pollResult = Objects.requireNonNull(topicMessageDtos).poll();
        if (pollResult == null) {
            log.info("No messages left in the assigned partition for consumer: {}", consumeRequest.getConsumer().getName());
        } else {
            log.info("message consumed:{}, by {} from {}", pollResult, consumeRequest.getConsumer().getName(), consumeRequest.getTopic().getName());
            createAndSaveInboundMessage(pollResult.getMessage(), consumeRequest.getTopic().getName(), consumeRequest.getConsumer(), pollResult.getProducerId());
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR, Constants.SOMETHING_WENT_WRONG + e.getMessage());
        }
        partition.setAvailable(true);
        consumeRequest.setStatus(ConsumeRequestStatus.DONE);
    }

    private boolean checkIfSubscribed(TopicDto topic, ConsumerDto consumer) {
        return topic.getSubscribers().stream().map(ConsumerDto::getName)
                .anyMatch(consumerName -> consumerName.equals(consumer.getName()));
    }

    private void createAndSaveInboundMessage(String message, String topicName, ConsumerDto consumer, long producerId) {
        inboundMessageRepo
                .save(helpers
                        .inboundMessageDtoToEntity(InboundMessageDto.builder().message(message).consumerId(consumer.getId()).producerId(producerId).topicName(topicName).subscriptionTime(LocalDateTime.now()).build()));
    }

}
