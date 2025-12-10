package com.smartqueue.repository;

import com.smartqueue.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    @Query("SELECT q FROM QueueEntry q WHERE q.event.id = :eventId AND q.status IN ('WAITING', 'IN_PROGRESS') ORDER BY q.position")
    List<QueueEntry> findActiveQueueByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(q) FROM QueueEntry q WHERE q.event.id = :eventId AND q.status IN ('WAITING', 'IN_PROGRESS')")
    Long countActiveEntriesByEvent(@Param("eventId") Long eventId);

    List<QueueEntry> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query("SELECT q FROM QueueEntry q WHERE q.event.id = :eventId AND q.status = 'IN_PROGRESS' ORDER BY q.updatedAt ASC")
    List<QueueEntry> findCurrentServingByEventId(@Param("eventId") Long eventId);

    @Query("SELECT DISTINCT q FROM QueueEntry q WHERE q.event.id = :eventId ORDER BY q.position")
    List<QueueEntry> findAllByEventId(@Param("eventId") Long eventId);

    List<QueueEntry> findByStatusIn(List<String> statuses);
}
