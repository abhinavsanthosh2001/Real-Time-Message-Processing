package com.assignment2.queuingmechanism.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumeResponse {
    long consumeRequestId;
    String topicName;
    String consumerName;
    String status;

}
