package com.assignment2.queuingmechanism.repository;

import com.assignment2.queuingmechanism.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepo extends JpaRepository<Producer, Long> {
}
