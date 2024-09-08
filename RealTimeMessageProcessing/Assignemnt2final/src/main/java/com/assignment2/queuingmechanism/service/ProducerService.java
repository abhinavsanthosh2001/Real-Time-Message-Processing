package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.dto.Partition;
import com.assignment2.queuingmechanism.dto.ProducerDto;
import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.dto.TopicMessageDto;
import com.assignment2.queuingmechanism.exceptions.CustomGenericException;
import com.assignment2.queuingmechanism.repository.ProducerRepo;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.responses.PublisherResponse;
import com.assignment2.queuingmechanism.utils.Helpers;
import com.assignment2.queuingmechanism.utils.Validations;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ProducerService {
    static List<Partition> partitionList = new ArrayList<>();
    ProducerRepo producerRepo;
    Helpers helpers;
    Validations validations;
    TopicRepo topicRepo;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    TopicService topicService;

    public ProducerDto saveProducer(ProducerDto producerDto) {
        return helpers.producerEntityToDto(producerRepo.save(helpers.producerDtoToEntity(producerDto)));
    }

    @Async
    public Future<PublisherResponse> addMessageToTopic(long producerId, String topicName, String message) {
        if (!producerRepo.existsById(producerId)) {
            throw new CustomGenericException(Constants.BAD_REQUEST, Constants.NO_PRODUCER_MESSAGE);
        }
        TopicDto topicDto = helpers.topicEntityToDto(topicRepo.findByName(topicName));
        validations.validatePublishMessageInput(topicDto, message);
        TopicMessageDto messageDto = new TopicMessageDto(helpers.generateMessageId(), producerId, message);
        addMessage(topicName, messageDto);
        log.info("successfully added message to topic:{}", topicName);
        return executor.submit(() -> new PublisherResponse(producerId, topicName, message));
    }

    private void addMessage(String topicName, TopicMessageDto messageDto) {
        TopicService
                .topics
                .computeIfAbsent(topicName, k -> new HashMap<>());
        TopicService
                .topics
                .get(topicName)
                .computeIfAbsent(messageDto.getProducerId(), k -> new LinkedBlockingQueue<>())
                .add(messageDto);
        addToPartitionList(topicName, messageDto);
    }

    private void addToPartitionList(String topicName, TopicMessageDto messageDto) {
        Optional<Partition> optionalPartition = partitionList.stream()
                .filter(partition -> partition.getProducerId() == messageDto.getProducerId())
                .filter(partition -> partition.getTopicName().equals(topicName))
                .findFirst();

        if (optionalPartition.isPresent()) {
            log.info(partitionList.toString());
            return;
        }
        Partition partition = new Partition();
        partition.setProducerId(messageDto.getProducerId());
        partition.setTopicName(topicName);
        partitionList.add(partition);
        log.info(partitionList.toString());
    }
}
