package com.hidroponic.backend.repository;

import com.hidroponic.backend.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
