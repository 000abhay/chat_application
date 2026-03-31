package com.chat.repository;

import com.chat.model.ChatRequest;
import com.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {
    @Query("SELECT cr FROM ChatRequest cr WHERE (cr.sender = :user1 AND cr.receiver = :user2) OR (cr.sender = :user2 AND cr.receiver = :user1)")
    Optional<ChatRequest> findExistingRequest(@Param("user1") User user1, @Param("user2") User user2);

    List<ChatRequest> findByReceiverAndStatus(User receiver, ChatRequest.RequestStatus status);
    List<ChatRequest> findBySenderAndStatus(User sender, ChatRequest.RequestStatus status);
    List<ChatRequest> findByReceiverOrSender(User receiver, User sender);
}
