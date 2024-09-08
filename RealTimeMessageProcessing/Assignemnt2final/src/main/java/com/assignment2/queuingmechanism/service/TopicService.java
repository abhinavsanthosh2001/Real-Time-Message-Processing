package com.assignment2.queuingmechanism.service;

import com.assignment2.queuingmechanism.dto.TopicDto;
import com.assignment2.queuingmechanism.dto.TopicMessageDto;
import com.assignment2.queuingmechanism.repository.TopicRepo;
import com.assignment2.queuingmechanism.utils.Helpers;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicService {
    static Map<String, Map<Long, LinkedBlockingQueue<TopicMessageDto>>> topics = new HashMap<>();
    Helpers helpers;
    TopicRepo topicRepo;

    public TopicDto saveTopic(TopicDto topicDto) {
        return helpers.topicEntityToDto(topicRepo.save(helpers.topicDtoToEntity(topicDto)));
    }

    public Object getTopics() {
        return topics;
    }
}
