package com.assignment2.queuingmechanism.repository;

import com.assignment2.queuingmechanism.entities.InboundMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundMessageRepo extends JpaRepository<InboundMessage, Long> {
}
