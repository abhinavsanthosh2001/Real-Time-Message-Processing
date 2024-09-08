package com.assignment2.queuingmechanism.repository;

import com.assignment2.queuingmechanism.entities.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepo extends JpaRepository<Consumer, Long> {
    Consumer findByName(String name);
}
