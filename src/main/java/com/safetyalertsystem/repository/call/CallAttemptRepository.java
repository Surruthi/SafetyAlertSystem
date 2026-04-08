package com.safetyalertsystem.repository.call;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safetyalertsystem.entity.call.CallAttempt;

public interface CallAttemptRepository extends JpaRepository<CallAttempt, Long> {

    List<CallAttempt> findByAlertIdOrderByIdAsc(Long alertId);

    Optional<CallAttempt> findByCallSid(String callSid);
}