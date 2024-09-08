package com.assignment2.queuingmechanism.repository;

import com.assignment2.queuingmechanism.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo extends JpaRepository<Topic, Long> {
    Topic findByName(String name);

}
