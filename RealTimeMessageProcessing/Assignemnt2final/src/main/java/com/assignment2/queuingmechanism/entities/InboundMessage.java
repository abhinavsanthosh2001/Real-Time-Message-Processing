package com.assignment2.queuingmechanism.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboundMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    long producerId;
    long consumerId;
    String topicName;
    String message;
    LocalDateTime subscriptionTime;
}
