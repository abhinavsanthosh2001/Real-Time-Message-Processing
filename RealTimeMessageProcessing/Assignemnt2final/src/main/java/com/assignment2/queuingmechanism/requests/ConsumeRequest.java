package com.assignment2.queuingmechanism.requests;

import com.assignment2.queuingmechanism.constants.ConsumeRequestStatus;
import com.assignment2.queuingmechanism.dto.ConsumerDto;
import com.assignment2.queuingmechanism.dto.Partition;
import com.assignment2.queuingmechanism.dto.TopicDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumeRequest {
    long id;
    ConsumerDto consumer;
    TopicDto topic;
    Partition partition;
    ConsumeRequestStatus status = ConsumeRequestStatus.PENDING;
}
