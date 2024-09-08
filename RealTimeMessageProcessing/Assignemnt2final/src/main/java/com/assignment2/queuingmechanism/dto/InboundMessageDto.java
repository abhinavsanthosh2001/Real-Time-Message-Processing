package com.assignment2.queuingmechanism.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InboundMessageDto {
    long id;
    long producerId;
    long consumerId;
    String topicName;
    String message;
    LocalDateTime subscriptionTime = LocalDateTime.now();
}